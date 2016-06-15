package com.findittlive.finditt.slidermenu;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.findittlive.finditt.News.HotActivity;
import com.findittlive.finditt.News.News;
import com.findittlive.finditt.News.NewsActivity;
import com.findittlive.finditt.R;
import com.findittlive.finditt.Search.GlobalClass;

import com.findittlive.finditt.Search.TopResults;
import com.findittlive.finditt.receivers.SyncService;
import com.findittlive.finditt.slidingadapter.NavDrawerListAdapter;
import com.findittlive.finditt.slidingmodel.NavDrawerItem;
import com.google.common.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.parse.Parse;
import com.parse.PushService;
import com.plotprojects.retail.android.Plot;
import com.plotprojects.retail.android.PlotConfiguration;

public class MyActivity extends Activity {
private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    // nav drawer title
    private CharSequence mDrawerTitle;
    // used to store app title
    private CharSequence mTitle;
    // slide menu items
    private String[] navMenuTitles;
    private String[] navMenuTitles2;
    private String[] navMenuTitles3;
    private TypedArray navMenuIcons;
    String Latitude = "0";
    String Longitude = "0";
    Double gotLat;
    Double gotLong;


    private ArrayList<NavDrawerItem> navDrawerItems;

    private NavDrawerListAdapter adapter;
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
        setContentView(R.layout.activity_my);
        //plot geofencing
        PlotConfiguration config = new PlotConfiguration("9xV3shZHvmYMXkSz");
        Plot.init(this, config);
       // Parse.initialize(this, "3DCe29CX1oUSFnaGP09bFB0KM0sYNOR51G3ZYIZK", "VHvjkHJyuRFZ5qwNpIwrGbfIGuFxu1LrXaFoFmvq");
        // Also in this method, specify a default Activity to handle push notifications
       // PushService.setDefaultPushCallback(this, MyActivity.class);

        //ad section
        mAdView = (AdView)findViewById(R.id.adView);

        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
               // .addTestDevice("abc") //Random Text
                .build();

        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);

//start the service
        Intent servi = new Intent(this, SyncService.class);
        startService(servi);


        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

        updateBarHandler = new Handler();


       mTitle = mDrawerTitle = getTitle();
//load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.search_items);
        navMenuTitles2 = getResources().getStringArray(R.array.lounge_items);
        navMenuTitles3 = getResources().getStringArray(R.array.twing_items);

        //nav drawer menu icons
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerList = (ListView)findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<NavDrawerItem>();
        // adding nav drawer items to array
        //add the header first
        navDrawerItems.add(new NavDrawerItem("AROUND ME"));
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0],navMenuIcons.getResourceId(6,-1)));
        // Find People
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // Photos
       // navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));

        navDrawerItems.add(new NavDrawerItem(navMenuTitles3[0],navMenuIcons.getResourceId(5,-1)));
        // Latest News
        navDrawerItems.add(new NavDrawerItem(navMenuTitles3[1], navMenuIcons.getResourceId(10, -1)));
        // Communities, Will add a counter here
       // navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));
        // Pages
       // navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        // What's hot, We  will add a counter here
      // navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));


     //   navDrawerItems.add(new NavDrawerItem("THE LOUNGE"));

        // Home
       // navDrawerItems.add(new NavDrawerItem(navMenuTitles2[0],navMenuIcons.getResourceId(7,-1)));
        // Find People
      //  navDrawerItems.add(new NavDrawerItem(navMenuTitles2[1], navMenuIcons.getResourceId(8, -1)));
        // Photos
       // navDrawerItems.add(new NavDrawerItem(navMenuTitles2[2], navMenuIcons.getResourceId(9, -1)));
        // Communities, Will add a counter here
       // navDrawerItems.add(new NavDrawerItem(navMenuTitles2[3], navMenuIcons.getResourceId(3, -1), true, "22"));
        // Pages
       // navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        // What's hot, We  will add a counter here
       // navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));

     //   navDrawerItems.add(new NavDrawerItem("TWINGBOX"));

        // Whats hot
      //  navDrawerItems.add(new NavDrawerItem(navMenuTitles3[0],navMenuIcons.getResourceId(5,-1)));
        // Latest News
       // navDrawerItems.add(new NavDrawerItem(navMenuTitles3[1], navMenuIcons.getResourceId(10, -1)));
        // TV Shows
       // navDrawerItems.add(new NavDrawerItem(navMenuTitles3[2], navMenuIcons.getResourceId(11, -1)));
        // Communities, Will add a counter hereRadio
       // navDrawerItems.add(new NavDrawerItem(navMenuTitles3[3], navMenuIcons.getResourceId(12, -1), true, "22"));
     //   navDrawerItems.add(new NavDrawerItem(navMenuTitles3[4], navMenuIcons.getResourceId(9, -1)));
      //  navDrawerItems.add(new NavDrawerItem(navMenuTitles3[5], navMenuIcons.getResourceId(9, -1)));
//recycle the typed array
        navMenuIcons.recycle();
        //set the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),navDrawerItems);
        mDrawerList.setAdapter(adapter);
       mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int position, long ID) {
               // display view for selected nav drawer item
               displayView(position);
           }
       });




// enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        //toggle button settings
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ){
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        if (savedInstanceState == null) {
            // on first time display view for first nav item
           displayView(1);
        }

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
                Toast.makeText(MyActivity.this, toast, Toast.LENGTH_LONG).show();
                //int nDocs = datastore.getDocumentCount();
                // BasicDocumentRevision rev = datastore.getAllDocuments(0,nDocs,true);
                //Toast.makeText(MyActivity.this, nDocs, Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_refresh:
                // sync

               launchRingDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                //this is the header so do nothing
                break;
            case 1:
              //  fragment = new FindPeopleFragment();
                fragment = new HomeFragment();
                break;


            case 2:
              //  fragment = new MyLoginFragment();
              //  fragment = new PhotosFragment();
             Intent i = new Intent(MyActivity.this,TopResults.class);
             startActivity(i);
                break;

            case 3:
                Intent inty = new Intent(MyActivity.this,HotActivity.class);
                startActivity(inty);
                break;


            case 4:
                Intent intent = new Intent(MyActivity.this,NewsActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    public void launchRingDialog() {
        final ProgressDialog ringProgressDialog =
                ProgressDialog.show(MyActivity.this, "Please wait ...",	"Synchronization in progress..", true);
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
                    ringProgressDialog.dismiss();
                    if (replicator.getState() != Replicator.State.COMPLETE) {
                        System.out.println("Error replicating TO remote");
                        System.out.println(listener.error);
                        int nDocs = globalVariable.OpenDataStore(getApplicationContext()).getDocumentCount();
                        showToast("Unable to synchronize, please check your data connection or try again");


                    }
                    else{
//Log.d("TAG","Replication completed successfully");
                        showToast("Synchronization completed successfully");

                    }


                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
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
    protected void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }


}
