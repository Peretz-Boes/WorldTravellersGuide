package com.example.android.worldtravellersguide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by Peretz on 2017-04-20.
 */

public class PlaceAdapter extends ArrayAdapter<Place> {

    public PlaceAdapter(Context context,List<Place> places){
        super(context,0,places);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        Place place=getItem(position);
        if (convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.place_item,parent,false);
        }
        return convertView;
    }

}
