package com.example.group_project.foodpantry;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

        Intent intent = getIntent();
        String pantryId = intent.getStringExtra("pantryId");
        String uId = intent.getStringExtra("userID");


        // access to database
        getDatabaseInfo(pantryId);



    }

    private void getDatabaseInfo(String id){
        addr.setText("hello");
        phNum.setText("phNum");
        emAddr.setText("emAddr");

    }
}
