package com.example.group_project.foodpantry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoAdapterMaps implements GoogleMap.InfoWindowAdapter {
    private final View mInfoWindowView;
    private Context mContext;

    public CustomInfoAdapterMaps(Context c){
        this.mInfoWindowView = LayoutInflater.from(c).inflate(R.layout.custome_info_window, null);
        this.mContext = c;
    }

    public void render(Marker marker, View view){
        String pantryId = marker.getTitle();

        //
        if(pantryId != null){
            String[] info = pantryInfo(pantryId);
            if (info.length == 4){
                TextView pantryTitle = (TextView) view.findViewById(R.id.pantryTitle);
                if(info[0] != "")  pantryTitle.setText(info[0]);
                else pantryTitle.setText("");

                TextView pantryAddress = (TextView) view.findViewById(R.id.pantryAddress);
                if(info[1] != "")   pantryAddress.setText(info[1]);
                else pantryAddress.setText("");

                TextView pantryEmail = (TextView) view.findViewById(R.id.pantryEmail);
                if(info[2] != "")   pantryEmail.setText(info[2]);
                else pantryEmail.setText("");

                TextView pantryVolunteers = (TextView) view.findViewById(R.id.pantryVolunteers);
                if(info[3] != "")    pantryVolunteers.setText(info[3]);
                else pantryVolunteers.setText("");
            }

        }
        //return
    }

    private String[] pantryInfo(String name){
        //connect to database and get Pantry info
        //dummy data
        String[] info = {"pantryName", "7999 Regents Dr, College Park, MD", "pantry1@pantries.com", "Yes"};

        return info;
    }

    @Override
    public View getInfoWindow(Marker marker) {

        render(marker, this.mInfoWindowView);
        return mInfoWindowView;
       // return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        render(marker, this.mInfoWindowView);
        return mInfoWindowView;
        //return null;
    }
}
