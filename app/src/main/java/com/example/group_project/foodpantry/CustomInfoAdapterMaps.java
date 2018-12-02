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
    private TextView pantryName;
    private TextView pantryAddress;
    private TextView pantryPhone;
    private TextView pantryWebsite;
    private TextView eventDate;
    private TextView pantryTimeOpenClose;
    private TextView clickForMore;
    private boolean foundId = false;

    private String TAG = "Custom Info Adapter";
    public CustomInfoAdapterMaps(Context c){
        this.mInfoWindowView = LayoutInflater.from(c).inflate(R.layout.custome_info_window, null);
        this.mContext = c;
    }

    public void render(Marker marker, View view){
        pantryName = (TextView) view.findViewById(R.id.pantryName);
        pantryAddress = (TextView) view.findViewById(R.id.pantryAddress);
        pantryPhone = (TextView) view.findViewById(R.id.pantryPhone);

        pantryWebsite = (TextView) view.findViewById(R.id.pantryWebsite);
        eventDate = (TextView) view.findViewById(R.id.eventDate);

        pantryTimeOpenClose = (TextView) view.findViewById(R.id.pantryTimeOpenClose);

        clickForMore = (TextView) view.findViewById(R.id.pantryMoreInfo);
        clickForMore.setVisibility(View.GONE);

        String pantryId = marker.getTitle();
       // Log.i(TAG, "Pantry ID: " + pantryId);
        if(pantryId != null){
           pantryInfo(pantryId);
        }

    }

    private void pantryInfo(final String pantryId){
        //connect to database and get Pantry info
        //dummy data

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("registration").addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean found = false;
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        if(postSnapshot.getKey() != null && postSnapshot.getKey().equals(pantryId)){
                            if(postSnapshot.hasChild("daysOpen")){
                                Pantry temp = postSnapshot.getValue(Pantry.class);
                                if(temp != null) {
                                    displayPantry(temp);
                                   // foundId = true;
                                    found = true;
                                }
                            }
                            else{

                                Event temp = postSnapshot.getValue(Event.class);
                                if(temp != null) {
                                    displayEvent(temp);
                                  //  foundId = true;
                                    found = true;
                                }
                            }
                        }
                    }
                    if(!found){
                        //make everything go away
                        //
                       Log.i(TAG, pantryId);
                       mInfoWindowView.setVisibility(View.GONE);

                    }
              }

              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {
                  Log.i(TAG, "Database error: " + databaseError.getMessage());
              }
        });

    }

    private void displayPantry(Pantry pantry){
        pantryName.setText(pantry.getName());
        pantryAddress.setText("Address: " + pantry.getAddress());
        pantryPhone.setText("Phone Number: " + pantry.getPhoneNumber());
        pantryWebsite.setText("Website: " + pantry.getWebsite());

        eventDate.setVisibility(View.GONE);
        pantryTimeOpenClose.setText("Time Open: " + pantry.getTimeOpen() + " - " + pantry.getTimeClosed() );
        clickForMore.setVisibility(View.VISIBLE);
    }

    private void displayEvent(Event event){
        pantryName.setText(event.getName());
        pantryAddress.setText("Address: " + event.getAddress());
        pantryPhone.setText("Phone Number: " + event.getPhoneNumber());
        pantryWebsite.setText("Website: " + event.getWebsite());

        eventDate.setText("Event Date: " + event.getEventDate());
        pantryTimeOpenClose.setText("Time Open: " + event.getTimeOpen() + " - " + event.getTimeClosed() );
        clickForMore.setVisibility(View.VISIBLE);
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
