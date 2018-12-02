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

    private Pantry pantry;


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

        database = FirebaseDatabase.getInstance();

        addr = findViewById(R.id.pantryAddress);
        phNum = findViewById(R.id.pantryPhone);
        emAddr = findViewById(R.id.pantryEmail);


        directions = findViewById(R.id.directionsButton);
        Intent intent = getIntent();
        String pantryId = intent.getStringExtra("pantryId");
        String uId = intent.getStringExtra("userID");


        // access to database
        getDatabaseInfo(pantryId);

        //


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
