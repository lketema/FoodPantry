package com.example.group_project.foodpantry;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

public class PantryInfo extends AppCompatActivity {

    private TextView addr;
    private TextView phNum;
    private TextView emAddr;
    private Button website;
    private Button directions;
    private TextView timeOpen;
    private TextView timeClosed;
    private CheckBox favorite;


    FirebaseDatabase database;
        /**
     * String name, String address, String phoneNumber, String
     emailAddress, String website, String timeOpen, String timeClosed,
     boolean[] daysOpen
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pantry);

        addr = findViewById(R.id.pantryAddressInfo);
        phNum = findViewById(R.id.pantryPhoneInfo);
        emAddr = findViewById(R.id.pantryEmailInfo);
        timeOpen = findViewById(R.id.pantryTimeOpenInfo);
        timeClosed = findViewById(R.id.pantryTimeClosedInfo);

        directions = findViewById(R.id.directionsButton);
        Intent intent = getIntent();
        String pantryId = intent.getStringExtra("pantryId");
        String uId = intent.getStringExtra("userID");


        // access to database
        getDatabaseInfo(pantryId);

        //
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // update in the database for the user.
            }
        });

        directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q=" + "7999 Regents dr, College Park, MD"));
                startActivity(intent);
            }
        });

    }

    private void getDatabaseInfo(String id){
        addr.setText("hello");
        phNum.setText("phNum");
        emAddr.setText("emAddr");



    }
}
