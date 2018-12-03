package com.example.group_project.foodpantry;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

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

public class RegistrationListActivity extends ListActivity {

    final private String TAG = "RegList";

    private RegistrationListAdapter mAdapter;

    private String userID;
    private User user;

    private List<Registration> ownedThings = new ArrayList<>();
    private List<String> ownedIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userID = (String)getIntent().getExtras().get("userID");

        getUserRegistrations();

        mAdapter = new RegistrationListAdapter(ownedThings, getApplicationContext());
        getListView().setAdapter(mAdapter);

        getListView().setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(TAG, "short click");
                String regId = ownedIDs.get(i);

                Intent intent = new Intent(RegistrationListActivity.this, RegistrationInfo.class);

                intent.putExtra("registrationID", regId);
                intent.putExtra("userID", userID);

                startActivity(intent);
            }
        });

        getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int pos, long id) {

                final String regId = ownedIDs.get(pos);

                if (mAdapter.getItem(pos) instanceof Event) {
                    new AlertDialog.Builder(RegistrationListActivity.this)
                            .setTitle("Delete Event")
                            .setMessage("Would you like to delete this event?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    deleteReg(regId, pos);
                                }})
                            .setNegativeButton(android.R.string.no, null).show();
                } else {
                    new AlertDialog.Builder(RegistrationListActivity.this)
                            .setTitle("Delete Pantry")
                            .setMessage("Would you like to delete this pantry?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    deleteReg(regId, pos);
                                }})
                            .setNegativeButton(android.R.string.no, null).show();
                }

                return true;
            }
        });

    }

    private void deleteReg(final String regID, final int pos) {
        //Steps:
        // 1. Delete from user registration list
        // 2. Delete from registrations

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        database.child("users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                user.removeRegistration(regID);

                database.child("users").child(userID).setValue(user);

                database.child("registration").child(regID).removeValue();

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

    private void getUserRegistrations() {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        database.child("users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                RegistrationListActivity.this.ownedIDs = user.getRegistrations();

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

    private void addRegistrationInfo(final String pid) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        database.child("registration").child(pid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("daysOpen")) {
                    Pantry temp = dataSnapshot.getValue(Pantry.class);
                    RegistrationListActivity.this.ownedThings.add(temp);
                } else {
                    Event temp = dataSnapshot.getValue(Event.class);
                    RegistrationListActivity.this.ownedThings.add(temp);
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
