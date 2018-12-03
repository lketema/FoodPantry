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
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegistrationInfo extends AppCompatActivity {

    private TextView mName, mAddress, mPhoneNumber, mEmail, mWebsite, mTimeOpenClose, mEventDate;
    private Button mWebsiteButton, mDirectionsButton;
    private CheckBox mFavorite, mSun, mMon, mTue, mWed, mThu, mFri, mSat;

    private Registration registration;
    private User user;

    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pantry);

        //get database
        database = FirebaseDatabase.getInstance().getReference();

        //assign all UI elements
        mName = (TextView) findViewById(R.id.registrationName);
        mAddress = (TextView) findViewById(R.id.registrationAddress);
        mPhoneNumber = (TextView) findViewById(R.id.registrationPhoneNumber);
        mEmail = (TextView) findViewById(R.id.registrationEmail);
        mWebsite = (TextView) findViewById(R.id.registrationWebsite);
        mTimeOpenClose = (TextView) findViewById(R.id.registrationTimeOpenClose);
        mEventDate = (TextView) findViewById(R.id.eventDate);

        mWebsiteButton = (Button) findViewById(R.id.websiteButton);
        mDirectionsButton = (Button) findViewById(R.id.directionsButton);

        mFavorite = (CheckBox) findViewById(R.id.favorites);
        mSun = (CheckBox) findViewById(R.id.checkbox_sun);
        mMon = (CheckBox) findViewById(R.id.checkbox_mon);
        mTue = (CheckBox) findViewById(R.id.checkbox_tue);
        mWed = (CheckBox) findViewById(R.id.checkbox_wed);
        mThu = (CheckBox) findViewById(R.id.checkbox_thu);
        mFri = (CheckBox) findViewById(R.id.checkbox_fri);
        mSat = (CheckBox) findViewById(R.id.checkbox_sat);


        //get info from Intent
        Intent intent = getIntent();
        final String registrationID = "-LSkjneBfQkmH5uRnctE",
                //intent.getStringExtra("registrationID"),
                userID = "1yfb4cmbjeZf85VEfhphObkkoVg1";
                        //intent.getStringExtra("userID");

        // access to database for registration
        DatabaseReference child = database.child("registration").child(registrationID);

        child.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild("daysOpen")) {
                    registration = (Pantry) dataSnapshot.getValue(Pantry.class);

                    //may as well set Pantry specific stuff here
                    if (((Pantry) registration).isOpenOn(Pantry.SUNDAY)) mSun.setChecked(true);
                    if (((Pantry) registration).isOpenOn(Pantry.MONDAY)) mMon.setChecked(true);
                    if (((Pantry) registration).isOpenOn(Pantry.TUESDAY)) mTue.setChecked(true);
                    if (((Pantry) registration).isOpenOn(Pantry.WEDNESDAY)) mWed.setChecked(true);
                    if (((Pantry) registration).isOpenOn(Pantry.THURSDAY)) mThu.setChecked(true);
                    if (((Pantry) registration).isOpenOn(Pantry.FRIDAY)) mFri.setChecked(true);
                    if (((Pantry) registration).isOpenOn(Pantry.SATURDAY)) mSat.setChecked(true);

                    //set Event views to be invisible
                    mEventDate.setVisibility(View.GONE);

                } else {
                    registration = (Event) dataSnapshot.getValue(Event.class);

                    mEventDate.setText(((Event) registration).getEventDate());

                    RelativeLayout pantryLayout = (RelativeLayout) findViewById(R.id.pantry_layout);
                    pantryLayout.setVisibility(View.GONE);

                }

                //populate text fields based on what the registration is
                mName.setText(registration.getName());
                mAddress.setText(registration.getAddress());
                mPhoneNumber.setText(registration.getPhoneNumber());
                mEmail.setText(registration.getEmailAddress());
                mWebsite.setText(registration.getWebsite());
                mTimeOpenClose.setText(registration.getTimeOpenClosedString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //access to database for user
        child = database.child("users").child(userID);
        child.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                //see if the user has this place as a favorite
                //if so, check the favorites box
                if (user.getFavorites().contains(registrationID)) mFavorite.setChecked(true);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //check to see if user clicks on button to update database
        mFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (user != null) {
                    if (isChecked && !user.getFavorites().contains(registrationID)) {
                        user.addFavorite(registrationID);
                        database.child("users").child(userID).setValue(user);

                    } else if (!isChecked && user.getFavorites().contains(registrationID)) {
                        user.removeFavorite(registrationID);
                        database.child("users").child(userID).setValue(user);
                    }
                }
            }
        });


        mDirectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q=" + registration.getAddress()));
                startActivity(intent);
            }
        });

        mWebsiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent enterRegistrationWebsiteIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(registration.getWebsite()));
                Intent chooser = Intent.createChooser(enterRegistrationWebsiteIntent,
                        "Choose how to view " + registration.getName() + "'s website:");
                startActivity(chooser);

            }
        });



    }

}
