package com.findittlive.finditt.slidingadapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.findittlive.finditt.R;
import com.findittlive.finditt.slidingmodel.NavDrawerItem;

import java.util.ArrayList;

/**
 * Created by Neji on 10/5/2014.
 */
public class NavDrawerListAdapter extends BaseAdapter {
    LinearLayout headerLayout;
    RelativeLayout itemLayout;
    private Context context;
    private ArrayList<NavDrawerItem> navDrawerItems;
    public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount(){
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position){
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent ){
if(convertView==null){
    LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    convertView = mInflater.inflate(R.layout.drawer_list_item,null);
 }
        ImageView imgIcon = (ImageView)convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        TextView txtCount = (TextView) convertView.findViewById(R.id.counter);
TextView headerText = (TextView)convertView.findViewById(R.id.drawerTitle);
       headerLayout= (LinearLayout) convertView
                .findViewById(R.id.headerLayout);
        itemLayout =(RelativeLayout)convertView.findViewById(R.id.itemLayout);

        if (navDrawerItems.get(position).getHeader()!=null && navDrawerItems.get(position).getTitle()==null){
            headerLayout.setVisibility(LinearLayout.VISIBLE);
            itemLayout.setVisibility(RelativeLayout.INVISIBLE);
            headerText.setText(navDrawerItems.get(position).getHeader());
        } else {
            headerLayout.setVisibility(LinearLayout.INVISIBLE);
            itemLayout.setVisibility(RelativeLayout.VISIBLE);
            //do the normal stuff
            imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
            txtTitle.setText(navDrawerItems.get(position).getTitle());
//set the drawer header

            // displaying count
            // check whether it set visible or not
            if(navDrawerItems.get(position).getCounterVisibility()){
                txtCount.setText(navDrawerItems.get(position).getCount());
            }else{
                // hide the counter view
                txtCount.setVisibility(View.GONE);
            }
        }


        return convertView;

    }
}
