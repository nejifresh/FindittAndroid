package com.findittlive.finditt.slidermenu;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Address;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.findittlive.finditt.R;
import com.findittlive.finditt.Search.GPSTracker;
import com.findittlive.finditt.Search.GlobalClass;
import com.findittlive.finditt.Search.MActivity;
import com.findittlive.finditt.Search.SearchResults;
import com.findittlive.finditt.maps.Map_Activity;
import com.findittlive.finditt.receivers.SyncService;
/**
import com.openenviron.andeasylib.EasyLocation;
import com.openenviron.andeasylib.EasySensor;
import com.openenviron.andeasylib.EasyTelephony;
**/
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Neji on 10/5/2014.
 */
public class HomeFragment extends Fragment {
    private AutoCompleteTextView actv;
    private Button btnPickLocation;
    private Button btnSelect;
    private TextView locationText;
    List<Address> addresses = null;
    String addressText="";
    Handler updateBarHandler;
    BroadcastReceiver br;
    String Latitude = "0";
    String Longitude = "0";
    Double gotLat;
    Double gotLong;
    public static final String MyPREFERENCES = "GeoPrefs";
    private SliderLayout mDemoSlider;
   public static final String latkey = "0.0";
    public static final String longkey = "0.0";
    Button btnClick;
    private TextView txtRadius;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        final String[] categories = getResources().
                getStringArray(R.array.categories);
        Arrays.sort(categories);
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,categories);
        //image slider
        mDemoSlider = (SliderLayout)rootView.findViewById(R.id.slider);
        HashMap<String, Integer> bitmap_maps = new HashMap<String, Integer>();
        bitmap_maps.put("Welcome to Finditt",R.drawable.findme);
        bitmap_maps.put("Search for the Best Restaurants",R.drawable.abachaimage);
        bitmap_maps.put("Looking for a Taxi?",R.drawable.cabimae);
        bitmap_maps.put("Get the best African local delicacies", R.drawable.africaimage);
        bitmap_maps.put("Shop at the finest malls", R.drawable.mallimage);

          for (String i:bitmap_maps.keySet()) {
            DefaultSliderView imSlider = new DefaultSliderView(getActivity());
            imSlider
                    .image(bitmap_maps.get(i))
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            mDemoSlider.addSlider(imSlider);
        }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(2500);



        final GlobalClass globalVariable= (GlobalClass)getActivity().getApplicationContext();
txtRadius = (TextView)rootView.findViewById(R.id.txtradius);
btnSelect = (Button)rootView.findViewById(R.id.btnSelect);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                final AlertDialog alert = alertDialog.create();
                LayoutInflater inflater =getActivity().getLayoutInflater();
                final View convertView = (View) inflater.inflate(R.layout.custom, null);
                alert.setView(convertView);
                alert.setTitle("Select an Item");
             final  ListView lv = (ListView) convertView.findViewById(R.id.listView1);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,categories);
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String selectedFromList =(String) (lv.getItemAtPosition(i));
                        actv.setText(selectedFromList);
                        alert.dismiss();

                    }
                });
                alert.show();
            }
        });

        actv = (AutoCompleteTextView) rootView.findViewById(R.id.AutoCompleteTextView1);
        actv.setAdapter(adapter);

        locationText = (TextView)rootView.findViewById(R.id.nearToText);
        actv.setText(globalVariable.getQuery());

        btnClick = (Button)rootView.findViewById(R.id.btnClick);
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!txtRadius.getText().toString().matches("")){
                    globalVariable.setQuery(actv.getText().toString());
globalVariable.setRadius(Float.valueOf(txtRadius.getText().toString()));
                    Intent i = new Intent(getActivity(), SearchResults.class);
                    i.putExtra("mequery",actv.getText().toString());
                    startActivity(i);

                } else{
                    txtRadius.setError("Enter Search Radius");
                    return;
                }

            }
        });

        btnPickLocation = (Button)rootView.findViewById(R.id.btnPickLocation);
        btnPickLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
Intent intent = new Intent(getActivity(), Map_Activity.class);
               globalVariable.setQuery(actv.getText().toString());
                intent.putExtra("latitude",globalVariable.getMaLatitude());
                intent.putExtra("longitude",globalVariable.getMaLongitude());
                startActivity(intent);
               getActivity().finish();
            }
        });
LoadValues();

        return rootView;
    }

    private void LoadValues(){
        SharedPreferences sharedpreferences;
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        GPSTracker locator = new GPSTracker(getActivity().getApplicationContext());
        Location loc = locator.getLocation();

if (sharedpreferences.getString("latitude", "").isEmpty() && sharedpreferences.getString("longitude", "").isEmpty()){

    try{
        locationText.setText(Double.toString(loc.getLatitude()) +","+ Double.toString(loc.getLongitude()));

    }catch (Exception e){

    }
     final GlobalClass globalVariable= (GlobalClass)getActivity().getApplicationContext();
    if (loc== null){
Toast.makeText(getActivity().getApplicationContext(),"LOCATION IS TURNED OFF: Please turn on Location", Toast.LENGTH_LONG).show();
        globalVariable.setMaLatitude("0.0000");
        globalVariable.setMaLongitude("0.0000");

        globalVariable.setCurrLatitude("0.0000");
        globalVariable.setCurrLongitude("0.0000");
    } else {

        globalVariable.setMaLatitude(Double.toString(loc.getLatitude()));
        globalVariable.setMaLongitude(Double.toString(loc.getLongitude()));

        globalVariable.setCurrLatitude(Double.toString(loc.getLatitude()));
        globalVariable.setCurrLongitude(Double.toString(loc.getLongitude()));
    }
ReverseGeocodingTask geocodingTask = new ReverseGeocodingTask(getActivity().getApplicationContext());
    geocodingTask.execute();


        } else {
    gotLat = Double.parseDouble(sharedpreferences.getString(latkey, "0.0"));
    final GlobalClass globalVariable = (GlobalClass) getActivity().getApplicationContext();
    globalVariable.setMaLatitude(sharedpreferences.getString("latitude", ""));
    globalVariable.setMaLongitude(sharedpreferences.getString("longitude", ""));

    globalVariable.setCurrLatitude(sharedpreferences.getString("latitude", ""));
    globalVariable.setCurrLongitude(sharedpreferences.getString("longitude", ""));

    locationText.setText((sharedpreferences.getString("latitude", "")) + "," + (sharedpreferences.getString("longitude", "")));
    sharedpreferences.edit().clear().commit();
    ReverseGeocodingTask geocodingTask = new ReverseGeocodingTask(getActivity().getApplicationContext());
    geocodingTask.execute();

}


    }

    double roundTwoDecimals(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.####");
        return Double.valueOf(twoDForm.format(d));
    }
/**
    private void exampleOne() {
        new Thread() {
            @Override
            public void run() {

                // Create a Broadcast Reciver for using the library, it is needed for every module, as all the available data is brodcasted as intents.
                BroadcastReceiver br = new BroadcastReceiver() {

                    @Override
                    public void onReceive(Context context, Intent intent) {
                        // The Latitude data is available as string Extra from the intent.
                        String Latitude = "0";
                        String Longitude = "0";

                        Latitude = (intent.getStringExtra(EasyLocation.LATITUDE));
                        Longitude = (intent.getStringExtra(EasyLocation.LONGITUDE));
                        addresses = (EasyLocation.getAddress(getActivity(),
                                Double.parseDouble(Latitude) ,
                                Double.parseDouble(Longitude)));
                        if(addresses != null && addresses.size() > 0 ){
                            Address address = addresses.get(0);

                            addressText = String.format("%s, %s, %s",
                                    address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                                    address.getLocality(),
                                    address.getCountryName());
                            locationText.setText(addressText.toString());
                            EasyLocation.stopGPS();
                        }else{
                            locationText.setText(Latitude +","+Longitude);
                            EasyLocation.stopGPS();
                        }

//EasyLocation.stopGPS();
                    }
                };
            }
        }.start();
    }
**/

/**
    private class ReverseGeocodingTask extends AsyncTask<Void, Void, String>{
        Context mContext;

        public ReverseGeocodingTask(Context context){
            super();
            mContext = context;
        }

        // Finding address using reverse geocoding
        @Override
        protected String doInBackground(Void... params) {
            addresses = (EasyLocation.getAddress(getActivity(),
                    Double.parseDouble(Latitude) ,
                    Double.parseDouble(Longitude)));
            if(addresses != null && addresses.size() > 0 ){
                Address address = addresses.get(0);

                addressText = String.format("%s, %s, %s",
                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                        address.getLocality(),
                        address.getCountryName());
               // locationText.setText(addressText.toString());
                //EasyLocation.stopGPS();
            }else{
               addressText = (Latitude +","+Longitude);
               // EasyLocation.stopGPS();
            }

            return addressText;
        }

        @Override
        protected void onPostExecute(String addressText) {
            Toast.makeText(getActivity(),addressText,Toast.LENGTH_LONG).show();
         locationText.setText(addressText);

        }
    }
**/

    @Override
    public void onPause() {
        super.onPause();
       // getActivity().unregisterReceiver(lftBroadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
      // LoadValues();

    }
    private class ReverseGeocodingTask extends AsyncTask<Double, Void, String>{
        Context mContext;
        final GlobalClass globalVariable= (GlobalClass)getActivity().getApplicationContext();

        public ReverseGeocodingTask(Context context){
            super();
            mContext = context;
        }

        @Override
        protected String doInBackground(Double... params) {
            Geocoder geocoder = new Geocoder(mContext);
            double latitude = Double.parseDouble(globalVariable.getMaLatitude());
            double longitude = Double.parseDouble(globalVariable.getMaLongitude());

            List<Address> addresses = null;
            String addressText="";

            try {
                addresses = geocoder.getFromLocation(latitude, longitude,1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(addresses != null && addresses.size() > 0 ){
                Address address = addresses.get(0);

                addressText = String.format("%s, %s, %s",
                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                        address.getLocality(),
                        address.getCountryName());

            }

            return addressText;
        }

        @Override
        protected void onPostExecute(String addressText) {
            // Setting address of the touched Position
            locationText.setText(addressText);
        }
    }

/**

    private final BroadcastReceiver lftBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // extract the location info in the broadcast
            final LocationInfo locationInfo = (LocationInfo) intent.getSerializableExtra(LocationLibraryConstants.LOCATION_BROADCAST_EXTRA_LOCATIONINFO);
            // refresh the display with it
            refreshDisplay(locationInfo);
        }
    };

    private void refreshDisplay() {
        refreshDisplay(new LocationInfo(getActivity()));
    }
    private void refreshDisplay(final LocationInfo locationInfo) {

        if (locationInfo.anyLocationDataReceived()) {

           // latText.setText(Float.toString(locationInfo.lastLat));
           // longText.setText(Float.toString(locationInfo.lastLong));
            if (locationInfo.hasLatestDataBeenBroadcast()) {
                Toast.makeText(getActivity(),"Latest Location has been broadcast",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getActivity(),"Awaiting Location broadcast",Toast.LENGTH_SHORT).show();
            }


        }

    }

**/


}
