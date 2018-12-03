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
import android.view.KeyEvent;
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

public class RegistrationInfo extends AppCompatActivity {

    private TextView mPrivileges, mName, mAddress, mPhoneNumber, mEmail, mWebsite, mTimeOpenClose, mEventDate;
    private EditText mNameEdit, mAddressEdit, mPhoneNumberEdit, mEmailEdit, mWebsiteEdit;
    private static EditText mTimeOpenEdit, mTimeClosedEdit, mEventDateEdit;
    private Button mWebsiteButton, mDirectionsButton;
    private CheckBox mFavorite, mSun, mMon, mTue, mWed, mThu, mFri, mSat;

    private Registration registration;
    private boolean isPantry = false;
    private User user;

    private static DatabaseReference database;

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

        mNameEdit = (EditText) findViewById(R.id.pRegistrationName);
        mAddressEdit = (EditText) findViewById(R.id.pRegistrationAddress);
        mPhoneNumberEdit = (EditText) findViewById(R.id.pRegistrationPhoneNumber);
        mEmailEdit = (EditText) findViewById(R.id.pRegistrationEmail);
        mWebsiteEdit = (EditText) findViewById(R.id.pRegistrationPhoneNumber);
        mTimeOpenEdit = (EditText) findViewById(R.id.pChangeOpenTime);
        mTimeClosedEdit = (EditText) findViewById(R.id.pChangeClosedTime);
        mEventDateEdit = (EditText) findViewById(R.id.pEventDate);

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
        final String registrationID = //"-LSkjneBfQkmH5uRnctE",
                "-LShP2YO09BOj_rW3Z4n",
                //intent.getStringExtra("registrationID"),
        userID = "1yfb4cmbjeZf85VEfhphObkkoVg1";
                //        intent.getStringExtra("userID");

        // access to database for registration
        DatabaseReference child = database.child("registration").child(registrationID);

        child.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild("daysOpen")) {
                    isPantry = true;

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
                    mEventDateEdit.setVisibility(View.GONE);

                } else {
                    registration = (Event) dataSnapshot.getValue(Event.class);

                    //set pantry views to be invisible

                    mEventDate.setText(((Event) registration).getEventDateForDisplay());
                    mEventDateEdit.setText(((Event) registration).getEventDateForDisplay());
                    RelativeLayout pantryLayout = (RelativeLayout) findViewById(R.id.pantry_layout);
                    pantryLayout.setVisibility(View.GONE);

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
                user = dataSnapshot.getValue(User.class);
                //see if the user has this place as a favorite
                //if so, check the favorites box
                if (user.getFavorites().contains(registrationID)) mFavorite.setChecked(true);

                //If user is the current registration owner,
                // set the privilege of editing the stuff
                if (user.getUserType().equals("owner")
                        && dataSnapshot.hasChild("registrations")
                        && ((PantryOwner) user).ownsRegistration(registrationID)) {


                   // set non-editable views invisible and allow owner to go to town
                   mPrivileges.setText("Hi, owner! You can edit your registration information here");
                   mName.setVisibility(View.GONE);
                   mAddress.setVisibility(View.GONE);
                   mPhoneNumber.setVisibility(View.GONE);
                   mEmail.setVisibility(View.GONE);
                   mWebsite.setVisibility(View.GONE);
                   mTimeOpenClose.setVisibility(View.GONE);
                   mEventDate.setVisibility(View.GONE);
                   mSun.setClickable(true);
                   mMon.setClickable(true);
                   mTue.setClickable(true);
                   mWed.setClickable(true);
                   mThu.setClickable(true);
                   mFri.setClickable(true);
                   mSat.setClickable(true);

                } else {
                    //make all editable views invisible
                    mPrivileges.setVisibility(View.GONE);
                    mNameEdit.setVisibility(View.GONE);
                    mAddressEdit.setVisibility(View.GONE);
                    mPhoneNumberEdit.setVisibility(View.GONE);
                    mEmailEdit.setVisibility(View.GONE);
                    mWebsiteEdit.setVisibility(View.GONE);
                    RelativeLayout rl = (RelativeLayout) findViewById(R.id.pChangeTimeOpenAndClose);
                    rl.setVisibility(View.GONE);
                    mEventDateEdit.setVisibility(View.GONE);
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

        //add the appropriate listeners for updating database for pantry owner
        mNameEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    if (registration != null) {
                        registration.setName(((EditText) view).getText().toString());
                        database.child("registration").child(registrationID).setValue(registration);
                    }
                }
            }
        });
        mAddressEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    if (registration != null) {
                        registration.setAddress(((EditText) view).getText().toString());
                        database.child("registration").child(registrationID).setValue(registration);
                    }
                }
            }
        });
        mPhoneNumberEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    if (registration != null) {
                        registration.setPhoneNumber(((EditText) view).getText().toString());
                        database.child("registration").child(registrationID).setValue(registration);
                    }
                }
            }
        });
        mEmailEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    if (registration != null) {
                        registration.setEmailAddress(((EditText) view).getText().toString());
                        database.child("registration").child(registrationID).setValue(registration);
                    }
                }
            }
        });
        mWebsiteEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    if (registration != null) {
                        registration.setWebsite(((EditText) view).getText().toString());
                        database.child("registration").child(registrationID).setValue(registration);
                    }
                }
            }
        });

        mTimeOpenEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((EditText) view).setFocusable(true);
                showOpenTimePickerDialog();
                ((EditText) view).requestFocus();
                ((EditText) view).setFocusable(false);
            }
        });

        mTimeOpenEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus && registration != null) {
                    registration.setTimeOpen(((EditText) view).getText().toString());
                    database.child("registration").child(registrationID).setValue(registration);
                }
            }
        });

        mTimeClosedEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((EditText) view).setFocusable(true);
                showClosedTimePickerDialog();
                ((EditText) view).requestFocus();
                ((EditText) view).setFocusable(false);

            }
        });

        mTimeClosedEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus && registration != null) {
                    registration.setTimeClosed(((EditText) view).getText().toString());
                    database.child("registration").child(registrationID).setValue(registration);
                }
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
                            database.child("registration").child(registrationID).setValue(registration);
                        } else if (!isChecked && ((Pantry) registration).isOpenOn(Pantry.SUNDAY)) {
                            ((Pantry) registration).setDayOperational(Pantry.SUNDAY, false);
                            database.child("registration").child(registrationID).setValue(registration);
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
                            database.child("registration").child(registrationID).setValue(registration);
                        } else if (!isChecked && ((Pantry) registration).isOpenOn(Pantry.MONDAY)) {
                            ((Pantry) registration).setDayOperational(Pantry.MONDAY, false);
                            database.child("registration").child(registrationID).setValue(registration);
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
                            database.child("registration").child(registrationID).setValue(registration);
                        } else if (!isChecked && ((Pantry) registration).isOpenOn(Pantry.TUESDAY)) {
                            ((Pantry) registration).setDayOperational(Pantry.TUESDAY, false);
                            database.child("registration").child(registrationID).setValue(registration);
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
                            database.child("registration").child(registrationID).setValue(registration);
                        } else if (!isChecked && ((Pantry) registration).isOpenOn(Pantry.WEDNESDAY)) {
                            ((Pantry) registration).setDayOperational(Pantry.WEDNESDAY, false);
                            database.child("registration").child(registrationID).setValue(registration);
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
                            database.child("registration").child(registrationID).setValue(registration);
                        } else if (!isChecked && ((Pantry) registration).isOpenOn(Pantry.THURSDAY)) {
                            ((Pantry) registration).setDayOperational(Pantry.THURSDAY, false);
                            database.child("registration").child(registrationID).setValue(registration);
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
                            database.child("registration").child(registrationID).setValue(registration);
                        } else if (!isChecked && ((Pantry) registration).isOpenOn(Pantry.FRIDAY)) {
                            ((Pantry) registration).setDayOperational(Pantry.FRIDAY, false);
                            database.child("registration").child(registrationID).setValue(registration);
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
                            database.child("registration").child(registrationID).setValue(registration);
                        } else if (!isChecked && ((Pantry) registration).isOpenOn(Pantry.SATURDAY)) {
                            ((Pantry) registration).setDayOperational(Pantry.SATURDAY, false);
                            database.child("registration").child(registrationID).setValue(registration);
                        }
                    }
                }
            });
        } else {
            //if event, allow creator to edit the date event
            mEventDateEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((EditText) view).setFocusable(true);
                    showDatePickerDialog();;
                    ((EditText) view).requestFocus();
                    ((EditText) view).setFocusable(false);

                }
            });

            mEventDateEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (hasFocus && registration != null) {
                        ((Event) registration).setEventDate(((EditText) view).getText().toString());
                        database.child("registration").child(registrationID).setValue(registration);
                    }
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

            //use event time as default

            // Create a new instance of TimePickerDialog and return
            return null;//new TimePickerDialog(getActivity(), this, hour, minute, true);
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

            // Create a new instance of TimePickerDialog and return
            return null;// new TimePickerDialog(getActivity(), this, hour, minute, true);
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
