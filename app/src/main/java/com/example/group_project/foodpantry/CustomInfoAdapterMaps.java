package com.example.group_project.foodpantry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.Map;

public class CustomInfoAdapterMaps implements GoogleMap.InfoWindowAdapter {
    private final View mInfoWindowView;
    private Context mContext;
    private TextView registrationName, clickForMore, registrationType;
    private Map<String, Event> mEvents;
    private Map<String, Pantry> mPantry;

   // private String TAG = "Custom Info Adapter";
    public CustomInfoAdapterMaps(Context c, Map<String, Event> events, Map<String, Pantry> pantry ){

        this.mInfoWindowView = LayoutInflater.from(c).inflate(R.layout.custome_info_window, null);
        this.mContext = c;
        mEvents = events;
        mPantry = pantry;

    }

    private void render(Marker marker, View view){
        registrationName = (TextView) view.findViewById(R.id.registrationName);
        clickForMore = (TextView) view.findViewById(R.id.registrationMoreInfo);
        registrationType = (TextView) view.findViewById(R.id.registrationType);

        final String registrationID = marker.getTitle();

        if(!mPantry.isEmpty() &&  mPantry.containsKey(registrationID)){
            displayPantry(mPantry.get(registrationID));
        }
        else if(!mEvents.isEmpty() && mEvents.containsKey(registrationID)){
            displayEvent(mEvents.get(registrationID));
        }
        else{
            mInfoWindowView.setVisibility(View.GONE);
        }

    }
    private void displayPantry(Pantry pantry){

        registrationName.setText(pantry.getName());
        registrationType.setText("(pantry)");
    }

    private void displayEvent(Event event){
        registrationName.setText(event.getName());
        registrationType.setText("(event)");
    }

    @Override
    public View getInfoWindow(Marker marker) {
        render(marker, this.mInfoWindowView);
        return mInfoWindowView;

    }

    @Override
    public View getInfoContents(Marker marker) {
        render(marker, this.mInfoWindowView);
        return mInfoWindowView;

    }
}
// below for database access but since process is asynchronous, it won't work properly
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
        /*
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
        */
