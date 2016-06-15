package com.findittlive.finditt.Search;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
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
import com.findittlive.finditt.slidermenu.MyActivity;
import com.google.common.eventbus.Subscribe;
import com.parse.Parse;
import com.parse.PushService;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;


/**
 * Created by Neji on 10/26/2014.
 */
public class GlobalClass extends Application {
    private String bizname;
    private String phone;
    private String description;
    private String type;
    private String businessname;
    private String address;
    private Bitmap bitmap;
    private double latitude;
    private double longitude;

    private String maLatitude;
    private String maLongitude;

   private String currLatitude;
    private String currLongitude;

    private static Context mContext;
    private  float distance;
    private Bitmap bitmap2;
    private Bitmap bitmap3;
    private Bitmap bitmap4;
    private Bitmap bitmap5;
    private Bitmap bitmap6;
    private String searchItem;
private String bizlocation;
private String thequery;
    private static final String DATASTORE_MANGER_DIR = "fData";
    private static final String DATASTORE_NAME = "finditt";
    private static final String DATASTORE_MANGER_DIR2 = "findittData";
    private static final String DATASTORE_NAME2 = "findittnews";

    private Replicator mPushReplicator;
    private Replicator mPullReplicator;


   Handler mHandler;
    //News Items
    private String headline;
    private String body;
    private String source;
    private String articledate;
    private Bitmap imagenews;
    private Float radius;

    public void onCreate() {
        Parse.initialize(this, "3DCe29CX1oUSFnaGP09bFB0KM0sYNOR51G3ZYIZK", "VHvjkHJyuRFZ5qwNpIwrGbfIGuFxu1LrXaFoFmvq");
        // Also in this method, specify a default Activity to handle push notifications
        PushService.setDefaultPushCallback(this, MyActivity.class);
    }


    public Datastore OpenDataStore(Context context){
        this.mContext = context;
        File path = mContext.getApplicationContext().getDir(DATASTORE_MANGER_DIR, MODE_PRIVATE);
        DatastoreManager manager= new DatastoreManager(path.getAbsolutePath());
        Datastore datastore;
        datastore =  manager.openDatastore(DATASTORE_NAME);
        Log.d("SET", "Set up database at " + path.getAbsolutePath());
        return datastore;
    }

    public Datastore OpenNewsStore(Context context){
        this.mContext = context;
        File path = mContext.getApplicationContext().getDir(DATASTORE_MANGER_DIR2, MODE_PRIVATE);
        DatastoreManager manager= new DatastoreManager(path.getAbsolutePath());
        Datastore datastore;

        datastore =  manager.openDatastore(DATASTORE_NAME2);
        Log.d("SET", "Set up database at " + path.getAbsolutePath());
        return datastore;
    }

    public Float getRadius() {
        return radius;
    }

    public void setRadius(Float radius) {
        this.radius = radius;
    }

    public String getCurrLatitude() {
        return currLatitude;
    }

    public void setCurrLatitude(String currLatitude) {
        this.currLatitude = currLatitude;
    }

    public String getCurrLongitude() {
        return currLongitude;
    }

    public void setCurrLongitude(String currLongitude) {
        this.currLongitude = currLongitude;
    }

    public String getBizlocation() {
        return bizlocation;
    }

    public void setBizlocation(String bizlocation) {
        this.bizlocation = bizlocation;
    }

    public String getName() {

        return bizname;
    }

    public void setName(String aBizName) {

        bizname = aBizName;

    }

    public String getPhone() {

        return phone;
    }

    public void setPhone(String aPhone) {

        phone = aPhone;
    }

    public Bitmap getImage(){
        return this.bitmap;
    }
    public void setImage(Bitmap image){
        this.bitmap = image;
    }
    public String getMaLatitude(){
        return (this.maLatitude);
    }
    public void setMaLatitude(String maLatitude){
        this.maLatitude = maLatitude;
    }
    public String getAddress(){
        return this.address;
    }
    public void setAddress(String address){
        this.address = address;
    }
    public String getMaLongitude(){
        return (this.maLongitude);
    }
    public void setMaLongitude(String maLongitude){
        this.maLongitude = maLongitude;
    }
    public Bitmap getImage3(){
        return this.bitmap3;
    }
    public void setImage3(Bitmap image){
        this.bitmap3 = image;
    }

    public Bitmap getImage2(){
        return this.bitmap2;
    }
    public void setImage2(Bitmap image){
        this.bitmap2 = image;
    }

    public Bitmap getImage4(){
        return this.bitmap4;
    }
    public void setImage4(Bitmap image){
        this.bitmap4 = image;
    }

    public Bitmap getImage5(){
        return this.bitmap5;
    }
    public void setImage5(Bitmap image){
        this.bitmap5 = image;
    }

    public Bitmap getImage6(){
        return this.bitmap6;
    }
    public void setImage6(Bitmap image){
        this.bitmap6 = image;
    }
    public String getType(){
        return this.type;
    }
    public void setType(String signType){
        this.type = signType;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String desc) {
        this.description = desc;
    }
    public String getSearchItem(){
        return this.searchItem;
    }
    public void setSearchItem(String searchItem){
        this.searchItem = searchItem;
    }
    public String getQuery(){
        return this.thequery;
    }
    public void setQuery(String thequery){
        this.thequery = thequery;
    }

    public String getHeadline(){
        return this.headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getArticledate() {
        return articledate;
    }

    public void setArticledate(String articledate) {
        this.articledate = articledate;
    }

    public Bitmap getImagenews() {
        return imagenews;
    }

    public void setImagenews(Bitmap imagenews) {
        this.imagenews = imagenews;
    }
}
