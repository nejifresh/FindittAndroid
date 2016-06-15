package com.findittlive.finditt.receivers;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.cloudant.sync.notifications.ReplicationCompleted;
import com.cloudant.sync.notifications.ReplicationErrored;
import com.cloudant.sync.replication.ErrorInfo;
import com.cloudant.sync.replication.PullReplication;
import com.cloudant.sync.replication.Replicator;
import com.cloudant.sync.replication.ReplicatorFactory;
import com.findittlive.finditt.Search.GlobalClass;
import com.google.common.eventbus.Subscribe;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Neji on 11/16/2014.
 */
public class SyncService extends IntentService {
    public SyncService() {
        super(SyncService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("SERV","Service Started");
        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

        // File path = getApplicationContext().getDir(DATASTORE_MANGER_DIR,MODE_PRIVATE);
        // DatastoreManager manager = new DatastoreManager(path.getAbsolutePath());
        // Datastore datastore = manager.openDatastore(DATASTORE_NAME);
        PullReplication pull = new PullReplication();
        pull.username = "mencesectownstanybrister";
        pull.password = "qbeGkq7mpdoQ0avIriNwa0em";
        pull.target = globalVariable.OpenDataStore(getApplicationContext());
        try{
            pull.source = createServerURI();
        } catch (URISyntaxException e){
            e.printStackTrace();
        }

        final Replicator replicator = ReplicatorFactory.oneway(pull);
        CountDownLatch latch = new CountDownLatch(1);
        Listener listener = new Listener(latch);
        replicator.getEventBus().register(listener);

        try{
            replicator.start();

            latch.await();
            replicator.getEventBus().unregister(listener);
        } catch (Exception e) {

        }
     // REPLICATION ACTION HAS ENDED   ringProgressDialog.dismiss();
        if (replicator.getState() != Replicator.State.COMPLETE) {
            System.out.println("Error replicating TO remote");
            System.out.println(listener.error);
            int nDocs = globalVariable.OpenDataStore(getApplicationContext()).getDocumentCount();
           // showToast("Unable to synchronize, please check your data connection or try again");


        }
        else{
//Log.d("TAG","Replication completed successfully");
           // showToast("Synchronization completed successfully");
Log.d("SERV","Synchronization has completed");
        }



}

    private URI createServerURI()
            throws URISyntaxException {
        URI uri = new URI("https://finditt.cloudant.com/finditt");
        return uri;
    }

    private class Listener {

        private final CountDownLatch latch;
        public ErrorInfo error = null;

        Listener(CountDownLatch latch) {
            this.latch = latch;
        }

        @Subscribe
        public void complete(ReplicationCompleted event) {
            latch.countDown();


        }

        @Subscribe
        public void error(ReplicationErrored event) {
            this.error = event.errorInfo;
            latch.countDown();


        }

    }
}
