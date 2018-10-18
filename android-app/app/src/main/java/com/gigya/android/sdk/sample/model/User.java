package com.gigya.android.sdk.sample.model;

public class User {

    private static final String TAG = "User";

    private String mUID;
    private String mFirstName;
    private String mLastName;

    public User(String firstName, String lastName, String uid){
        mFirstName = firstName;
        mLastName = lastName;
        mUID = uid;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getUID() {
        return mUID;
    }
}
