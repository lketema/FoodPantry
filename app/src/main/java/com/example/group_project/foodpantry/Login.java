package com.example.group_project.foodpantry;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Login extends AppCompatActivity {

    EditText EditTextLogInEmail, EditTextLogInPassword;
    String logInEmail="", logInpassword="";
    String TAG = "LOG_IN: ";
    TextView emailVerifyMsg;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditTextLogInEmail = (EditText) findViewById(R.id.editTextLogInEmail);
        EditTextLogInPassword = (EditText) findViewById(R.id.editTextLogInPassword);

        emailVerifyMsg = (TextView) findViewById(R.id.textViewVerificationMsg);
        emailVerifyMsg.setVisibility(TextView.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();

        Button registerBtn = (Button) findViewById(R.id.buttonLogin);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "setOnClickListener Push Login Button");
                logIn();
            }
        });


        TextView textViewSignUp = (TextView) findViewById(R.id.textViewSignUp);
        textViewSignUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Go to Sign Up Page");
                Intent LoginIntent = new Intent(Login.this, Signup.class);
                startActivity(LoginIntent);
            }
        });

        TextView textViewResetPswd = (TextView) findViewById(R.id.textViewForgotPassword);
        textViewResetPswd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent LoginIntent = new Intent(Login.this, ForgotPassword.class);
                startActivity(LoginIntent);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }

    public void logIn(){
        logInEmail = EditTextLogInEmail.getText().toString().trim();
        logInpassword = EditTextLogInPassword.getText().toString().trim();


        mAuth.signInWithEmailAndPassword(logInEmail, logInpassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        TextView errMsg = (TextView) findViewById(R.id.textViewLogInError);
                        errMsg.setText("");
                        emailVerifyMsg.setVisibility(TextView.INVISIBLE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();


                            //if the email is verified then it will take user to map activity
                            if (user.isEmailVerified()){
                                emailVerifyMsg.setVisibility(TextView.INVISIBLE);
                                String currentUID = user.getUid().toString();

                                Intent LoginIntent = new Intent(Login.this, MapsActivity.class);
                                LoginIntent.putExtra("userID", currentUID);
                                startActivity(LoginIntent);

                            } else {
                                emailVerifyMsg.setVisibility(TextView.VISIBLE);
                                user.sendEmailVerification();
                                Log.i(TAG, "Verification email is sent once again");
//                                errMsg.setText("Please Verify your email!");
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            errMsg.setText("Email and/or Password do not match ");
                            Toast.makeText(Login.this, "Invalid Email or Password.",
                                    Toast.LENGTH_LONG).show();
                            EditTextLogInPassword.setText("");
                        }

                        // ...
                    }
                });

    }
}
