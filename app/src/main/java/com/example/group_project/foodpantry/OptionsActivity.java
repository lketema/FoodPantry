package com.example.group_project.foodpantry;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class OptionsActivity extends Activity{

    Button favButton;
    Button histButton;
    Button addEventButton;


    /**
     * Allows the user to access a variety of options including:
     * Access their list of favorites
     * View recently examined pantries
     * Create a new event as a pantry and add it to the map
     **/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_main.xml
        setContentView(R.layout.activity_options);

        // Locate the button in activity_main.xml
        favButton = (Button) findViewById(R.id.FavoritesButton);

        // Capture button clicks
        favButton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent = new Intent(OptionsActivity.this, FavoritesActivity.class);
                startActivity(myIntent);
            }
        });

        // Locate the button in activity_main.xml
        histButton = (Button) findViewById(R.id.HistoryButton);

        // Capture button clicks
        histButton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent = new Intent(OptionsActivity.this, HistoryActivity.class);
                startActivity(myIntent);
            }
        });

        // Locate the button in activity_main.xml
        addEventButton = (Button) findViewById(R.id.AddEventButton);

        // Capture button clicks
        addEventButton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent = new Intent(OptionsActivity.this, AddEventActivity.class);
                startActivity(myIntent);
            }
        });


    }


}
