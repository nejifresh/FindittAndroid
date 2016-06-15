package com.findittlive.finditt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.findittlive.finditt.slidermenu.MyActivity;
import com.findittlive.finditt.slidermenu.Signup;
import com.parse.ParseUser;

/**
 * Created by Neji on 11/17/2014.
 */
public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        /****** Create Thread that will sleep for 5 seconds *************/
        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 5 seconds
                    sleep(5*1000);

                    // After 5 seconds redirect to another intent
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    if (currentUser != null) {
                        // do stuff with the user
                        Intent intent = new Intent(SplashActivity.this, MyActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        // show the signup or login screen
                        Intent i = new Intent(SplashActivity.this, Signup.class);
                        startActivity(i);
                        finish();
                    }



                } catch (Exception e) {

                }
            }
        };

        // start thread
        background.start();



    }
}
