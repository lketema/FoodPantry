package com.example.group_project.foodpantry;

import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public abstract class User {

    private String name, email, hashword;

    private int salt;

    Random random = new Random();

    protected User(String name, String email, String password) {
        this.name = name;
        this.email = email;

        //never store plaintext of password
        //concatenate salt to plaintext and hash
        //that will be stored in database
        this.salt = random.nextInt();
        String pwSalt = password + ((Integer) salt).toString();
        this.hashword = this.hashString(pwSalt);
    }

    protected String hashString(String string) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {}
        //should never hit this exception

        byte[] input = string.getBytes(StandardCharsets.UTF_8);
        byte[] hashBytes = md.digest(input);
        return Base64.encodeToString(hashBytes, 0);
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getHashword() {
        return this.hashword;
    }


    //does each user have favorites?


}
