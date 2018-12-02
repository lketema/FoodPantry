package com.example.group_project.foodpantry;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import android.support.annotation.NonNull;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import java.util.List;
import java.util.ArrayList;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;


public class AddEventActivity extends AppCompatActivity {

    private Button submitButton;

    //for sending to firebase
    private EditText nameEditText;
    private EditText locationEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private EditText websiteEditText;
    private RadioGroup typeGroup;
    private Button chooseOpenButton;
    private Button chooseCloseButton;
    private static TextView selectedOpenTime;
    private static TextView selectedCloseTime;
    private TextView titleTextView;

    private LinearLayout eventLayout;
    private Button chooseDateButton;
    private static TextView selectedEventDate;

    private RelativeLayout pantryLayout;
    private CheckBox[] dayCheckboxes;

    private static boolean openTimeSet = false;
    private static boolean closeTimeSet = false;
    private static boolean dateSet = false;
    private static boolean isEvent = false;

    private String userID = null;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        //register all layout elements
        titleTextView = findViewById(R.id.TitleLabel);

        submitButton = findViewById(R.id.submit_button);
        nameEditText = findViewById(R.id.name_event_input);
        locationEditText = findViewById(R.id.location_event_input);
        phoneEditText = findViewById(R.id.phone_event_input);
        emailEditText = findViewById(R.id.email_event_input);
        websiteEditText = findViewById(R.id.website_event_input);

        typeGroup = findViewById(R.id.register_type_group);

        chooseOpenButton = findViewById(R.id.open_time_picker_button);
        chooseCloseButton = findViewById(R.id.close_time_picker_button);
        selectedOpenTime = findViewById(R.id.open_time_selected);
        selectedCloseTime = findViewById(R.id.close_time_selected);

        eventLayout = findViewById(R.id.event_layout);
        chooseDateButton = findViewById(R.id.event_date_picker_button);
        selectedEventDate = findViewById(R.id.event_date_selected);

        pantryLayout = findViewById(R.id.pantry_layout);
        dayCheckboxes = new CheckBox[7];
        dayCheckboxes[0] = findViewById(R.id.checkbox_sun);
        dayCheckboxes[1] = findViewById(R.id.checkbox_mon);
        dayCheckboxes[2] = findViewById(R.id.checkbox_tue);
        dayCheckboxes[3] = findViewById(R.id.checkbox_wed);
        dayCheckboxes[4] = findViewById(R.id.checkbox_thu);
        dayCheckboxes[5] = findViewById(R.id.checkbox_fri);
        dayCheckboxes[6] = findViewById(R.id.checkbox_sat);

        Intent data = getIntent();
        userID = data.getExtras().getString("userID");

        submitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                submitEvent();
            }
        });

        typeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Log.d("chk", "id" + i);

                if (i == R.id.is_event) {
                    isEvent = true;
                    isEventLayout();
                } else {
                    isEvent = false;
                    isPantryLayout();
                }
            }
        });

        chooseOpenButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(0);
            }
        });

        chooseCloseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(1);
            }
        });

        chooseDateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
    }

    private void isEventLayout() {
        pantryLayout.setVisibility(View.GONE);
        eventLayout.setVisibility(View.VISIBLE);
        submitButton.setText(R.string.submit_register_event);
        titleTextView.setText(R.string.title_register_event);
        nameEditText.setHint(R.string.register_event_name);
    }

    private void isPantryLayout() {
        pantryLayout.setVisibility(View.VISIBLE);
        eventLayout.setVisibility(View.GONE);
        submitButton.setText(R.string.submit_register_pantry);
        titleTextView.setText(R.string.title_register_pantry);
        nameEditText.setHint(R.string.register_pantry_name);
    }

    /////////////////////////////
    /// Submit Event Handling ///
    /////////////////////////////

    private void submitEvent() {
        //check validity
        if (isEmpty(nameEditText) || isEmpty(locationEditText) || isEmpty(phoneEditText) || isEmpty(emailEditText)
                || isEmpty(websiteEditText)) {
            Toast.makeText(this, "Please fill out all required fields", Toast.LENGTH_LONG).show();
            return;
        }
        if (isInvalidEventOrPantry()) {
            Toast.makeText(this, "Please set all dates and times", Toast.LENGTH_LONG).show();
            return;
        }
        if (isInvalidTimeOrDate()) {
            Toast.makeText(this, "Invalid date or time", Toast.LENGTH_LONG).show();
            return;
        }

        //get confirmation
        if (isEvent) {
            new AlertDialog.Builder(this)
                    .setTitle("Submit Event")
                    .setMessage("Register this event?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            sendToFireBase();
                        }})
                    .setNegativeButton(android.R.string.no, null).show();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Submit Pantry")
                    .setMessage("Register this pantry?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            sendToFireBase();
                        }})
                    .setNegativeButton(android.R.string.no, null).show();
        }

    }

    private void sendToFireBase() {
        //send to Firebase
        //start next event
        if (isEvent) {
            Event e = new Event(nameEditText.getText().toString(), locationEditText.getText().toString(),
                    phoneEditText.getText().toString(), emailEditText.getText().toString(),
                    websiteEditText.getText().toString(), selectedEventDate.getText().toString(),
                    selectedOpenTime.getText().toString(), selectedCloseTime.getText().toString());

            final DatabaseReference regRef = FirebaseDatabase
                    .getInstance()
                    .getReference("registration");

            regRef.push().setValue(e, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError,
                                       DatabaseReference databaseReference) {
                    String key = databaseReference.getKey();
                    addToUserTable(key);
                }
            });
        } else {
            List<Boolean> daysOpen = new ArrayList<Boolean>(7);

            for (int i = 0 ; i < dayCheckboxes.length; i++) {
                daysOpen.add(dayCheckboxes[i].isChecked());
            }

            Pantry p = new Pantry(nameEditText.getText().toString(), locationEditText.getText().toString(),
                    phoneEditText.getText().toString(), emailEditText.getText().toString(),
                    websiteEditText.getText().toString(), selectedOpenTime.getText().toString(),
                    selectedCloseTime.getText().toString(), daysOpen);

            final DatabaseReference regRef = FirebaseDatabase
                    .getInstance()
                    .getReference("registration");

            regRef.push().setValue(p, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError,
                                       DatabaseReference databaseReference) {
                    String key = databaseReference.getKey();
                    addToUserTable(key);
                }
            });

        }

    }

    private void addToUserTable(final String key) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        database.child("users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);

                user.addRegistration(key);

                database.child("users").child(userID).setValue(user);

                startConfirmationActivity();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Cancelled",Toast.LENGTH_LONG).show();
                Log.e("AddEventActivity", databaseError.toString());
            }
        });

    }

    private void startConfirmationActivity() {
        Intent conf = new Intent(AddEventActivity.this, EventConfirmationActivity.class);

        conf.putExtra("userID", userID);
        conf.putExtra("website", websiteEditText.getText().toString());
        conf.putExtra("isEvent", isEvent);

        startActivity(conf);
    }

    private boolean isEmpty (EditText v) {
        return v.getText().toString().matches("");
    }

    private boolean isInvalidEventOrPantry() {
        if (isEvent) {
            //event
            return !(openTimeSet && closeTimeSet && dateSet);
        } else {
            return !(atLeastOneChecked() && openTimeSet && closeTimeSet);
        }
    }

    private boolean isInvalidTimeOrDate() {
        boolean invalid = false;
        if (isEvent) {
            String dateStr = selectedEventDate.getText().toString();
            //yyyy-MM-dd
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date setDate = format.parse(dateStr);
                Date curDate = new Date();

                if (!isInFuture(setDate, curDate)) {
                    invalid = true;
                }
            } catch (ParseException e) {
                Log.e("AddEventActivity", "Invalid date parsed");
                invalid = true;
            }

        }
        //hh:mm:ss
        if (!invalid) {
            String openTime = selectedOpenTime.getText().toString();
            String closeTime = selectedCloseTime.getText().toString();

            if (!closeAfterOpen(openTime,closeTime)) {
                invalid = true;
            }
        }

        return invalid;
    }

    private boolean atLeastOneChecked() {
        boolean found = false;
        for (int i = 0; i < dayCheckboxes.length; i++) {
            if (dayCheckboxes[i].isChecked()) {
                found = true;
            }
        }
        return found;
    }

    private boolean isInFuture(Date setDate, Date curDate) {
        return setDate.after(curDate);
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

    //////////////////////////////////////
    /// Date and Time Picker Functions ///
    //////////////////////////////////////

    // used for choosing and displaying date and times

    private static String setDateString(int year, int monthOfYear, int dayOfMonth) {

        // Increment monthOfYear for Calendar/Date -> Time Format setting
        monthOfYear++;
        String mon = "" + monthOfYear;
        String day = "" + dayOfMonth;

        if (monthOfYear < 10)
            mon = "0" + monthOfYear;
        if (dayOfMonth < 10)
            day = "0" + dayOfMonth;

        return year + "-" + mon + "-" + day;
    }

    private static String setTimeString(int hourOfDay, int minute, int mili) {
        String hour = "" + hourOfDay;
        String min = "" + minute;

        if (hourOfDay < 10)
            hour = "0" + hourOfDay;
        if (minute < 10)
            min = "0" + minute;

        return hour + ":" + min + ":00";
    }



    // DialogFragment used to pick a ToDoItem deadline date

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current date as the default date in the picker

            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            String dateString = setDateString(year, monthOfYear, dayOfMonth);

            dateSet = true;
            selectedEventDate.setText(dateString);
        }

    }

    // DialogFragment used to pick a time


    public static class OpenTimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return
            return new TimePickerDialog(getActivity(), this, hour, minute, true);
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String timeString = setTimeString(hourOfDay, minute, 0);

            openTimeSet = true;
            selectedOpenTime.setText(timeString);
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

            // Create a new instance of TimePickerDialog and return
            return new TimePickerDialog(getActivity(), this, hour, minute, true);
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String timeString = setTimeString(hourOfDay, minute, 0);

            selectedCloseTime.setText(timeString);
            closeTimeSet = true;
        }
    }

    private void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private void showTimePickerDialog(int i) {
        if (i == 0) {
            DialogFragment newFragment = new OpenTimePickerFragment();
            newFragment.show(getFragmentManager(), "timePicker");
        } else {
            DialogFragment newFragment = new CloseTimePickerFragment();
            newFragment.show(getFragmentManager(), "timePicker");
        }
    }


}
