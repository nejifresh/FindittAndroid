package com.findittlive.finditt.maps;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.findittlive.finditt.R;
import com.findittlive.finditt.slidermenu.HomeFragment;
import com.findittlive.finditt.slidermenu.MyActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;

/**
 * Created by Neji on 10/21/2014.
 */
public class Map_Activity extends FragmentActivity implements OnMapLongClickListener,OnMapClickListener,OnMarkerDragListener {
    private static GoogleMap map;
    Double mylatitude;
    Double mylongitude;
    TextView latText;
    TextView longText;
    Button btnDone;
    Button btnCancel;
    public static final String MyPREFERENCES = "GeoPrefs";
    SharedPreferences sharedpreferences;
    public static final String latkey = "latkey";
   public static final String longkey = "longkey";
    @Override
    protected void onCreate(Bundle saveInstance) {
        super.onCreate(saveInstance);
        setContentView(R.layout.map_activity);
        latText = (TextView)findViewById(R.id.tvLat);
        longText = (TextView)findViewById(R.id.tvLong);
        btnDone = (Button)findViewById(R.id.btnDone);
        btnCancel = (Button)findViewById(R.id.btnCancel);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Map_Activity.this,MyActivity.class);
                startActivity(i);
                finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Map_Activity.this, MyActivity.class);
                startActivity(i);
                finish();
            }
        });

        map = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setOnMarkerDragListener(this);
        map.setOnMapLongClickListener(this);
        map.setOnMapClickListener(this);

        setUpMapIfNeeded();
        Intent intent = getIntent();
        mylatitude = Double.parseDouble(intent.getStringExtra("latitude"));
        mylongitude =Double.parseDouble(intent.getStringExtra("longitude"));

        if (map != null) {
            map.setMyLocationEnabled(true);
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            LatLng LOC = new LatLng(mylatitude,mylongitude);
            Marker marker = map.addMarker(new MarkerOptions()
                            .position(LOC)
                            .title("Your Current Location")
                            .snippet("According to GPS")
                    .draggable(true)

            );
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(LOC)
                    .zoom(15.5F)
                    .bearing(200F) // orientation
                    .tilt(50F) // viewing angle

                    .build();
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            latText.setText(  Double.toString(roundTwoDecimals(mylatitude)));
            longText.setText(Double.toString(roundTwoDecimals(mylongitude)));
            putGeo();
        }




/**

        CameraPosition INIT =
                new CameraPosition.Builder()
                        .target(new LatLng(19.0222, 72.8666))
                        .zoom(17.5F)
                        .bearing(300F) // orientation
                        .tilt( 50F) // viewing angle
                        .build();

        map.animateCamera(CameraUpdateFactory.newCameraPosition(INIT));

        // use map to move camera into position
        map.moveCamera( CameraUpdateFactory.newCameraPosition(INIT) );

        //create initial marker
        map.addMarker( new MarkerOptions()
                .position( new LatLng(19.0216, 72.8646) )
                .title("Location")
                .snippet("First Marker")).showInfoWindow();
 **/
    }

    private void putGeo(){
        String olat = latText.getText().toString();
        String olong = longText.getText().toString();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        //editor.clear();
        editor.putString("latitude",latText.getText().toString());
        editor.putString("longitude", longText.getText().toString());
        editor.commit();
       // Toast.makeText(getApplicationContext(),latkey,Toast.LENGTH_SHORT).show();
    }

    double roundTwoDecimals(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.####");
        return Double.valueOf(twoDForm.format(d));
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null) {
            // Try to obtain the map from the SupportMapFragment.
            map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (map != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }




    @Override
    public void onMarkerDrag(Marker arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMarkerDragEnd(Marker arg0) {
        // TODO Auto-generated method stub
        LatLng dragPosition = arg0.getPosition();
        double dragLat = dragPosition.latitude;
        double dragLong = dragPosition.longitude;
        Log.i("info", "on drag end :" + dragLat + " dragLong :" + dragLong);
        latText.setText(  Double.toString(roundTwoDecimals(dragLat)));
        longText.setText(Double.toString(roundTwoDecimals(dragLong)));
        putGeo();
        //Toast.makeText(getApplicationContext(), "Marker Dragged.to point: "+ dragPosition.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMarkerDragStart(Marker arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMapClick(LatLng arg0) {
        // TODO Auto-generated method stub
        map.animateCamera(CameraUpdateFactory.newLatLng(arg0));
    }


    @Override
    public void onMapLongClick(LatLng arg0) {
        // TODO Auto-generated method stub

        //create new marker when user long clicks
        map.clear();
        map.addMarker(new MarkerOptions()
                .position(arg0)
                .draggable(true));


        double dragLat = arg0.latitude;
        double dragLong = arg0.longitude;
        Log.i("info", "on drag end :" + dragLat + " dragLong :" + dragLong);
        latText.setText(  Double.toString(roundTwoDecimals(dragLat)));
        longText.setText(Double.toString(roundTwoDecimals(dragLong)));
        putGeo();
       // Toast.makeText(getApplicationContext(), "Marker moved.to point: "+ arg0.toString(), Toast.LENGTH_LONG).show();

    }
}
