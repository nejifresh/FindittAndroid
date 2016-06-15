package com.findittlive.finditt.News;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.findittlive.finditt.R;
import com.findittlive.finditt.Search.GlobalClass;

/**
 * Created by Neji on 11/9/2014.
 */
public class NewsContent extends Activity {
    TextView headyview;
    TextView bodyview;
    TextView sourceview;
    ImageView imagenews;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_content);

        final GlobalClass globalVariable= (GlobalClass) getApplicationContext();
        headyview = (TextView)findViewById(R.id.headytv);
        bodyview =(TextView)findViewById(R.id.bodytv);
        sourceview = (TextView)findViewById(R.id.courtesy);
        imagenews = (ImageView)findViewById(R.id.imageheady);

        headyview.setText(globalVariable.getHeadline());
        bodyview.setText(globalVariable.getBody());
        sourceview.setText("Courtesy of.. "+ globalVariable.getSource());
        imagenews.setImageBitmap(globalVariable.getImagenews());
    }
}
