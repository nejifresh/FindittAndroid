package com.findittlive.finditt.News;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.cloudant.sync.datastore.BasicDocumentRevision;
import com.cloudant.sync.datastore.Datastore;
import com.cloudant.sync.indexing.IndexManager;
import com.cloudant.sync.indexing.QueryBuilder;
import com.cloudant.sync.indexing.QueryResult;
import com.cloudant.sync.notifications.ReplicationCompleted;
import com.cloudant.sync.notifications.ReplicationErrored;
import com.cloudant.sync.replication.ErrorInfo;
import com.cloudant.sync.replication.PullReplication;
import com.cloudant.sync.replication.Replicator;
import com.cloudant.sync.replication.ReplicatorFactory;
import com.findittlive.finditt.R;
import com.findittlive.finditt.Search.GlobalClass;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.common.eventbus.Subscribe;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Neji on 11/8/2014.
 */
public class HotActivity extends Activity {
    private ListView listView1;
    private static final String DATASTORE_MANGER_DIR = "findittData";
    private static final String DATASTORE_NAME = "findittnews";
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot);
        listView1 = (ListView)findViewById(R.id.listviewHot);

        launchRingDialog();

    }

    public void myListView(final List theNews) {
        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        List<News> myNews = theNews;
        Collections.sort(myNews);
        NewsAdapter adapter = new NewsAdapter(getApplicationContext(),myNews);
        adapter.notifyDataSetChanged();

        listView1.setAdapter(adapter);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                 News thisNews = (News)(adapterView.getItemAtPosition(position));
                globalVariable.setHeadline(thisNews.getHeadline());
                globalVariable.setBody(thisNews.getBody());
                globalVariable.setSource(thisNews.getNewsSource());
                globalVariable.setArticledate(thisNews.getTimedifference());
globalVariable.setImagenews(thisNews.getBitmap());
                Intent i = new Intent(HotActivity.this,NewsContent.class);
                startActivity(i);
/**
                 globalVariable.setImage3(thisItem.getImage3());
                 globalVariable.setImage4(thisItem.getImage4());
                 globalVariable.setImage5(thisItem.getImage5());

                 **/
            }
        });
    }
    public void launchRingDialog() {
        final ProgressDialog ringProgressDialog =
                ProgressDialog.show(HotActivity.this, "Please wait ...",	"Getting Latest News Updates..", true);
        ringProgressDialog.setCancelable(false);
        //ringProgressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Here you should write your time consuming task...
                    // Create a replicator that replicates changes from the local
// datastore to t//he remote database.
                   final GlobalClass globalVariable = (GlobalClass) getApplicationContext();


                    PullReplication pull = new PullReplication();
                    pull.username = "jandtheryingstisithemide";
                    pull.password = "KtJfIjsBCSw2OiwthESStAXd";
                    pull.target = globalVariable.OpenNewsStore(getApplicationContext());
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
                        int nDocs = globalVariable.OpenDataStore(getApplicationContext()).getDocumentCount();
                        showToast("Unable to synchronize, please check your data connection or try again");


                    }
                    else{
//Log.d("TAG","Replication completed successfully");


                        showToast("Updates Retrieved Successfully");

                    }


                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private URI createServerURI()
            throws URISyntaxException {
        URI uri = new URI("https://finditt.cloudant.com/findittnews");
        return uri;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // .addTestDevice("abc") //Random Text
                .build();

        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);
        List<News> newsItems = new ArrayList<News>();
        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

        int nDocs = globalVariable.OpenNewsStore(getApplicationContext()).getDocumentCount();

        Datastore newstore = globalVariable.OpenNewsStore(getApplicationContext());
        IndexManager indexManager;
        try{
            indexManager = new IndexManager(newstore);
            indexManager.ensureIndexed("Category", "Category");

        QueryBuilder query = new QueryBuilder();
        query.index("Category").oneOf("Gossip");
        QueryResult result = indexManager.query(query.build());

        List<BasicDocumentRevision> alldocs = newstore.getAllDocuments(0, nDocs, true);
       // showToast("Synchronization Completed " + alldocs.size()+ " Documents FOUND");

        for(BasicDocumentRevision rev : result) {
            News t = News.giveNewsDetails(rev,getApplicationContext());
            if (t != null) {
                newsItems.add(t);
            }
            Log.d("Time","created list items");
            myListView(newsItems);

        }
        } catch (Exception e){

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void showToast(final String toast)
    {
        runOnUiThread(new Runnable() {
            public void run()
            {
                Toast.makeText(HotActivity.this, toast, Toast.LENGTH_LONG).show();
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
}
