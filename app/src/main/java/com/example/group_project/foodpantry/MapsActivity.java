package com.example.group_project.foodpantry;

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
import android.widget.Button;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private String TAG = "Maps Activity";
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mCurrentLocation;
    private double mLatitude;
    private double mLongitude;

    private final int LOCATION_PERMISSION_CODE = 4;
    private EditText mZipCode;


   // private Boolean useZipCode = false;
    private Boolean permissionGranted = false;
    //private Boolean gotLocation = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the MapFragment and get notified when the map is ready to be used.
        //mZipCode = findViewById(R.id.AddrZipcode);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //get last known location through fused location
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getPhoneLocationPermission();
        if(permissionGranted){
            getPhoneLocation();
           //mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())));
            mMap.setMyLocationEnabled(true);

        }
        else{
            Log.i(TAG, "Permission Not Granted");

        }
       // LatLng sydney = new LatLng(-34, 151);
       // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
      //  mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    /**
     * Called by the Search Button on map,
     * gets the values of the Address EditText and decodes to Location using
     * GeoCoder
     * @param view
     */
    public void onClickSearchButton(View view){
        Button mSearchButton = findViewById(R.id.SearchButton);
        EditText mZipCodeAddr = findViewById(R.id.AddrZipcode);
        String mText = mZipCodeAddr.getText().toString();

        List<Address> mAddressList = null;
        if(mText != null && mText != ""){
            Geocoder mGeocoder = new Geocoder(this);
            try {
                mAddressList = mGeocoder.getFromLocationName(mText, 1);
            } catch (IOException e) {
                e.printStackTrace();
                Log.i(TAG, "Error Getting Location from Address or Zipcode");
            }

            if(mAddressList != null){
                Address mAddress = mAddressList.get(0);
                //Location newLoc = new Location(mFusedLocationProviderClient);
                if (mAddress != null) {
                    //  no need to send location because this doesn't need to update location
                    updateLocation(mAddress.getLatitude(), mAddress.getLongitude(), null);
                }
            }
        }


    }

    private void getPhoneLocation(){

        try{
            if(permissionGranted){
                Task<Location> lastLocation;
                if(null != (lastLocation = mFusedLocationProviderClient.getLastLocation())) {

                    lastLocation.addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful()) { //got location
                                Location temp = task.getResult();
                                if (temp != null){
                                    updateLocation(temp.getLatitude(), temp.getLongitude(), temp);
                                }
                            } else {
                                Log.i(TAG, "Could not get location");
                            }
                        }
                    });
                }
            }
        }
        catch (SecurityException e){
            // needs to handles or error
            Log.i(TAG, "Security Exception when getting locaton: " + e.getMessage());
        }

    }

    private void updateLocation(double lat, double lng, Location loc){
         LatLng mLatLng = new LatLng(lat, loc.getLongitude());
            mMap.addMarker(new MarkerOptions().position(mLatLng).title("Home"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 15f));
            if(loc != null){
                mCurrentLocation = loc;
            }
            mLatitude = lat;
            mLongitude = lng;

    }
    private void getPhoneLocationPermission(){
        Log.i(TAG, "Getting Phone's Location");
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted  --- request perms, return handled by onRequestPermissionsResult
            requestPermissions(new String[] {ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
            return;
        }
        else{
           //permission granted for location
            permissionGranted = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode) {
            case LOCATION_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getPhoneLocationPermission();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Location Access Not Granted", Toast.LENGTH_LONG).show();
                    Log.i(TAG, "Location permission was not granted");
                    useZipCode = true;
                }
                return;
            }

        }

    }
}
