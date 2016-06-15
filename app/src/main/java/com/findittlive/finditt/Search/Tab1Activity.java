package com.findittlive.finditt.Search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.findittlive.finditt.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.io.ByteArrayOutputStream;

/**
 * Created by Neji on 10/26/2014.
 */
public class Tab1Activity extends FragmentActivity {
    private SliderLayout mDemoSlider;
    public String longitude;
    public String BizName;
    public String Address;
    public String Description;
    public String Phone;
    public Bitmap bmap;
    private TextView tv;
    ImageView img;
    private TextView tvDesc;
    private TextView tvAddress;
    private TextView tvLoc;
    private static GoogleMap map;
    double mylatitude;
    double mylongitude;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab1);
        final GlobalClass globalVariable= (GlobalClass) getApplicationContext();
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);
        tv = (TextView)findViewById(R.id.titletext);
        tvAddress = (TextView)findViewById(R.id.tvAddy);
        tvDesc = (TextView)findViewById(R.id.tvDesc);
tvLoc = (TextView)findViewById(R.id.tvLoc);
        final String Name = globalVariable.getName();
      img = (ImageView)findViewById(R.id.imageLoad);
        img.setImageBitmap(globalVariable.getImage());
        Phone = globalVariable.getPhone();



        String tempUri = getImageUri(getApplicationContext(), globalVariable.getImage());
        TextSliderView textSliderView = new TextSliderView(this);
        // initialize a SliderLayout
        textSliderView
                .description(globalVariable.getName().toUpperCase())
                .image(tempUri)
                .setScaleType(BaseSliderView.ScaleType.CenterCrop);
        mDemoSlider.addSlider(textSliderView);

    mDemoSlider.setPresetTransformer(SliderLayout.Transformer.FlipHorizontal);
    mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
        mDemoSlider.setDuration(4000);
        mDemoSlider.stopAutoCycle();

        tvAddress.setText(toCamelCase(globalVariable.getAddress()));
tv.setText(toCamelCase(Name));
        tvDesc.setText(toSentenceCase(globalVariable.getDescription()));
tvLoc.setText(toCamelCase(globalVariable.getBizlocation()));
        map = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();


        setUpMapIfNeeded();
        Intent intent = getIntent();
        mylatitude = Double.parseDouble(globalVariable.getMaLatitude());
        mylongitude = Double.parseDouble(globalVariable.getMaLongitude());

        if (map != null) {
            map.setMyLocationEnabled(true);
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            LatLng LOC = new LatLng(mylatitude,mylongitude);
            Marker marker = map.addMarker(new MarkerOptions()
                            .position(LOC)
                            .title(globalVariable.getName())
                            .snippet(globalVariable.getAddress())
                            .draggable(false)

            );
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(LOC)
                    .zoom(15.5F)
                    .bearing(200F) // orientation
                    .tilt(50F) // viewing angle

                    .build();
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }

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

    public void callNumber(View view){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + Phone ));
        startActivity(callIntent);
    }

    private void setUpMap() {
        map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    public String getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return path;//Uri.parse(path);
    }

    public static String toSentenceCase(String inputString) {
        String result = "";
        if (inputString.length() == 0) {
            return result;
        }
        char firstChar = inputString.charAt(0);
        char firstCharToUpperCase = Character.toUpperCase(firstChar);
        result = result + firstCharToUpperCase;
        boolean terminalCharacterEncountered = false;
        char[] terminalCharacters = {'.', '?', '!'};
        for (int i = 1; i < inputString.length(); i++) {
            char currentChar = inputString.charAt(i);
            if (terminalCharacterEncountered) {
                if (currentChar == ' ') {
                    result = result + currentChar;
                } else {
                    char currentCharToUpperCase = Character.toUpperCase(currentChar);
                    result = result + currentCharToUpperCase;
                    terminalCharacterEncountered = false;
                }
            } else {
                char currentCharToLowerCase = Character.toLowerCase(currentChar);
                result = result + currentCharToLowerCase;
            }
            for (int j = 0; j < terminalCharacters.length; j++) {
                if (currentChar == terminalCharacters[j]) {
                    terminalCharacterEncountered = true;
                    break;
                }
            }
        }
        return result;
    }

    public static String toCamelCase(String inputString) {
        String result = "";
        if (inputString.length() == 0) {
            return result;
        }
        char firstChar = inputString.charAt(0);
        char firstCharToUpperCase = Character.toUpperCase(firstChar);
        result = result + firstCharToUpperCase;
        for (int i = 1; i < inputString.length(); i++) {
            char currentChar = inputString.charAt(i);
            char previousChar = inputString.charAt(i - 1);
            if (previousChar == ' ') {
                char currentCharToUpperCase = Character.toUpperCase(currentChar);
                result = result + currentCharToUpperCase;
            } else {
                char currentCharToLowerCase = Character.toLowerCase(currentChar);
                result = result + currentCharToLowerCase;
            }
        }
        return result;
    }

    public void sendSMS(View view){
        final GlobalClass globalVariable= (GlobalClass) getApplicationContext();
        String phone = globalVariable.getPhone();
        String bizname = globalVariable.getName();
        String message = "Hello, i found "+bizname + " on Finditt, i'm interested in your products/services";
        try{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone,null,message,null,null);
            Toast.makeText(getApplicationContext(),"SMS Sent Successfully",Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Message sending failed",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void showDirections(View view){
        final GlobalClass globalVariable= (GlobalClass) getApplicationContext();
        Uri uri =Uri.parse("http://maps.google.com/maps?&saddr="+globalVariable.getCurrLatitude()+
                ","+globalVariable.getCurrLongitude()+"&daddr="+mylatitude+","+mylongitude +"" );         //15.653293,78.809369&daddr=15.612456,78.706055");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
