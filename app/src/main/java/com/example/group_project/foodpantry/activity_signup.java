package com.example.group_project.foodpantry;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class activity_signup extends AppCompatActivity {

//    String fullname="",emailAddr="", password="";

    EditText fullname,emailAddr, password;

    String name="", email="", paswd="";



    String TAG = "Sign_IN: ";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        TextView textViewLogin = (TextView) findViewById(R.id.textViewLogin);
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent LoginIntent = new Intent(activity_signup.this, MainActivity.class);
                startActivity(LoginIntent);
            }
        });

//        fullname = findViewById(R.id.editTextName).toString().trim();
//        emailAddr = findViewById(R.id.editTextEmail).toString().trim();
//        password = findViewById(R.id.editTextPassword).toString().trim();

//        name = (EditText) findViewById(R.id.editTextName);

        fullname = (EditText) findViewById(R.id.editTextName);
        emailAddr = (EditText) findViewById(R.id.editTextEmail);
        password = (EditText) findViewById(R.id.editTextPassword);




        mAuth = FirebaseAuth.getInstance();
        Button registerBtn = (Button) findViewById(R.id.buttonRegister);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "About to push Button Method");
                name = fullname.getText().toString().trim();
                paswd = password.getText().toString().trim();
                email = emailAddr.getText().toString().trim();

                Log.i(TAG, "NAME: "+name);
                Log.i(TAG, "EMAIL: " +email);
                Log.i(TAG,"Password: " +paswd);
                pushRegisterButton();
            }
        });
    }

    private boolean registerUser(){


        if (name.length() == 0){
            TextView errMsg = (TextView) findViewById(R.id.textViewError);
            errMsg.setText("Name must not be empty");
            findViewById(R.id.editTextName).requestFocus();
            return false;
        }
        if (email.length() == 0){
            TextView errMsg = (TextView) findViewById(R.id.textViewError);
            errMsg.setText("Email must not be empty");
            findViewById(R.id.editTextEmail).requestFocus();
            return false;

        }
        if (paswd.length() == 0){
            TextView errMsg = (TextView) findViewById(R.id.textViewError);
            errMsg.setText("Password must not be empty");
            findViewById(R.id.editTextPassword).requestFocus();
            return false;
        }

        return true;
    }

    public void pushRegisterButton(){
        boolean s = registerUser();
        Log.i(TAG, "VALID: " +s);
        if(s==true){

            Log.i(TAG, "PUSH BUTTON");
            Log.i(TAG, "NAME: "+name);
            Log.i(TAG, "EMAIL: " +email);
            Log.i(TAG,"Password: " +paswd);



            mAuth.createUserWithEmailAndPassword(email, paswd)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                Toast.makeText(getApplicationContext(), "User is registered", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getApplicationContext(), "User Not registered", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
        }

    }



}
