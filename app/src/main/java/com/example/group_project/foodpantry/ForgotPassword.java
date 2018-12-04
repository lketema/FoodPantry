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
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPassword extends AppCompatActivity {
    EditText EditTextEmail;
    String email="";
    String TAG = "RESET_PASSWORD: ";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        EditTextEmail = (EditText) findViewById(R.id.editTextResetPswdEmail);

        email = EditTextEmail.getText().toString().trim();
        mAuth = FirebaseAuth.getInstance();

        Button resetBtn = (Button) findViewById(R.id.buttonResetPassword);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = EditTextEmail.getText().toString().trim();
                Log.i(TAG, "emai: " + email);

                if (validateTextFields()==true) {
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgotPassword.this, "A password reset email has been sent!",
                                        Toast.LENGTH_LONG).show();
                                Intent logIntent = new Intent(ForgotPassword.this, Login.class);
                                startActivity(logIntent);
                            } else {
                                Toast.makeText(ForgotPassword.this, "Unable to Find Account with given email. ",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

            }
        });

        TextView backLogin = (TextView) findViewById(R.id.textViewBacktoLogin);
        backLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mloginIntent = new Intent(ForgotPassword.this, Login.class);
                startActivity(mloginIntent);
            }
        });

    }

    private boolean validateTextFields(){


        email = EditTextEmail.getText().toString().trim();
        boolean isEmailAddrValid = emailValidation(email);
        if (isEmailAddrValid == false){
            Toast.makeText(ForgotPassword.this, "Please provide a valid email.",
                    Toast.LENGTH_LONG).show();

            return false;

        }



        return true;
    }

    private boolean emailValidation(String emailAddr){

        //return true when email is valid
        boolean isValid = false;
        Pattern pattern;
        Matcher matcher;

        String emailFormat = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                +"[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        pattern = Pattern.compile(emailFormat);

        matcher = pattern.matcher(emailAddr);
        isValid = matcher.matches();

        if(isValid == true){
            return true;
        }

        return isValid;
    }

    public void onBackPressed() {
        Intent intent = new Intent(ForgotPassword.this,
                Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);

    }
}