package com.findittlive.finditt.News;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.findittlive.finditt.R;
import com.findittlive.finditt.Search.Items;

import java.util.List;

/**
 * Created by Neji on 11/8/2014.
 */
public class NewsAdapter extends BaseAdapter {
    final Context context;
    final List<News> newsList;

    public NewsAdapter(Context context,List<News> news){
        this.context = context;
        this.newsList = news;
    }
    public News giveItemPosition(int position){
        return this.newsList.get(position);
    }
    @Override
    public int getCount() {
        return this.newsList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.newsList.get(position);
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
            convertView = inflater.inflate(R.layout.item_news, parent, false);
        }
        TextView headyname = (TextView) convertView.findViewById(R.id.headyname);
        TextView bodyline = (TextView) convertView.findViewById(R.id.bodyline);
        TextView timediff = (TextView) convertView.findViewById(R.id.timediff);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.newslist_image);

        News t = this.newsList.get(position);
        headyname.setText(t.getHeadline());
        bodyline.setText(t.getBody());
        timediff.setText(t.getTimedifference());
        imageView.setImageBitmap(t.getBitmap());

        notifyDataSetChanged();

        return convertView;

    }
}
