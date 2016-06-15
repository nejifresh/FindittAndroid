package com.findittlive.finditt.News;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.text.format.DateUtils;
import android.util.Log;

import com.cloudant.sync.datastore.Attachment;
import com.cloudant.sync.datastore.BasicDocumentRevision;
import com.findittlive.finditt.Search.GlobalClass;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by Neji on 11/8/2014.
 */
public class News implements Comparable<News>  {
    private String headline;
    private String body;
    private  long timepost;
    private long timenow;
    private Bitmap bitmap;
    private String timedifference;
    private String newsSource;

    public News(){
        super();
    }

    public String getNewsSource() {
        return newsSource;
    }

    public void setNewsSource(String newsSource) {
        this.newsSource = newsSource;
    }

    public String getHeadline(){
        return this.headline;
    }
    public void setHeadline(String headline){
        this.headline = headline;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getTimepost() {
        return timepost;
    }

    public void setTimepost(long timepost) {
        this.timepost = timepost;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getTimedifference() {
        return timedifference;
    }

    public void setTimedifference(String timedifference) {
        this.timedifference = timedifference;
    }

    public static News giveNewsDetails(BasicDocumentRevision revision, Context context){
        News t = new News();
        BasicDocumentRevision docs = revision;
        Map<String, Object> map = docs.asMap();
        //now get the attachments
        Bitmap bMap = null;

        //this gets the name of attachment which was formed using the headline name in the feeder tool side
        String headyname = ((String) map.get("News Headline"));
        String attache = headyname.toString().replace(" ","_")+ "_image1.jpg";
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





        // if(map.containsKey("type") && map.get("type").equals(Task.DOC_TYPE)) {
        t.setHeadline((String) map.get("News Headline"));
        t.setBody((String) map.get("News Body"));
        t.setNewsSource((String) map.get("News Source"));
        t.setTimepost((Integer)map.get("Current Time"));
        t.setBitmap(bMap);

        long tsLong = System.currentTimeMillis()/1000;
        Calendar calendar = Calendar.getInstance();
        java.util.Date now = calendar.getTime();
        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());

       // String ts = tsLong.toString();
        CharSequence timeresult = DateUtils.getRelativeTimeSpanString(t.getTimepost(),  tsLong, DateUtils.SECOND_IN_MILLIS);
        Log.d("Time",timeresult.toString());
        t.setTimedifference(timeresult.toString());

        return t;

    }
    public static long toLocal(long timestamp) {
        Calendar cal = Calendar.getInstance();
        int offset = cal.getTimeZone().getOffset(timestamp);
        return timestamp - offset;
    }

    public int compareTo(News compareTiming) {

        long compareDistance = ((News)compareTiming).getTimepost();

        //ascending order
        return (int)(compareDistance - this.timepost  );

        //descending order
        //return compareQuantity - this.quantity;

    }
}
