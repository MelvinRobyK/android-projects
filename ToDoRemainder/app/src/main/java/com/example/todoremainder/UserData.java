package com.example.todoremainder;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserData {

    @PrimaryKey
    @NonNull
    private String uid;
    private String username;
    private String password;


    public UserData(@NonNull String uid, String username, String password) {
        this.uid = uid;
        this.username = username;
        this.password = password;
    }

    public UserData(){

    }

    @NonNull
    public String getUid() {
        return uid;
    }

    public void setUid(@NonNull String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
