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
    private TextView registrationName;
    private TextView registrationAddress;
    private TextView registrationPhone;
    private TextView registrationWebsite;
    private TextView eventDate;
    private TextView registrationTimeOpenClose;
    private TextView clickForMore;
    Map<String, Event> mEvents;
    Map<String, Pantry> mPantry;

    private String TAG = "Custom Info Adapter";
    public CustomInfoAdapterMaps(Context c, Map<String, Event> events, Map<String, Pantry> pantry ){
        this.mInfoWindowView = LayoutInflater.from(c).inflate(R.layout.custome_info_window, null);
        this.mContext = c;

        clickForMore = (TextView) this.mInfoWindowView.findViewById(R.id.registrationMoreInfo);
        clickForMore.setVisibility(View.GONE);

        mEvents = events;
        mPantry = pantry;

    }

    private void render(Marker marker, View view){
        registrationName = (TextView) view.findViewById(R.id.registrationName);
        registrationAddress = (TextView) view.findViewById(R.id.registrationAddress);
        registrationPhone = (TextView) view.findViewById(R.id.registrationPhone);
        registrationWebsite = (TextView) view.findViewById(R.id.registrationWebsite);
        eventDate = (TextView) view.findViewById(R.id.eventDate);
        registrationTimeOpenClose = (TextView) view.findViewById(R.id.registrationTimeOpenClose);

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

    private void displayCommon(String name, String address, String phone,
                               String website, String time){

        // set the common property for pantry or event
        CustomInfoAdapterMaps.this.registrationName.setText(name);

        CustomInfoAdapterMaps.this.registrationAddress.setText(this.mContext.getResources()
                .getString(R.string.adaptarAddress, address));
        CustomInfoAdapterMaps.this.registrationPhone.setText(this.mContext.getResources()
                .getString(R.string.adaptarPhone, phone));
        CustomInfoAdapterMaps.this.registrationWebsite.setText(this.mContext.getResources()
                .getString(R.string.adaptarWebsite, website));
        CustomInfoAdapterMaps.this.registrationTimeOpenClose.setText(this.mContext.getResources()
                .getString(R.string.adaptarTimeOpenClose, time));
        CustomInfoAdapterMaps.this.clickForMore.setVisibility(View.VISIBLE);

    }
    private void displayPantry(Pantry pantry){
        displayCommon(pantry.getName(), pantry.getAddress(), pantry.getPhoneNumber(),
                pantry.getWebsite(), pantry.getTimeOpen()+ " - " + pantry.getTimeClosed() );
        CustomInfoAdapterMaps.this.eventDate.setVisibility(View.GONE);

    }

    private void displayEvent(Event event){
        displayCommon(event.getName(), event.getAddress(), event.getPhoneNumber(),
                event.getWebsite(), event.getTimeOpen()+ " - " + event.getTimeClosed() );

        CustomInfoAdapterMaps.this.eventDate.setText(this.mContext.getResources()
                .getString(R.string.adaptarEventDate, event.getEventDate()));
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
        render(marker, this.mInfoWindowView);
        return mInfoWindowView;
        //return null;
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
