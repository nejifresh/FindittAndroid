package com.findittlive.finditt.Search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.findittlive.finditt.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Neji on 10/13/2014.
 */
public class ItemAdapter extends BaseAdapter {
    final Context context;
    final List<Items> itemsList;

    public ItemAdapter(Context context,List<Items> items){
        this.context = context;
        this.itemsList = items;
    }
public Items giveItemPosition(int position){
    return this.itemsList.get(position);
}
    @Override
    public int getCount() {
        return this.itemsList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.itemsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_items, parent, false);
        }
        TextView bizname = (TextView) convertView.findViewById(R.id.bizname);
        TextView Address = (TextView) convertView.findViewById(R.id.address);
       TextView distance = (TextView) convertView.findViewById(R.id.distance);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.list_image);
       RatingBar ratingBar = (RatingBar)convertView.findViewById(R.id.ratebar);
        //TextView bizlocation = (TextView)convertView.findViewById(R.id.tvLoc);
        Items t = this.itemsList.get(position);

if(!t.equals(null)){
    bizname.setText(t.getBusinessName());
    Address.setText(t.getAddress());

    imageView.setImageBitmap(t.getImage());


    ratingBar.setRating(t.getRating());
    distance.setText(Float.toString(roundTwoDecimals(t.getDistance() / 1000)).toString() + " km away");

}





         // bizlocation.setText(t.getBizlocation());
         //format the output
         //DecimalFormat df = new DecimalFormat("###.#");
         //String forme = df.format(t.getDistance());
         // distance.setText(forme.toString());
         // signing.setText(position);

         notifyDataSetChanged();

         return convertView;


    }

    float roundTwoDecimals(float d)
    {
        DecimalFormat twoDForm = new DecimalFormat("####.#");
        return Float.valueOf(twoDForm.format(d));

    }
}
