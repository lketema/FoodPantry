package com.example.group_project.foodpantry;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
    private Marker mLastMarker;

    private String TAG = "Custom Info Adapter";
    public CustomInfoAdapterMaps(Context c){
        this.mInfoWindowView = LayoutInflater.from(c).inflate(R.layout.custome_info_window, null);
        this.mContext = c;


    }

    public void render(Marker marker, View view){
        Toast.makeText(this.mContext, "I get here", Toast.LENGTH_SHORT).show();
        pantryName = (TextView) this.mInfoWindowView.findViewById(R.id.pantryName);
       //pantryName.setText("");
        pantryAddress = (TextView) this.mInfoWindowView.findViewById(R.id.pantryAddress);
       //pantryAddress.setText("");
        pantryPhone = (TextView) this.mInfoWindowView.findViewById(R.id.pantryPhone);
       //pantryPhone.setText("");
        pantryWebsite = (TextView) this.mInfoWindowView.findViewById(R.id.pantryWebsite);
       //pantryWebsite.setText("");
        eventDate = (TextView) this.mInfoWindowView.findViewById(R.id.eventDate);
        //eventDate.setText("");
        pantryTimeOpenClose = (TextView) this.mInfoWindowView.findViewById(R.id.pantryTimeOpenClose);
       //pantryTimeOpenClose.setText("");
        clickForMore = (TextView) this.mInfoWindowView.findViewById(R.id.pantryMoreInfo);
        //this.mInfoWindowView.setVisibility(View.GONE);
        String pantryId = marker.getTitle();
        // Log.i(TAG, "Pantry ID: " + pantryId);
        pantryInfo(pantryId);

    }


    private void pantryInfo(final String pantryId){
        //connect to database and get Pantry info
        //dummy data

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("registration").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               // boolean found = false;
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    if(postSnapshot.getKey() != null && postSnapshot.getKey().equals(pantryId)){
                        //Log.i(TAG, "Found: " + postSnapshot.getKey());
                        if(postSnapshot.hasChild("daysOpen")){
                          //  Log.i(TAG, "Found: " + postSnapshot.getKey());
                            Pantry temp = postSnapshot.getValue(Pantry.class);
                            if(temp != null) {
                                //displayPantry(temp);
                                //CustomInfoAdapterMaps.this.pantryName.setText("BKhfahfs");
                               // Log.i(TAG, temp.toString());
                                Toast.makeText(CustomInfoAdapterMaps.this.mContext, "I get here", Toast.LENGTH_SHORT).show();
                                CustomInfoAdapterMaps.this.pantryName.setText(temp.getName());
                                CustomInfoAdapterMaps.this.pantryName.setText("Blha");
                                CustomInfoAdapterMaps.this.pantryAddress.setText("Address: " + temp.getAddress());
                                CustomInfoAdapterMaps.this.pantryPhone.setText("Phone Number: " + temp.getPhoneNumber());
                                CustomInfoAdapterMaps.this.pantryWebsite.setText("Website: " + temp.getWebsite());

                                CustomInfoAdapterMaps.this.eventDate.setVisibility(View.GONE);
                                CustomInfoAdapterMaps.this.pantryTimeOpenClose.setText("Time Open: " + temp.getTimeOpen() + " - " + temp.getTimeClosed() );
                                CustomInfoAdapterMaps.this.clickForMore.setVisibility(View.VISIBLE);
                            }
                        }
                        else{

                            Event event = postSnapshot.getValue(Event.class);
                            if(event != null && !event.isPantry()) {
                                //displayEvent(event);
                                CustomInfoAdapterMaps.this.pantryName.setText(event.getName());
                                CustomInfoAdapterMaps.this.pantryAddress.setText("Address: " + event.getAddress());
                                CustomInfoAdapterMaps.this.pantryPhone.setText("Phone Number: " + event.getPhoneNumber());
                                CustomInfoAdapterMaps.this.pantryWebsite.setText("Website: " + event.getWebsite());

                                CustomInfoAdapterMaps.this.eventDate.setText("Event Date: " + event.getEventDate());
                                CustomInfoAdapterMaps.this.pantryTimeOpenClose.setText("Time Open: " + event.getTimeOpen() + " - " + event.getTimeClosed() );
                                CustomInfoAdapterMaps.this.clickForMore.setVisibility(View.VISIBLE);
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

    }

    private void displayPantry(Pantry pantry){
       // this.mInfoWindowView.setVisibility(View.VISIBLE);
        Log.i(TAG, pantry.toString());
        CustomInfoAdapterMaps.this.pantryName.setText(pantry.getName());
        CustomInfoAdapterMaps.this.pantryName.setText("Blha");
        CustomInfoAdapterMaps.this.pantryAddress.setText("Address: " + pantry.getAddress());
        CustomInfoAdapterMaps.this.pantryPhone.setText("Phone Number: " + pantry.getPhoneNumber());
        CustomInfoAdapterMaps.this.pantryWebsite.setText("Website: " + pantry.getWebsite());

        CustomInfoAdapterMaps.this.eventDate.setVisibility(View.GONE);
        CustomInfoAdapterMaps.this.pantryTimeOpenClose.setText("Time Open: " + pantry.getTimeOpen() + " - " + pantry.getTimeClosed() );
        CustomInfoAdapterMaps.this.clickForMore.setVisibility(View.VISIBLE);

    }

    private void displayEvent(Event event){
       // this.mInfoWindowView.setVisibility(View.VISIBLE);
        CustomInfoAdapterMaps.this.pantryName.setText(event.getName());
        CustomInfoAdapterMaps.this.pantryAddress.setText("Address: " + event.getAddress());
        CustomInfoAdapterMaps.this.pantryPhone.setText("Phone Number: " + event.getPhoneNumber());
        CustomInfoAdapterMaps.this.pantryWebsite.setText("Website: " + event.getWebsite());

        CustomInfoAdapterMaps.this.eventDate.setText("Event Date: " + event.getEventDate());
        CustomInfoAdapterMaps.this.pantryTimeOpenClose.setText("Time Open: " + event.getTimeOpen() + " - " + event.getTimeClosed() );
        CustomInfoAdapterMaps.this.clickForMore.setVisibility(View.VISIBLE);

    }


    @Override
    public View getInfoWindow(Marker marker) {

        render(marker, this.mInfoWindowView);
        return mInfoWindowView;
        //return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        render(marker, this.mInfoWindowView);
        return mInfoWindowView;
        //return null;
    }
}
