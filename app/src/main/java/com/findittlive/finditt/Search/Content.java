package com.findittlive.finditt.Search;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.findittlive.finditt.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by Neji on 10/26/2014.
 */
public class Content extends TabActivity {
public String latitude;
    public String longitude;
    public String BizName;
    public String Address;
    public String Description;
    public String Phone;
    public Bitmap bmap;
TextView titleText;
    private AdView mAdView;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);
        final GlobalClass globalVariable= (GlobalClass) getApplicationContext();
        String signtype = globalVariable.getType();
       // Toast.makeText(getApplicationContext(),signtype,Toast.LENGTH_LONG).show();
        if (signtype.equals("BASIC")){
            TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);


            TabHost.TabSpec tab1 = tabHost.newTabSpec("First Tab");
            tab1.setIndicator("Details");
            tab1.setContent(new Intent(this,Tab1Activity.class));
            tabHost.addTab(tab1);
        } else{

     //   bmap = intent.get(bmap);
        // create the TabHost that will contain the Tabs
        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);


        TabHost.TabSpec tab1 = tabHost.newTabSpec("First Tab");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("Second Tab");


        // Set the Tab name and Activity
        // that will be opened when particular Tab will be selected
        tab1.setIndicator("Details");
        tab1.setContent(new Intent(this,Tab1Activity.class));

        tab2.setIndicator("View More");
        tab2.setContent(new Intent(this,Tab2Activity.class));



        /** Add the tabs  to the TabHost to display. */
        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        }

        //Tab1Activity activity = (Tab1Activity)getLocalActivityManager().getActivity("First Tab");
//activity.SetTitle("My Title");
       // titleText =  (TextView)activity.findViewById(R.id.titletext);
       // titleText.setText("Hello");
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
    }
}

