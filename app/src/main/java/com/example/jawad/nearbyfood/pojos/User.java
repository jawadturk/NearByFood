package com.example.jawad.nearbyfood.pojos;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;


@IgnoreExtraProperties
public class User {

    public String username;
    public String email;
    public String name;
    public String gcm;
    public String profilePicture = "";

    public User(String gcm) {
        this.gcm = gcm;
    }



    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, String name, String gcm) {
        this.username = username;
        this.email = email;
        this.name=name;
        this.gcm = gcm;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("gcm", gcm);
        return result;
    }

}
// [END blog_user_class]
