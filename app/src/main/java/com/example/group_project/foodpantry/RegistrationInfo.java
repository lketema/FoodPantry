package com.example.group_project.foodpantry;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.regex.Pattern;

public class RegistrationInfo extends AppCompatActivity {

    private TextView mPrivileges, mName, mAddress, mPhoneNumber,
            mEmail, mWebsite, mTimeOpenClose, mEventDate, mTextEventDate;
    private EditText mNameEdit, mAddressEdit, mPhoneNumberEdit,
            mEmailEdit, mWebsiteEdit;
    private RelativeLayout mTimeRelativeLayout, mPantryRelativeLayout;

    private static EditText mTimeOpenEdit, mTimeClosedEdit, mEventDateEdit;
    private Button mWebsiteButton, mDirectionsButton, mUpdateButton, mBackButton;
    private CheckBox mFavorite, mSun, mMon, mTue, mWed, mThu, mFri, mSat;

    private Registration registration;
    private boolean isPantry = false;
    private User user;
    private boolean isPantryOwner = false;

    private static DatabaseReference database;

    private Intent returnIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pantry);

        //get database
        database = FirebaseDatabase.getInstance().getReference();

        //assign all UI elements
        mPrivileges = (TextView) findViewById(R.id.PrivilegesTextView);
        mName = (TextView) findViewById(R.id.registrationName);
        mAddress = (TextView) findViewById(R.id.registrationAddress);
        mPhoneNumber = (TextView) findViewById(R.id.registrationPhoneNumber);
        mEmail = (TextView) findViewById(R.id.registrationEmail);
        mWebsite = (TextView) findViewById(R.id.registrationWebsite);
        mTimeOpenClose = (TextView) findViewById(R.id.registrationTimeOpenClose);
        mEventDate = (TextView) findViewById(R.id.eventDate);
        mTextEventDate = (TextView) findViewById(R.id.pTextEventDate);

        mNameEdit = (EditText) findViewById(R.id.pRegistrationName);
        mAddressEdit = (EditText) findViewById(R.id.pRegistrationAddress);
        mPhoneNumberEdit = (EditText) findViewById(R.id.pRegistrationPhoneNumber);
        mEmailEdit = (EditText) findViewById(R.id.pRegistrationEmail);
        mWebsiteEdit = (EditText) findViewById(R.id.pRegistrationWebsite);
        mTimeOpenEdit = (EditText) findViewById(R.id.pChangeOpenTime);
        mTimeClosedEdit = (EditText) findViewById(R.id.pChangeClosedTime);
        mEventDateEdit = (EditText) findViewById(R.id.pEventDate);

        mTimeRelativeLayout = (RelativeLayout) findViewById(R.id.pChangeTimeOpenAndClose);
        mPantryRelativeLayout = (RelativeLayout) findViewById(R.id.pantry_layout);

        mWebsiteButton = (Button) findViewById(R.id.websiteButton);
        mDirectionsButton = (Button) findViewById(R.id.directionsButton);
        mUpdateButton = (Button) findViewById(R.id.updateButton);
        mBackButton = (Button) findViewById(R.id.goBackButton);

        mFavorite = (CheckBox) findViewById(R.id.favorites);
        mSun = (CheckBox) findViewById(R.id.checkbox_sun);
        mMon = (CheckBox) findViewById(R.id.checkbox_mon);
        mTue = (CheckBox) findViewById(R.id.checkbox_tue);
        mWed = (CheckBox) findViewById(R.id.checkbox_wed);
        mThu = (CheckBox) findViewById(R.id.checkbox_thu);
        mFri = (CheckBox) findViewById(R.id.checkbox_fri);
        mSat = (CheckBox) findViewById(R.id.checkbox_sat);

        //get info from Intent
        final Intent intent = getIntent();

        final String registrationID = intent.getStringExtra("registrationID"),
                //"-LSkjneBfQkmH5uRnctE" "-LSoiOUalxyyMwiLJVts",

        userID = intent.getStringExtra("userID");
                //"foPc4vl745Z5oUe2NvrBaLlRUg83";
                //"1yfb4cmbjeZf85VEfhphObkkoVg1";


        returnIntent = new Intent(RegistrationInfo.this, MapsActivity.class)
                .putExtra("userID", userID)
                .putExtra("registrationID", registrationID)
                .putExtra("returning", true);
        returnIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);


        // access to database for registration
        DatabaseReference child = database.child("registration").child(registrationID);

        child.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild("daysOpen")) {
                    isPantry = true;

                    registration = dataSnapshot.getValue(Pantry.class);

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
                    mEventDateEdit.setVisibility(View.GONE);
                    mTextEventDate.setVisibility(View.GONE);

                } else {
                    registration = dataSnapshot.getValue(Event.class);

                    mEventDate.setText(((Event) registration).getEventDateForDisplay());
                    mEventDateEdit.setText(((Event) registration).getEventDate());

                    //set pantry views to be invisible
                    mPantryRelativeLayout.setVisibility(View.GONE);

                }

                //populate text fields based on what the registration is
                mName.setText(registration.getName());
                mNameEdit.setText(registration.getName());
                mAddress.setText(registration.getAddress());
                mAddressEdit.setText(registration.getAddress());
                mPhoneNumber.setText(registration.getPhoneNumber());
                mPhoneNumberEdit.setText(registration.getPhoneNumber());
                mEmail.setText(registration.getEmailAddress());
                mEmailEdit.setText(registration.getEmailAddress());
                mWebsite.setText(registration.getWebsite());
                mWebsiteEdit.setText(registration.getWebsite());
                mTimeOpenClose.setText(registration.getTimeOpenClosedString());
                mTimeOpenEdit.setText(registration.getTimeOpen());
                mTimeClosedEdit.setText(registration.getTimeClosed());
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
                if (dataSnapshot.hasChild("registrations")) {
                    isPantryOwner = true;
                    user = dataSnapshot.getValue(PantryOwner.class);
                } else {
                    user = dataSnapshot.getValue(User.class);
                }

                //see if the user has this place as a favorite
                //if so, check the favorites box
                if (user.getFavorites().contains(registrationID)) mFavorite.setChecked(true);

                //If user is the current registration owner,
                // set the privilege of editing the stuff
                if (user.getUserType().equals("owner")
                        && isPantryOwner && ((PantryOwner) user).ownsRegistration(registrationID)) {
                    // set non-editable views invisible and allow owner to go to town
                    mPrivileges.setVisibility(View.VISIBLE);
                    mPrivileges.setText("Hi, owner! You can edit your registration information here");
                    mName.setVisibility(View.GONE);
                    mNameEdit.setVisibility(View.VISIBLE);
                    mAddress.setVisibility(View.GONE);
                    mAddressEdit.setVisibility(View.VISIBLE);
                    mPhoneNumber.setVisibility(View.GONE);
                    mPhoneNumberEdit.setVisibility(View.VISIBLE);
                    mEmail.setVisibility(View.GONE);
                    mEmailEdit.setVisibility(View.VISIBLE);
                    mWebsite.setVisibility(View.GONE);
                    mWebsiteEdit.setVisibility(View.VISIBLE);
                    mTimeOpenClose.setVisibility(View.GONE);
                    mTimeRelativeLayout.setVisibility(View.VISIBLE);
                    mEventDate.setVisibility(View.GONE);
                    if (!isPantry) {
                        mEventDateEdit.setVisibility(View.VISIBLE);
                        mTextEventDate.setVisibility(View.VISIBLE);
                    }

                    mUpdateButton.setVisibility(View.VISIBLE);

                    mSun.setClickable(true);
                    mMon.setClickable(true);
                    mTue.setClickable(true);
                    mWed.setClickable(true);
                    mThu.setClickable(true);
                    mFri.setClickable(true);
                    mSat.setClickable(true);

                }
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

        mTimeOpenEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOpenTimePickerDialog();
            }
        });

        mTimeClosedEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showClosedTimePickerDialog();
            }
        });

        //if pantry, allow creator to update database days of operation
        if (isPantry) {
            mSun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (registration != null) {
                        if (isChecked && !((Pantry) registration).isOpenOn(Pantry.SUNDAY)) {
                            ((Pantry) registration).setDayOperational(Pantry.SUNDAY, true);
                        } else if (!isChecked && ((Pantry) registration).isOpenOn(Pantry.SUNDAY)) {
                            ((Pantry) registration).setDayOperational(Pantry.SUNDAY, false);
                        }
                    }
                }
            });
            mMon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (registration != null) {
                        if (isChecked && !((Pantry) registration).isOpenOn(Pantry.MONDAY)) {
                            ((Pantry) registration).setDayOperational(Pantry.MONDAY, true);
                        } else if (!isChecked && ((Pantry) registration).isOpenOn(Pantry.MONDAY)) {
                            ((Pantry) registration).setDayOperational(Pantry.MONDAY, false);
                        }
                    }
                }
            });
            mTue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (registration != null) {
                        if (isChecked && !((Pantry) registration).isOpenOn(Pantry.TUESDAY)) {
                            ((Pantry) registration).setDayOperational(Pantry.TUESDAY, true);
                        } else if (!isChecked && ((Pantry) registration).isOpenOn(Pantry.TUESDAY)) {
                            ((Pantry) registration).setDayOperational(Pantry.TUESDAY, false);
                        }
                    }
                }
            });
            mWed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (registration != null) {
                        if (isChecked && !((Pantry) registration).isOpenOn(Pantry.WEDNESDAY)) {
                            ((Pantry) registration).setDayOperational(Pantry.WEDNESDAY, true);
                        } else if (!isChecked && ((Pantry) registration).isOpenOn(Pantry.WEDNESDAY)) {
                            ((Pantry) registration).setDayOperational(Pantry.WEDNESDAY, false);
                        }
                    }
                }
            });
            mThu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (registration != null) {
                        if (isChecked && !((Pantry) registration).isOpenOn(Pantry.THURSDAY)) {
                            ((Pantry) registration).setDayOperational(Pantry.THURSDAY, true);
                        } else if (!isChecked && ((Pantry) registration).isOpenOn(Pantry.THURSDAY)) {
                            ((Pantry) registration).setDayOperational(Pantry.THURSDAY, false);
                        }
                    }
                }
            });
            mFri.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (registration != null) {
                        if (isChecked && !((Pantry) registration).isOpenOn(Pantry.FRIDAY)) {
                            ((Pantry) registration).setDayOperational(Pantry.FRIDAY, true);
                        } else if (!isChecked && ((Pantry) registration).isOpenOn(Pantry.FRIDAY)) {
                            ((Pantry) registration).setDayOperational(Pantry.FRIDAY, false);
                        }
                    }
                }
            });
            mSat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (registration != null) {
                        if (isChecked && !((Pantry) registration).isOpenOn(Pantry.SATURDAY)) {
                            ((Pantry) registration).setDayOperational(Pantry.SATURDAY, true);
                        } else if (!isChecked && ((Pantry) registration).isOpenOn(Pantry.SATURDAY)) {
                            ((Pantry) registration).setDayOperational(Pantry.SATURDAY, false);
                        }
                    }
                }
            });
        } else {
            //if event, allow creator to edit the date event
            mEventDateEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDatePickerDialog();

                }
            });
        }

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

        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (registration != null && allFieldsAreValid()) {
                    //update the registration properly
                    registration.setName(mNameEdit.getText().toString());
                    registration.setAddress(mAddressEdit.getText().toString());
                    registration.setPhoneNumber(mPhoneNumberEdit.getText().toString());
                    registration.setEmailAddress(mEmailEdit.getText().toString());
                    registration.setWebsite(mWebsiteEdit.getText().toString());
                    registration.setTimeOpen(mTimeOpenEdit.getText().toString());
                    registration.setTimeClosed(mTimeClosedEdit.getText().toString());
                    if (!isPantry) ((Event) registration).setEventDate(mEventDateEdit.getText().toString());
                    else {
                        ((Pantry) registration).setDayOperational(Pantry.SUNDAY, mSun.isChecked());
                        ((Pantry) registration).setDayOperational(Pantry.MONDAY, mMon.isChecked());
                        ((Pantry) registration).setDayOperational(Pantry.TUESDAY, mTue.isChecked());
                        ((Pantry) registration).setDayOperational(Pantry.WEDNESDAY, mWed.isChecked());
                        ((Pantry) registration).setDayOperational(Pantry.THURSDAY, mThu.isChecked());
                        ((Pantry) registration).setDayOperational(Pantry.FRIDAY, mFri.isChecked());
                        ((Pantry) registration).setDayOperational(Pantry.SATURDAY, mSat.isChecked());
                    }
                    Log.i("TAGGY", registration.toString());

                    //display Toast
                    Toast.makeText(RegistrationInfo.this, "Successfully updated registration!", Toast.LENGTH_SHORT).show();

                    //update in database
                    database.child("registration").child(registrationID).setValue(registration);
                }
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(returnIntent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(returnIntent);
    }

    private boolean allFieldsAreValid() {
        if (registration == null) return false;

        String name = mNameEdit.getText().toString().trim(),
                phoneNum = mPhoneNumberEdit.getText().toString().trim(),
                email = mEmailEdit.getText().toString().trim(),
                timeOpen = mTimeOpenEdit.getText().toString(),
                timeClosed = mTimeClosedEdit.getText().toString();

        String emailRegex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                +"[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",
                phoneNumRegex = "^\\d+$";

        if (name.equals("")) {
            Toast.makeText(RegistrationInfo.this, "Please provide a name", Toast.LENGTH_SHORT).show();
            findViewById(R.id.pRegistrationName).requestFocus();
            return false;
        }

        if (phoneNum.equals("")) {
            Toast.makeText(RegistrationInfo.this, "Please provide a phone number", Toast.LENGTH_SHORT).show();
            findViewById(R.id.pRegistrationPhoneNumber).requestFocus();
            return false;
        }

        if (phoneNum.length() > 12 || phoneNum.length() < 10 || !Pattern.matches(phoneNumRegex, phoneNum)) {
            Toast.makeText(RegistrationInfo.this, "Please provide a 10 digit phone number", Toast.LENGTH_SHORT).show();
            findViewById(R.id.pRegistrationPhoneNumber).requestFocus();
            return false;
        }

        if (email.equals("")) {
            Toast.makeText(RegistrationInfo.this, "Please provide an email", Toast.LENGTH_SHORT).show();
            findViewById(R.id.pRegistrationEmail).requestFocus();
            return false;
        }

        if (!Pattern.matches(emailRegex, email)) {
            Toast.makeText(RegistrationInfo.this, "Please provide a valid email", Toast.LENGTH_SHORT).show();
            findViewById(R.id.pRegistrationEmail).requestFocus();
            return false;
        }

        if (!closeAfterOpen(timeOpen, timeClosed)) {
            Toast.makeText(RegistrationInfo.this, "Open time must come before close time", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean closeAfterOpen(String openTime, String closeTime) {
        String[] openSegments = openTime.split(":");
        String[] closeSegments = closeTime.split(":");

        int openHour = Integer.parseInt(openSegments[0]);
        int closeHour = Integer.parseInt(closeSegments[0]);

        int openMinute = Integer.parseInt(openSegments[1]);
        int closeMinute = Integer.parseInt(closeSegments[1]);

        if (closeHour < openHour) {
            return false;
        } else if (closeHour == openHour && closeMinute < openMinute) {
            return false;
        }
        return true;
    }

    private static String getDateString(int year, int monthOfYear, int dayOfMonth) {

        // Increment monthOfYear for Calendar/Date -> Time Format setting
        ++monthOfYear;
        String mon = "" + monthOfYear;
        String day = "" + dayOfMonth;

        if (monthOfYear < 10)
            mon = "0" + monthOfYear;
        if (dayOfMonth < 10)
            day = "0" + dayOfMonth;

        return year + "-" + mon + "-" + day;
    }

    private static String getTimeString(int hourOfDay, int minute, int mili) {
        String hour = "" + hourOfDay;
        String min = "" + minute;

        if (hourOfDay < 10)
            hour = "0" + hourOfDay;
        if (minute < 10)
            min = "0" + minute;

        return hour + ":" + min + ":00";
    }

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the date of the event as default thing in clicker

            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            String dateString = getDateString(year, monthOfYear, dayOfMonth);

            mEventDateEdit.setText(dateString);

        }

    }

    public static class OpenTimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, minute, true);
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String timeString = getTimeString(hourOfDay, minute, 0);

            mTimeOpenEdit.setText(timeString);
        }
    }

    public static class CloseTimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, minute, true);
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String timeString = getTimeString(hourOfDay, minute, 0);

            mTimeClosedEdit.setText(timeString);

        }
    }

    private void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private void showOpenTimePickerDialog() {
        DialogFragment newFragment = new OpenTimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    private void showClosedTimePickerDialog() {
        DialogFragment newFragment = new CloseTimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

}
