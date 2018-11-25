package com.example.group_project.foodpantry;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
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

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mCurrentLocation;
    private final int LOCATION_PERMISSION_CODE = 1;
    private String TAG = "Maps Activity";
    private Boolean useZipCode = false;
    private Boolean permissionGranted = false;
    private Boolean gotLocation = false;
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


        if(gotLocation){
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())));
        }
       // LatLng sydney = new LatLng(-34, 151);
       // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
      //  mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void getPhoneLocation(){
        getPhoneLocationPermission();
        try{
        if(permissionGranted){
            Task lastLocation = mFusedLocationProviderClient.getLastLocation();
            lastLocation.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){ //got location
                        gotLocation = true;
                        mCurrentLocation = (Location) task.getResult();
                    }
                    else{
                       Log.i(TAG, "Could not get location");
                    }
                }
            });
        }
        else{
            //TODO implement use by zipcode
        } }
        catch (SecurityException e){
            // needs to handles or error
            Log.i(TAG, "Security Exception when getting locaton: " + e.getMessage());
        }
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
