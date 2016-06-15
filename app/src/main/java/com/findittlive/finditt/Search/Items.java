package com.findittlive.finditt.Search;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Log;

import com.cloudant.sync.datastore.Attachment;
import com.cloudant.sync.datastore.BasicDocumentRevision;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Map;
import com.findittlive.finditt.Search.GlobalClass;
/**
 * Created by Neji on 10/11/2014.
 */
public class Items implements Comparable<Items> {
    private String description;
    private String type;
    private String businessname;
    private String address;
    private Bitmap bitmap;
    private double latitude;
    private double longitude;
private String bizlocation;
    private String maLatitude;
    private String maLongitude;

    private static Context mContext;
    private  float distance;
    private static float mydistance=0;
    private String phone;
    private Bitmap bitmap2;
    private Bitmap bitmap3;
    private Bitmap bitmap4;
    private Bitmap bitmap5;
    private Bitmap bitmap6;
    public static final String MyPREFERENCES = "GeoPrefs";
public float rating;

    public Items(){
        super();
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String desc) {
        this.description = desc;
    }
    public String getBusinessName() {
        return this.businessname;
    }
    public void setBusinessname(String businessname) {
        this.businessname = businessname;
    }
    public String getBizlocation() {
        return bizlocation;
    }
    public void setBizlocation(String bizlocation) {
        this.bizlocation = bizlocation;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getAddress(){
        return this.address;
    }
    public void setAddress(String address){
        this.address = address;
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

    public String getMaLongitude(){
        return (this.maLongitude);
    }
    public void setMaLongitude(String maLongitude){
        this.maLongitude = maLongitude;
    }

     public Double getLatitude(){
        return this.latitude;
    }
    public void setLatitude(Double latitude){
        this.latitude = latitude;
    }
    public Double getLongitude(){
        return this.longitude;
    }
    public void setLongitude(float longitude){
        this.longitude = longitude;
    }
    public Location getLocation(Double latitude,Double longitude){
        Location itemLocation = new Location("ItemLocation");
        itemLocation.setLatitude(latitude);
        itemLocation.setLongitude(longitude);

        return itemLocation;
    }
    public float getDistance(){

        return  this.distance;
    }
    public void setDistance(float distance){
     this.distance = distance;
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

    public String getPhone(){
return this.phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public String getType(){
        return this.type;
    }
    public void setType(String signType){
        this.type = signType;
    }

    public int compareTo(Items compareItems) {

        float compareDistance = ((Items) compareItems).getDistance();

        //ascending order
        return (int)(this.distance - compareDistance);

        //descending order
        //return compareQuantity - this.quantity;

    }

    /**
    @Override
    public String toString() {
        return "" + getDescription() + "";
    }
    **/

public static Items giveDetails(BasicDocumentRevision revision, Context context){
Items t = new Items();
    BasicDocumentRevision docs = revision;
    Map<String, Object> map = docs.asMap();
    //now get the attachments
    Bitmap bMap = null;
    Bitmap bMap2 = null;
    Bitmap bMap3 = null;
    Bitmap bMap4 = null;
    Bitmap bMap5 = null;
    //this gets the name of attachment which was formed using the business name in the capture tool side
    String bizzyname = ((String) map.get("Business Name"));
    String attache = bizzyname.toString().replace(" ","_")+ "_image1.jpg";
    Attachment attachment =  docs.getAttachments().get(attache);

    try{
        InputStream is = attachment.getInputStream();
        BufferedInputStream bf = new BufferedInputStream(is);
        bMap = BitmapFactory.decodeStream(bf);
        Log.d("Got", "got the attachment and input stream");
        is.close();

    }catch (Exception e){
        e.printStackTrace();
        Log.e("Got", "failed to get the attachment and input stream");

    }

    //Get image 2 if available
    String bizzyname2 = ((String) map.get("Business Name"));
    String attache2 = bizzyname2.toString().replace(" ","_")+ "_image2.jpg";
    Attachment attachment2 =  docs.getAttachments().get(attache2);
    try{
        InputStream is = attachment2.getInputStream();
        BufferedInputStream bf = new BufferedInputStream(is);
        bMap2 = BitmapFactory.decodeStream(bf);
        Log.d("Got", "got the second attachment and input stream");
        is.close();

    }catch (Exception e){
        e.printStackTrace();
        Log.e("Got", "failed to get the 2nd attachment and input stream");

    }
    //get third attachment
    //Get image 3 if available
    String bizzyname3 = ((String) map.get("Business Name"));
    String attache3= bizzyname3.toString().replace(" ","_")+ "_image3.jpg";
    Attachment attachment3 =  docs.getAttachments().get(attache3);
    try{
        InputStream is3 = attachment3.getInputStream();
        BufferedInputStream bf3 = new BufferedInputStream(is3);
        bMap3 = BitmapFactory.decodeStream(bf3);
        Log.d("Got", "got the 3rd attachment and input stream");
        is3.close();

    }catch (Exception e){
        e.printStackTrace();
        Log.e("Got", "failed to get the attachment and input stream");

    }
    //Get image 4 if available
    String bizzyname4 = ((String) map.get("Business Name"));
    String attache4 = bizzyname4.toString().replace(" ","_")+ "_image4.jpg";
    Attachment attachment4 =  docs.getAttachments().get(attache4);
    try{
        InputStream is = attachment4.getInputStream();
        BufferedInputStream bf = new BufferedInputStream(is);
        bMap4 = BitmapFactory.decodeStream(bf);
        Log.d("Got", "got the 4th attachment and input stream");
        is.close();

    }catch (Exception e){
        e.printStackTrace();
        Log.e("Got", "failed to get the attachment and input stream");

    }

    //Get image 5 if available
    String bizzyname5 = ((String) map.get("Business Name"));
    String attache5 = bizzyname5.toString().replace(" ","_")+ "_image5.jpg";
    Attachment attachment5 =  docs.getAttachments().get(attache5);
    try{
        InputStream is = attachment5.getInputStream();
        BufferedInputStream bf = new BufferedInputStream(is);
        bMap5 = BitmapFactory.decodeStream(bf);
        Log.d("Got", "got the fifth attachment and input stream");
        is.close();

    }catch (Exception e){
        e.printStackTrace();
        Log.e("Got", "failed to get the attachment and input stream");

    }

    final GlobalClass globalVariable= (GlobalClass)context;


    try {
    // Use this guy to calculate distance from current location to present one
   // GPSTracker myLocation = new GPSTracker(mContext);
    //SharedPreferences sharedpreferences;
    Location locationA = new Location("point A");
    locationA.setLatitude(Double.parseDouble(globalVariable.getMaLatitude()));  //(4.963897);
    locationA.setLongitude(Double.parseDouble(globalVariable.getMaLongitude())); //(8.341769);
    //Location firstLocation = myLocation.getLocation();
    Location secondLocation = t.getLocation(Double.parseDouble(((String) map.get("Latitude"))),
            Double.parseDouble(((String) map.get("Longitude"))));
    mydistance = locationA.distanceTo(secondLocation);

    DecimalFormat df = new DecimalFormat("###.#");
    Log.d("Dist","Got distance - " + df.format(mydistance/1000));

}
catch (Exception e){
    Log.e("Dist","could not get distance");
}

    if(mydistance/1000 > globalVariable.getRadius()){
        return t = null;
    } else {

        DecimalFormat df = new DecimalFormat("###.#");
        // if(map.containsKey("type") && map.get("type").equals(Task.DOC_TYPE)) {
        t.setDescription((String) map.get("Description"));
        t.setBusinessname((String) map.get("Business Name"));
        t.setAddress((String) map.get("Address"));
        t.setImage(bMap);
        t.setDistance(mydistance);
        t.setType((String) map.get("SigningType"));
        t.setPhone((String) map.get("Phone"));
        t.setMaLatitude((String) map.get("Latitude"));
        t.setMaLongitude((String) map.get("Longitude"));
        t.setRating(Float.parseFloat((String) map.get("Rating")));
        t.setBizlocation((String) map.get("Location"));

        try {
            t.setImage2(bMap2);
            t.setImage3(bMap3);
            t.setImage4(bMap4);
            t.setImage5(bMap5);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return t;
    }

}
}
