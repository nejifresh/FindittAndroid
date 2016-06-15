package com.findittlive.finditt.Search;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.findittlive.finditt.R;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

/**
 * Created by Neji on 10/26/2014.
 */
public class Tab2Activity extends Activity {
    private SliderLayout mDemoSlider;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tab2);
        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);


        HashMap<String, String> bitmap_maps = new HashMap<String, String>();
        String[] fileurl = {getImageUri(getApplicationContext(), globalVariable.getImage()),
                getImageUri(getApplicationContext(), globalVariable.getImage2()),
                getImageUri(getApplicationContext(), globalVariable.getImage4()),
                getImageUri(getApplicationContext(), globalVariable.getImage5())
        };
        try {
            bitmap_maps.put("Image2", getImageUri(getApplicationContext(), globalVariable.getImage2()));
            Log.d("HOLA", "Got image 2");
            bitmap_maps.put("Image3", getImageUri(getApplicationContext(), globalVariable.getImage3()));
            Log.d("HOLA", "Got image 3");
            bitmap_maps.put("Image4", getImageUri(getApplicationContext(), globalVariable.getImage4()));
            Log.d("HOLA", "Got image 4");
            bitmap_maps.put("Image5", getImageUri(getApplicationContext(), globalVariable.getImage5()));
            Log.d("HOLA", "Got image 5");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("HOLA", "Could not get the images");
        }

        int size = fileurl.length;
        for (String i:bitmap_maps.keySet()) {
            DefaultSliderView imSlider = new DefaultSliderView(this);
            imSlider
                    .image(bitmap_maps.get(i))
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            mDemoSlider.addSlider(imSlider);
                 }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);

    }

    public String getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return path;//Uri.parse(path);
    }
}
