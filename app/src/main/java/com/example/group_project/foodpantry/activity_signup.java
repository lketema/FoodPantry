package com.example.group_project.foodpantry;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class activity_signup extends AppCompatActivity {

//    String fullname="",emailAddr="", password="";


    EditText EditTextFullname,EditTextPhoneNumber, EditTextEmailAddr, EditTextPassword;

    enum UserType{
        OWNER("owner"), VOLUNTEER("volunteer"), RECIPIENT("recipient");

        private String userType;

        UserType(String usrType){
            this.userType = usrType;
        }

        public String getUserType(){
            return userType;
        }

    }
    String fullname="", phone="", email="", password="", userType = UserType.VOLUNTEER.getUserType();
    //**Note: default userType is Volunteer

    RadioButton radioButtonOwner, radioButtonVolunteer, radioButtonRecipient;
//    Boolean radBtnOwner = false, radBtnVolunteer = false, radBtnRecipient=false;



    String TAG = "Sign_UP: ";

    private FirebaseAuth mAuth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        database = FirebaseDatabase.getInstance();


        EditTextFullname = (EditText) findViewById(R.id.editTextName);
        EditTextPhoneNumber = (EditText) findViewById(R.id.editTextPhone);
        EditTextEmailAddr = (EditText) findViewById(R.id.editTextEmail);
        EditTextPassword = (EditText) findViewById(R.id.editTextPassword);

        radioButtonOwner = (RadioButton) findViewById(R.id.radioButtonOwner);
        radioButtonVolunteer = (RadioButton) findViewById(R.id.radioButtonVolunteer);
        radioButtonRecipient = (RadioButton) findViewById(R.id.radioButtonRcp);



        mAuth = FirebaseAuth.getInstance();
        Button registerBtn = (Button) findViewById(R.id.buttonRegister);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "setOnClickListener Push Register Button");
                registerUserEmail();
            }
        });


        //user clicks on this to get back to login activity
        TextView textViewLogin = (TextView) findViewById(R.id.textViewLogin);
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent LoginIntent = new Intent(activity_signup.this, Login.class);
                startActivity(LoginIntent);
            }
        });
    }



    //ignore this for now
//    public void onRadioButtonClicked(View view){
//
//        boolean checked = ((RadioButton) view).isChecked();
//
//        //check which radio button is checked
//        switch(view.getId()){
//            case R.id.radioButtonOwner:
//                if  (checked)
//                    radBtnOwner=true;
//                break;
//            case R.id.radioButtonVolunteer:
//                if  (checked)
//                    radBtnVolunteer=true;
//                break;
//            case R.id.radioButtonRcp:
//                if  (checked)
//                    radBtnRecipient=true;
//                break;
//        }
//
//
//
//    }

    private boolean validateTextFields(){
        TextView errMsg = (TextView) findViewById(R.id.textViewSignUpError);
        errMsg.setText("");

        if (fullname.length() == 0){
            errMsg.setText("Name Cannot be empty!");
            findViewById(R.id.editTextPhone).requestFocus();
            return false;
        }

        boolean isPhoneNumValid = phoneNumValidation(phone);
        if (isPhoneNumValid == false){
            errMsg.setText("Invalid Phone Number!");
            findViewById(R.id.editTextEmail).requestFocus();
            return false;
        }


        boolean isEmailAddrValid = emailValidation(email);
        if (isEmailAddrValid == false){
            errMsg.setText("Email must not be empty");
            findViewById(R.id.editTextEmail).requestFocus();
            return false;

        }
        if (password.length() == 0){
            errMsg.setText("Password must not be empty");
            findViewById(R.id.editTextPassword).requestFocus();
            return false;
        }

        Log.i(TAG, " SHAH OWNER: " +radioButtonOwner.isChecked());
        Log.i(TAG, " SHAH VOLUNTEER: " +radioButtonVolunteer.isChecked());
        Log.i(TAG, " SHAH RECPT: " +radioButtonRecipient.isChecked());
        if( (radioButtonOwner.isChecked() ==false) && (radioButtonVolunteer.isChecked() ==false)
                && (radioButtonRecipient.isChecked()==false)){

            errMsg.setText("Please choose your identity!");
            findViewById(R.id.radioButtonGroup).requestFocus();
            return false;
        }else{
            if (radioButtonOwner.isChecked()==true){
                userType = UserType.OWNER.getUserType();
            } else {
                if (radioButtonVolunteer.isChecked()==true){
                    userType = UserType.VOLUNTEER.getUserType();
                } else {
                    if (radioButtonRecipient.isChecked()==true) {
                        userType = UserType.RECIPIENT.getUserType();
                    } else {
                        errMsg.setText("Please choose your identity!");
                        findViewById(R.id.radioButtonGroup).requestFocus();
                        return false;
                    }
                }
            }

        }

        return true;
    }

    public void registerUserEmail(){
        fullname = EditTextFullname.getText().toString().trim();
        password = EditTextPassword.getText().toString().trim();
        email = EditTextEmailAddr.getText().toString().trim();
        phone = EditTextPhoneNumber.getText().toString().trim();

        boolean textFieldsvalidation = validateTextFields();
        if(textFieldsvalidation == true){

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                User user = new User(fullname, email, phone, userType);
                                //DB: Add  into main profile of all users
                                addProfileInDB(user, "users");

                                //DB: add users into different profiles based on userTypes
                                if(userType.equals(UserType.OWNER.getUserType())){
                                    addProfileInDB(user,"pantryOwners");
                                } else if(userType.equals(UserType.VOLUNTEER.getUserType())){
                                    addProfileInDB(user,"volunteers");
                                } else if(userType.equals(UserType.RECIPIENT.getUserType())){
                                    addProfileInDB(user,"recipients");
                                }






                            } else {
                                Toast.makeText(getApplicationContext(), "User Not registered", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
        }

    }

    private void addProfileInDB(User userObj, String rootName){
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        DatabaseReference currentUID = database.getReference(rootName)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        //after sucessfully Creating uID for firbase authentication
        //add UID and userInfo in DB under 'usernames' branch
        currentUID.setValue(userObj).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    currentUser.sendEmailVerification();
                    Toast.makeText(getApplicationContext(), "User is Successfully registered", Toast.LENGTH_SHORT).show();

                    EditTextFullname.setText("");
                    EditTextPhoneNumber.setText("");
                    EditTextEmailAddr.setText("");
                    EditTextPassword.setText("");
                }

            }
        });
    }

    private boolean emailValidation(String emailAddr){

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


    private boolean phoneNumValidation(String phoneNumber){

        boolean isValid = false;

        String format = "[a-zA-Z]+";

        if(Pattern.matches(format, phoneNumber)){ //if alphabats
            isValid =  false;
        } else {
            if( (phoneNumber.length() > 0) && (phoneNumber.length() < 11)){
                isValid = true;
            } else {
                isValid = false;
            }
        }

        return isValid;
    }




}
