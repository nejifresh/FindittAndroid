package com.findittlive.finditt.slidingmodel;

/**
 * Created by Neji on 10/5/2014.
 */
public class NavDrawerItem {
    private String title;
    private int icon;
    private String count = "0";
    private String header="";
    // boolean to set visiblity of the counter
    private boolean isCounterVisible = false;

    public NavDrawerItem(){}

    public NavDrawerItem(String title, int icon){
        this.title = title;
        this.icon = icon;

    }
    public NavDrawerItem(String Header){
        this.header = Header;

    }

    public NavDrawerItem(String title, int icon, boolean isCounterVisible, String count){
        this.title = title;
        this.icon = icon;
        this.isCounterVisible = isCounterVisible;
        this.count = count;
    }

    //THIS IS TO SHOW THE HEADER
    public String getHeader(){
        return this.header;
    }
    public void setHeader(String Header){
        this.header = Header;
    }

    public String getTitle(){
        return this.title;
    }

    public int getIcon(){
        return this.icon;
    }

    public String getCount(){
        return this.count;
    }

    public boolean getCounterVisibility(){
        return this.isCounterVisible;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setIcon(int icon){
        this.icon = icon;
    }

    public void setCount(String count){
        this.count = count;
    }

    public void setCounterVisibility(boolean isCounterVisible){
        this.isCounterVisible = isCounterVisible;
    }
}
