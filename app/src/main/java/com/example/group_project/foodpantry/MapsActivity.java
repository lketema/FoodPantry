package com.example.group_project.foodpantry;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{
    private String TAG = "Maps Activity";
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    public Location mCurrentLocation = null;

    private final int LOCATION_PERMISSION_CODE = 4;
    Intent getsIntent;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the MapFragment and get notified when the map is ready to be used.

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getsIntent = getIntent();
        //get last known location through fused location
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //default
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(38.98, -76.94), 15f));
        getPhoneLocation();

        addMarkers();
        // add Pantries from Database and google Search API

       // final Intent nextScreen = new Intent(MapsActivity.this, PantryInfo.class);
        mMap.setInfoWindowAdapter(new CustomInfoAdapterMaps(getApplicationContext()));
       // mMap.setOnMapClickListener(this)
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent nextScreen = new Intent(MapsActivity.this, PantryInfo.class);
                nextScreen.putExtra("userID", getsIntent.getStringExtra("userID"));
                nextScreen.putExtra("pantryId", marker.getTitle());
                startActivity(nextScreen);
            }
        });



    }



    /**
     * Called by the Search Button on map,
     * gets the values of the Address EditText and decodes to Location using
     * GeoCoder
     * @param view
     */
    public void onClickSearchButton(View view){
       // Button mSearchButton = findViewById(R.id.SearchButton);
        EditText mZipCodeAddr = findViewById(R.id.AddrZipcode);
        String mText = mZipCodeAddr.getText().toString();

        List<Address> mAddressList = null;
        if(!mText.equals("")){
            Geocoder mGeocoder = new Geocoder(this);
            try {
                mAddressList = mGeocoder.getFromLocationName(mText, 1);
            } catch (IOException e) {
                e.printStackTrace();
                Log.i(TAG, "Error Getting Location from Address or Zipcode");
            }

            if(mAddressList != null && mAddressList.size() != 0){
                Address mAddress = mAddressList.get(0);
                //Location newLoc = new Location(mFusedLocationProviderClient);
                if (mAddress != null) {
                    //  no need to send location because this doesn't need to update location
                    updateLocation(mAddress.getLatitude(), mAddress.getLongitude(), null);
                }
            }
            else{
                // make toast message saying wrong address input
                Toast.makeText(getApplicationContext(), "Incorrect Address Input, Please Try Again", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "Enter an Address or ZipCode", Toast.LENGTH_LONG).show();
        }

    }
    /*
    when setting the marker from database, we need to
    display the pantry name, address, email and
    if volunteers are needed.
     */

    private void addMarkers(){
        final LatLng testing = new LatLng(38.88, -76.00);
        mMap.addMarker(new MarkerOptions()
             .position(testing)
             .title("P09308q50458q9034090509")
             .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        databaseReference = FirebaseDatabase.getInstance().getReference("Registration");

    }


    /**
     * update location will move map view to specified latitude and longitude
     * if location is specified, it will update the mCurrentLocation
     * @param loc
     */
    private void updateLocation(double lat, double lng, Location loc){
         mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15f));
         if (loc != null){  // for case where user enable their location
             mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("You"));
             mCurrentLocation = loc;
         }
    }
    private void getPhoneLocation(){
        Log.i(TAG, "Getting Phone's Location Permission");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            // Permission is not granted  --- request perms, return handled by onRequestPermissionsResult
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_CODE);
        }
        else {
            try {
                mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            MapsActivity.this.updateLocation(location.getLatitude(), location.getLongitude(), location);
                        } else {
                             Log.i(TAG, "Could not get location");
                        }

                        }
                    });
            }
            catch (SecurityException e){
                Log.i(TAG, "Security Exception when getting locaton: " + e.getMessage());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode) {
            case LOCATION_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getPhoneLocation();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Location Access Not Granted", Toast.LENGTH_LONG).show();
                    Log.i(TAG, "Location permission was not granted");
                    //default location washington dc
                }
                return;
            }

        }

    }
}
