package com.findittlive.finditt.slidermenu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudant.sync.notifications.ReplicationCompleted;
import com.cloudant.sync.notifications.ReplicationErrored;
import com.cloudant.sync.replication.ErrorInfo;
import com.cloudant.sync.replication.PullReplication;
import com.cloudant.sync.replication.Replicator;
import com.cloudant.sync.replication.ReplicatorFactory;
import com.findittlive.finditt.R;
import com.findittlive.finditt.Search.GlobalClass;
import com.google.common.eventbus.Subscribe;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Neji on 11/17/2014.
 */
public class Signup extends Activity {
    TextView username;
    TextView password;
    TextView phone;
    TextView referee;
    TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
email = (TextView)findViewById(R.id.txtemail);
        password = (TextView)findViewById(R.id.txtpass);
        phone = (TextView)findViewById(R.id.txtphone);
        referee = (TextView)findViewById(R.id.txtref);
        username = (TextView)findViewById(R.id.txtusername);

    }

    public void signUpOnClickListener(View view){
        ParseUser user = new ParseUser();

 if (!email.getText().toString().isEmpty()){

    user.setEmail(email.getText().toString());

}else{
   email.setError("Enter your email");
    return;
}

  if (!password.getText().toString().isEmpty()){
user.setPassword(password.getText().toString());
    }else{
            password.setError("Enter a password");
            return;
        }

        if (!phone.getText().toString().isEmpty()){
user.put("phone",phone.getText().toString());
        }else{
            phone.setError("Enter your phone number");
            return;
        }

        if (!username.getText().toString().isEmpty()){
            user.setUsername(username.getText().toString());
        }else{
            username.setError("Enter a Username");
            return;
        }
        if (!referee.getText().toString().isEmpty()){
            user.put("referee", referee.getText().toString());
        }else{

        }
        final ProgressDialog ringProgressDialog =
                ProgressDialog.show(Signup.this, "Please wait ...",	"Sign up in progress..", true);
        ringProgressDialog.setCancelable(false);
       user.signUpInBackground(new SignUpCallback() {
           @Override
           public void done(com.parse.ParseException e) {
               if (e == null) {
                   // Hooray! Let them use the app now.
                   ringProgressDialog.dismiss();
                    showSuccessDialog();
               } else {
                   // Sign up didn't succeed. Look at the ParseException
                   // to figure out what went wrong
                   ringProgressDialog.dismiss();
                   showFailDialog();
                   e.printStackTrace();
               }
           }
       });

    }

    private void showSuccessDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        // Setting Dialog Title
        alertDialog.setTitle("Success..");

        // Setting Dialog Message
        alertDialog.setMessage("Registration Successful..");

        // Setting Icon to Dialog
        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
               launchRingDialog();

            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    private void showFailDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        // Setting Dialog Title
        alertDialog.setTitle("Error..");

        // Setting Dialog Message
        alertDialog.setMessage("Error in registration, invalid user credentials or check data connection");

        // Setting Icon to Dialog
        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                // Toast.makeText(getApplicationContext(),"New Location created", Toast.LENGTH_SHORT).show();
                return;

            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    private URI createServerURI()
            throws URISyntaxException {
        URI uri = new URI("https://finditt.cloudant.com/finditt");
        return uri;
    }

    public void launchRingDialog() {
        final ProgressDialog ringProgressDialog =
                ProgressDialog.show(Signup.this, "Please wait ...",	"Database installation in progress..", true);
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
                        showToast("Unable to connect, please check your data connection or try again");


                    }
                    else{
//Log.d("TAG","Replication completed successfully");
                        showToast("Database installation completed successfully");
Intent i = new Intent(Signup.this,MyActivity.class);
                        startActivity(i);
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

    public void showToast(final String toast)
    {
        runOnUiThread(new Runnable() {
            public void run()
            {
                Toast.makeText(Signup.this, toast, Toast.LENGTH_LONG).show();
                //int nDocs = datastore.getDocumentCount();
                // BasicDocumentRevision rev = datastore.getAllDocuments(0,nDocs,true);
                //Toast.makeText(MyActivity.this, nDocs, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void loginOnClickListener(View view){
Intent i = new Intent(Signup.this,LoginActivity.class);
        startActivity(i);
    }
}
