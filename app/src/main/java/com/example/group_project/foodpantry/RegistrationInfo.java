package com.example.group_project.foodpantry;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegistrationInfo extends AppCompatActivity {

    private TextView addr;
    private TextView phNum;
    private TextView emAddr;
    private Button website;
    private Button directions;
    private TextView timeOpen;
    private TextView timeClosed;
    private CheckBox favorite;

    private Registration registration;
    private User user;

    DatabaseReference database;
        /**
     * String name, String address, String phoneNumber, String
     emailAddress, String website, String timeOpen, String timeClosed,
     boolean[] daysOpen
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pantry);

        database = FirebaseDatabase.getInstance().getReference();

        addr = findViewById(R.id.pantryAddress);
        phNum = findViewById(R.id.pantryPhone);
        emAddr = findViewById(R.id.pantryEmail);

        directions = findViewById(R.id.directionsButton);

        Intent intent = getIntent();
        final String registrationID = intent.getStringExtra("registrationID"),
            userID = intent.getStringExtra("userID");

        // access to database
        DatabaseReference child = database.child("registration").child(registrationID);

        child.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild("daysOpen")) {
                    registration = (Pantry) dataSnapshot.getValue(Pantry.class);
                } else {
                    registration = (Event) dataSnapshot.getValue(Event.class);
                }


                Log.i("TAGGY", registration.toString());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //getDatabaseInfo(pantryId);
        child = database.child("users").child(userID);
        child.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //


        directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q=" + registration.getAddress()));
                startActivity(intent);
            }
        });

    }

}
