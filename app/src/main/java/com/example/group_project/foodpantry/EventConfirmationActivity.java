package com.example.group_project.foodpantry;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.content.Intent;

import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.ClipboardManager;
import android.content.ClipData;

public class EventConfirmationActivity extends AppCompatActivity {

    private TextView titleText;
    private TextView successText;
    private Button shareButton;

    private boolean isEvent = false;
    private String website = "www.example.com";
    private String userID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_confirmation);

        titleText = findViewById(R.id.TitleLabel);
        successText = findViewById(R.id.success_label);
        shareButton = findViewById(R.id.share_button);

        isEvent = (boolean)getIntent().getExtras().get("isEvent");
        website = (String)getIntent().getExtras().get("website");
        userID = (String)getIntent().getExtras().get("userID");


        shareButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showShareDialog();
            }
        });
    }

    private void showShareDialog() {
        final String message = getShareMessage();
        new AlertDialog.Builder(this)
                .setTitle("Share Pantry:")
                .setMessage(message)
                .setNeutralButton(android.R.string.copy, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) { copyText(message); }
                    })
                .setNegativeButton(android.R.string.ok, null).show();

    }

    private void copyText(String msg) {
        ClipboardManager clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("shareText",msg);
        clipboard.setPrimaryClip(clip);
    }

    private String getShareMessage() {
        if (isEvent) {
            if (hasWebsite()) {
                return "I just created an event on FoodPantrySupport! Check it out on the app or visit" +
                        " our website at " + website + ".";
            } else {
                return "I just created an event on FoodPantrySupport! Check it out by downloading the app.";
            }
        } else {
            if (hasWebsite()) {
                return "I just created a pantry on FoodPantrySupport! Check it out on the app or visit" +
                        " our website at " + website + ".";
            } else {
                return "I just created a pantry on FoodPantrySupport! Check it out by downloading the app.";
            }
        }
    }

    private boolean hasWebsite() {
        return !website.equals("");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EventConfirmationActivity.this,
                MapsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);

    }
}
