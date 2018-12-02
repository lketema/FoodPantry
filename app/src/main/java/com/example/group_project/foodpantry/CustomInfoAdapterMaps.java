package com.example.group_project.foodpantry;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CustomInfoAdapterMaps implements GoogleMap.InfoWindowAdapter {
    private final View mInfoWindowView;
    private Context mContext;

    private String TAG = "Custom Info Adapter";
    public CustomInfoAdapterMaps(Context c){
        this.mInfoWindowView = LayoutInflater.from(c).inflate(R.layout.custome_info_window, null);
        this.mContext = c;
    }

    public void render(Marker marker, View view){
        String pantryId = marker.getTitle();
        TextView pantryTitle = (TextView) view.findViewById(R.id.pantryTitle);
        TextView pantryAddress = (TextView) view.findViewById(R.id.pantryAddress);
        TextView pantryEmail = (TextView) view.findViewById(R.id.pantryEmail);
        TextView pantryVolunteers = (TextView) view.findViewById(R.id.pantryVolunteers);
                //
        if(pantryId != null){
            String[] info = pantryInfo(pantryId);
            if (info.length == 4){

                if(info[0] != "")  pantryTitle.setText(info[0]);
                else pantryTitle.setText("");


                if(info[1] != "")   pantryAddress.setText(info[1]);
                else pantryAddress.setText("");


                if(info[2] != "")   pantryEmail.setText(info[2]);
                else pantryEmail.setText("");


                if(info[3] != "")    pantryVolunteers.setText(info[3]);
                else pantryVolunteers.setText("");
            }

        }
        //return
    }

    private String[] pantryInfo(final String pantryId){
        //connect to database and get Pantry info
        //dummy data

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("registration").addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        if(postSnapshot.getKey().equals(pantryId)){
                            if(postSnapshot.hasChild("daysOpen")){
                                Pantry temp = postSnapshot.getValue(Pantry.class);
                                if(temp != null) {
                                    displayPantry(temp);
                                }
                            }
                            else{
                                Log.i(TAG, "I am a");
                                Event temp = postSnapshot.getValue(Event.class);
                                if(temp != null) {
                                    displayEvent(temp);
                                }
                            }
                        }
                    }
              }

              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {
                  Log.i(TAG, "Database error: " + databaseError.getMessage());
              }
        });
        String[] info = {"pantryName", "7999 Regents Dr, College Park, MD", "pantry1@pantries.com", "Yes"};

        return info;
    }

    private void displayPantry(Pantry pantry){

    }

    private void displayEvent(Event event){

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
