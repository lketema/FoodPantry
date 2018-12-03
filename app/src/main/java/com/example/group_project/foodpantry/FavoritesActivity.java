package com.example.group_project.foodpantry;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends ListActivity {

    final private String TAG = "FavList";

    private RegistrationListAdapter mAdapter;

    private String userID;
    private User user;

    private List<Registration> favedThings = new ArrayList<>();
    private List<String> ownedIDs;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userID = (String)getIntent().getExtras().get("userID");

        getUserRegistrations();

        mAdapter = new RegistrationListAdapter(favedThings, getApplicationContext());
        getListView().setAdapter(mAdapter);

        getListView().setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(TAG, "short click");
//              String regId = ownedIDs.get(i);
//
//                Intent intent = new Intent(FavoritesActivity.this, RegistrationInfo.class);
//
//                intent.putExtra("registrationID", regId);
//                intent.putExtra("userID", userID);
//                intent.putExtra("return", "FavoritesActivity");
//
//                startActivity(intent);
            }
        });

        getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int pos, long id) {

                final String regId = ownedIDs.get(pos);
                    new AlertDialog.Builder(FavoritesActivity.this)
                            .setTitle("Remove Favorite")
                            .setMessage("Would you like to remove this from your Favorites?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    deleteFav(regId, pos);
                                }})
                            .setNegativeButton(android.R.string.no, null).show();


                return true;
            }
        });

    }


    private void getUserRegistrations() {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        database.child("users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                FavoritesActivity.this.ownedIDs = user.getFavorites();

                for (String pid : ownedIDs) {
                    addRegistrationInfo(pid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Cancelled",Toast.LENGTH_LONG).show();
                Log.e("AddEventActivity", databaseError.toString());
            }
        });
    }

    private void deleteFav(final String regID, final int pos) {
        //Steps:
        // 1. Delete from user registration list
        // 2. Delete from registrations

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        database.child("users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                user.removeFavorite(regID);

                database.child("users").child(userID).setValue(user);

                ownedIDs.remove(regID);
                mAdapter.remove(pos);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Cancelled",Toast.LENGTH_LONG).show();
                Log.e("AddEventActivity", databaseError.toString());
            }
        });
    }

    private void addRegistrationInfo(final String pid) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        database.child("registration").child(pid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("daysOpen")) {
                    Pantry temp = dataSnapshot.getValue(Pantry.class);
                    FavoritesActivity.this.favedThings.add(temp);
                } else {
                    Event temp = dataSnapshot.getValue(Event.class);
                    FavoritesActivity.this.favedThings.add(temp);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Cancelled",Toast.LENGTH_LONG).show();
                Log.e("AddEventActivity", databaseError.toString());
            }
        });
    }
}
