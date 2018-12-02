package com.example.group_project.foodpantry;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

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

        mAuth = FirebaseAuth.getInstance();
        Button resetBtn = (Button) findViewById(R.id.buttonResetPassword);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = EditTextEmail.getText().toString().trim();
                Log.i(TAG, "emai: " +email);
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ForgotPassword.this, " Password reset email has been sent!",
                                    Toast.LENGTH_SHORT).show();
                            Intent logIntent = new Intent(ForgotPassword.this, Login.class);
                            startActivity(logIntent);
                        } else {
                            Toast.makeText(ForgotPassword.this, "Unable to Find Account with given email. ",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
}