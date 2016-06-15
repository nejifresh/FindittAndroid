package com.findittlive.finditt.Search;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.cloudant.sync.datastore.BasicDocumentRevision;
import com.cloudant.sync.datastore.Datastore;
import com.cloudant.sync.datastore.DatastoreManager;
import com.cloudant.sync.notifications.ReplicationCompleted;
import com.cloudant.sync.notifications.ReplicationErrored;
import com.cloudant.sync.replication.ErrorInfo;
import com.cloudant.sync.replication.PullReplication;
import com.cloudant.sync.replication.Replicator;
import com.cloudant.sync.replication.ReplicatorFactory;
import com.findittlive.finditt.R;
import com.google.common.eventbus.Subscribe;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;


public class MActivity extends Activity {
private ListView listView1;
    private static final String DATASTORE_MANGER_DIR = "fData";
    private static final String DATASTORE_NAME = "finditt";
    ProgressDialog barProgressDialog;
    Handler updateBarHandler;
    File path ;//= getApplicationContext().getDir(DATASTORE_MANGER_DIR, MODE_PRIVATE);
    DatastoreManager manager;// = new DatastoreManager(path.getAbsolutePath());
    Datastore datastore;// = manager.openDatastore(DATASTORE_NAME);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        listView1 = (ListView)findViewById(R.id.listview1);


        path = getApplicationContext().getDir(DATASTORE_MANGER_DIR, MODE_PRIVATE);
        manager = new DatastoreManager(path.getAbsolutePath());
        datastore = manager.openDatastore(DATASTORE_NAME);
        PullReplication pull = new PullReplication();
        pull.username = "finditt";
        pull.password = "neji4luv";
        pull.target = datastore;
        try {
            pull.source = createServerURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        updateBarHandler = new Handler();



    }

    @Override
    protected void onStart() {
        super.onStart();

       launchRingDialog();
       // myListView();
    }

    public void launchRingDialog() {
        final ProgressDialog ringProgressDialog =
                ProgressDialog.show(MActivity.this, "Please wait ...",	"Synchronization in progress ...", true);
        ringProgressDialog.setCancelable(false);
        //ringProgressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Here you should write your time consuming task...
                    // Create a replicator that replicates changes from the local
// datastore to t//he remote database.
                   // File path = getApplicationContext().getDir(DATASTORE_MANGER_DIR,MODE_PRIVATE);
                    //DatastoreManager manager = new DatastoreManager(path.getAbsolutePath());
                   // Datastore datastore = manager.openDatastore(DATASTORE_NAME);
                    PullReplication pull = new PullReplication();
                    pull.username = "finditt";
                    pull.password = "neji4luv";
                    pull.target = datastore;
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
                    ringProgressDialog.dismiss();
                    if (replicator.getState() != Replicator.State.COMPLETE) {
                        System.out.println("Error replicating TO remote");
                        System.out.println(listener.error);
                        int nDocs = datastore.getDocumentCount();
                        showToast("Unable to synchronize, please try again later");


                    }
                    else{
//Log.d("TAG","Replication completed successfully");
                        int nDocs = datastore.getDocumentCount();

                       showToast("Synchronization Completed " +nDocs+ " Documents FOUND");
                        List<BasicDocumentRevision> alldocs = datastore.getAllDocuments(0, nDocs, true);
                        List<Items> myItems = new ArrayList<Items>();



                        // Filter all documents down to those of type Task.
                        for(BasicDocumentRevision rev : alldocs) {
                            Items t = Items.giveDetails(rev,getApplicationContext());
                            if (t != null) {
                                myItems.add(t);
                            }
                           // myListView(myItems);
                        }

/**
                       ArrayAdapter<String> adapter = new ArrayAdapter<String>(MyActivity.this,
                                android.R.layout.simple_list_item_1, ity);

                        showToast("Synchronization Completed " +myItems.size()+ " Documents FOUND");
                       listView1.setAdapter(adapter);
 **/
                        //now load listview
                        //  List<Task> tasks = sTasks.allTasks();
                        //  mTaskAdapter = new DocAdapter(Demo_Activity.this,tasks);

                        // reloadTasksFromModel();

/**

 int pageSize = datastore.getDocumentCount();
 List<BasicDocumentRevision> docs = datastore.getAllDocuments(0, pageSize, true);

 DocAdapter adapter = new DocAdapter(Demo_Activity.this,docs);

 // ArrayAdapter adapter = new ArrayAdapter(this,docs)
 **/
                        // lv.setAdapter(mTaskAdapter);

                    }


                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private URI createServerURI()
            throws URISyntaxException {
        URI uri = new URI("https://finditt.cloudant.com/finditt");
        return uri;
    }
    public void showToast(final String toast)
    {
        runOnUiThread(new Runnable() {
            public void run()
            {
                Toast.makeText(MActivity.this, toast, Toast.LENGTH_LONG).show();
               //int nDocs = datastore.getDocumentCount();
                // BasicDocumentRevision rev = datastore.getAllDocuments(0,nDocs,true);
               //Toast.makeText(MyActivity.this, nDocs, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void myListView(final List theItems)
    {
        runOnUiThread(new Runnable() {
            public void run()
            {
                List<Items> Items = theItems;
                ItemAdapter adapter = new ItemAdapter(MActivity.this,Items);

              //  ArrayAdapter<Items> adapter = new ArrayAdapter<Items>(MyActivity.this,                        android.R.layout.simple_list_item_1, Items);
                listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        Items thisItem = (Items)(adapterView.getItemAtPosition(position));

                        Toast.makeText(getApplicationContext(),thisItem.getBusinessName(),Toast.LENGTH_LONG).show();
                    }
                });
               listView1.setAdapter(adapter);
adapter.notifyDataSetChanged();


            }
        });
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

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }
}
