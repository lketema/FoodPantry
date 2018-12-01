package com.example.group_project.foodpantry;

import android.Manifest;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import java.io.IOException;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private String TAG = "Maps Activity";
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    public Location mCurrentLocation = null;

    private final int LOCATION_PERMISSION_CODE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the MapFragment and get notified when the map is ready to be used.

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //get last known location through fused location
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //default
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(38.98, -76.94), 15f));
        getPhoneLocation();

        // add Pantries from Database and google Search API

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
