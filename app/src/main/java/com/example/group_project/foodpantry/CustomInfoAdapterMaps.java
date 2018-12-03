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
    //String registerationID;

    private String TAG = "Custom Info Adapter";
    public CustomInfoAdapterMaps(Context c){
        this.mInfoWindowView = LayoutInflater.from(c).inflate(R.layout.custome_info_window, null);
        this.mContext = c;

        clickForMore = (TextView) this.mInfoWindowView.findViewById(R.id.pantryMoreInfo);
        clickForMore.setVisibility(View.GONE);


    }

    public void render(Marker marker, View view){
        pantryName = (TextView) view.findViewById(R.id.pantryName);
        pantryAddress = (TextView) view.findViewById(R.id.pantryAddress);
        pantryPhone = (TextView) view.findViewById(R.id.pantryPhone);
        pantryWebsite = (TextView) view.findViewById(R.id.pantryWebsite);
        eventDate = (TextView) view.findViewById(R.id.eventDate);
        pantryTimeOpenClose = (TextView) view.findViewById(R.id.pantryTimeOpenClose);

        final String registerationID = marker.getTitle();
        //

        /*pantryName.setText("");
        pantryAddress.setText("");
        pantryPhone.setText("");
        pantryWebsite.setText("");
        eventDate.setText("");
        pantryTimeOpenClose.setText("");
        this.mInfoWindowView.setVisibility(View.GONE);
       */
        //connect to database and get Pantry info
        //dummy data
       // Toast.makeText(this.mContext, registerationID, Toast.LENGTH_LONG).show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("registration").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               // boolean found = false;
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                   // Toast.makeText(CustomInfoAdapterMaps.this.mContext, "2: " + registerationID, Toast.LENGTH_LONG).show();
                    if(postSnapshot.getKey() != null &&
                            postSnapshot.getKey().equals(registerationID)){
                        //Log.i(TAG, "Found: " + postSnapshot.getKey());
                       // Toast.makeText(CustomInfoAdapterMaps.this.mContext, "3: " +registerationID, Toast.LENGTH_LONG).show();
                        if(postSnapshot.hasChild("daysOpen")){
                          //  Log.i(TAG, "Found: " + postSnapshot.getKey());
                            Pantry pantry = postSnapshot.getValue(Pantry.class);
                            if(pantry != null) {
                           //     Toast.makeText(CustomInfoAdapterMaps.this.mContext,"4: " + registerationID, Toast.LENGTH_LONG).show();
                                displayPantry(pantry);
                                return;
                            }

                        }
                        else{
                            Event event = postSnapshot.getValue(Event.class);
                            if(event != null && !event.isPantry()) {
                                displayEvent(event);
                                return;
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
     //   Log.i(TAG, pantry.toString());
        CustomInfoAdapterMaps.this.pantryName.setText(pantry.getName());
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
        CustomInfoAdapterMaps.this.eventDate.setVisibility(View.VISIBLE);

    }


    @Override
    public View getInfoWindow(Marker marker) {

        render(marker, this.mInfoWindowView);
        return mInfoWindowView;
        //return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        //render(marker, this.mInfoWindowView);
        //return mInfoWindowView;
        return null;
    }
}
