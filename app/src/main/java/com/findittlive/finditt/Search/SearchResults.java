package com.findittlive.finditt.Search;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.cloudant.sync.datastore.BasicDocumentRevision;
import com.cloudant.sync.datastore.Datastore;
import com.cloudant.sync.datastore.DatastoreManager;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.common.eventbus.Subscribe;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;


public class SearchResults extends Activity {

private ListView listView1;
    private static final String DATASTORE_MANGER_DIR = "fData";
    private static final String DATASTORE_NAME = "finditt";
    ProgressDialog barProgressDialog;
    Handler updateBarHandler;
    File path ;//= getApplicationContext().getDir(DATASTORE_MANGER_DIR, MODE_PRIVATE);
    DatastoreManager manager;// = new DatastoreManager(path.getAbsolutePath());
    Datastore datastore;// = manager.openDatastore(DATASTORE_NAME);
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        listView1 = (ListView)findViewById(R.id.listview1);
        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
Intent intent = getIntent();
        String mequery = intent.getStringExtra("mequery");
        mAdView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // .addTestDevice("abc") //Random Text
                .build();

        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);


       // path = getApplicationContext().getDir(DATASTORE_MANGER_DIR, MODE_PRIVATE);
       // manager = new DatastoreManager(path.getAbsolutePath());
       // datastore = manager.openDatastore(DATASTORE_NAME);
     //  int nDocs = globalVariable.OpenDataStore(getApplicationContext()).getDocumentCount();
       // showToast("Loading Completed " +nDocs+ " Locations Found");
       // List<BasicDocumentRevision> alldocs = globalVariable.OpenDataStore(getApplicationContext()).getAllDocuments(0, nDocs, true);
        List<Items> myItems = new ArrayList<Items>();

        IndexManager indexManager;
        try {
            indexManager = new IndexManager(globalVariable.OpenDataStore(getApplicationContext()));
            indexManager.ensureIndexed("Category", "Category");
            QueryBuilder query = new QueryBuilder();
            query.index("Category").oneOf(mequery);
            QueryResult result = indexManager.query(query.build());
           // showToast("Search Completed, " + result.size() + " Locations Found");
            for(BasicDocumentRevision rev : result) {
                Items t = Items.giveDetails(rev,getApplicationContext());
                if (t != null) {
                    myItems.add(t);
                }
                myListView(myItems);

            }
        }catch (Exception e){
            Log.e("IndexError","Could not create index on dataastore");
            e.printStackTrace();

        }


/**
        // Filter all documents down to those of type Task.
        for(BasicDocumentRevision rev : alldocs) {
            Items t = Items.giveDetails(rev,getApplicationContext());
            if (t != null) {
                myItems.add(t);
            }
            myListView(myItems);

        }
 **/


    }

    @Override
    protected void onStart() {
        super.onStart();


     //  launchRingDialog();
       // myListView();
    }

    public void launchRingDialog() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                        int nDocs = datastore.getDocumentCount();

                       // showToast("Synchronization Completed " +nDocs+ " Documents FOUND");
                        List<BasicDocumentRevision> alldocs = datastore.getAllDocuments(0, nDocs, true);
                        List<Items> myItems = new ArrayList<Items>();



                        // Filter all documents down to those of type Task.
                        for(BasicDocumentRevision rev : alldocs) {
                            Items t = Items.giveDetails(rev,getApplicationContext());
                            if (t != null) {
                                myItems.add(t);
                            }
                            myListView(myItems);

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
                Toast.makeText(SearchResults.this, toast, Toast.LENGTH_LONG).show();
               //int nDocs = datastore.getDocumentCount();
                // BasicDocumentRevision rev = datastore.getAllDocuments(0,nDocs,true);
               //Toast.makeText(MyActivity.this, nDocs, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void myListView(final List theItems) {
        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        List<Items> Items = theItems;
        Collections.sort(Items);
        ItemAdapter adapter = new ItemAdapter(SearchResults.this,Items);
        adapter.notifyDataSetChanged();
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Items thisItem = (Items)(adapterView.getItemAtPosition(position));

                globalVariable.setName(thisItem.getBusinessName());
                globalVariable.setPhone(thisItem.getPhone());
                globalVariable.setDescription(thisItem.getDescription());
globalVariable.setType(thisItem.getType());
                globalVariable.setAddress(thisItem.getAddress());
                globalVariable.setMaLongitude(thisItem.getMaLongitude());
                globalVariable.setMaLatitude(thisItem.getMaLatitude());
                           globalVariable.setImage(thisItem.getImage());
                globalVariable.setImage2(thisItem.getImage2());
                globalVariable.setImage3(thisItem.getImage3());
                globalVariable.setImage4(thisItem.getImage4());
                globalVariable.setImage5(thisItem.getImage5());
                globalVariable.setBizlocation(thisItem.getBizlocation());
                Intent i = new Intent(SearchResults.this,Content.class);

                startActivity(i);
            }
        });
        listView1.setAdapter(adapter);
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }
}
