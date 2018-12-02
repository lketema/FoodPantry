package com.example.group_project.foodpantry;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OptionsActivity extends Activity{

    Button favButton;
    Button registButton;
    Button addEventButton;
    User user;
    DatabaseReference database;


    /**
     * Allows the user to access a variety of options including:
     * Access their list of favorites
     * View recently examined pantries
     * Create a new event as a pantry and add it to the map
     **/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        database = FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        final Intent data = getIntent();
        final String currentID = "UDFlWpEIbrd0906YczwPsvHZvFc2";//data.getStringExtra("userID");

        database.child("users").child(currentID).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {

               User newUser = dataSnapshot.getValue(User.class);

               user = new User(newUser.getName(), newUser.getEmail(), newUser.getEmail(), newUser.getUserType());

               if(user.getUserType().equals("owner")) {
                   // Locate the button in activity_options.xml
                   addEventButton = (Button) findViewById(R.id.AddEventButton);
                   registButton = findViewById(R.id.MyRegistrationsButton);

                   addEventButton.setVisibility(View.VISIBLE);
                   registButton.setVisibility(View.VISIBLE);

                   // Capture button clicks
                   addEventButton.setOnClickListener(new OnClickListener() {
                       public void onClick(View arg0) {

                           // Start NewActivity.class
                           Intent myIntent = new Intent(OptionsActivity.this, AddEventActivity.class);
                           myIntent.putExtra("userID", currentID);
                           startActivity(myIntent);
                       }
                   });

                   registButton.setOnClickListener(new OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           Intent myIntent = new Intent(OptionsActivity.this, RegistrationListActivty.class);
                           myIntent.putExtra("userID", currentID);
                           startActivity(myIntent);
                       }
                   });
               }



           }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        } );




        setContentView(R.layout.activity_options);

        favButton = (Button) findViewById(R.id.FavoritesButton);

        // Capture button clicks
        favButton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent = new Intent(OptionsActivity.this, FavoritesActivity.class);
                myIntent.putExtra("userID", currentID);
                startActivity(myIntent);
            }
        });

        // Locate the button in activity_main.xml
        registButton = (Button) findViewById(R.id.MyRegistrationsButton);





    }


}
