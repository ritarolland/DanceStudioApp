package com.example.dancestudioapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class User implements Parcelable {
    public String userEmail, userPassword, userName, id, role;
    private ArrayList<String> events;

    public User(){};

    public User(String id, String userEmail, String userPassword, String userName, String role) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userName = userName;
        this.id = id;
        this.role = role;
    }

    protected User(Parcel in) {
        userEmail = in.readString();
        userPassword = in.readString();
        userName = in.readString();
        id = in.readString();
        role = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userEmail);
        dest.writeString(userPassword);
        dest.writeString(userName);
        dest.writeString(id);
        dest.writeString(role);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
