package com.example.group_project.foodpantry;

import android.app.ListActivity;
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

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(TAG, adapterView.getClass().toString());
                Log.i(TAG, view.getClass().toString());
            }
        });

    }

    private void getUserRegistrations() {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        database.child("users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG, "here");
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
