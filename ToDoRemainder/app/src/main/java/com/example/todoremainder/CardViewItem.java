package com.example.todoremainder;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ToDo_Item")
public class CardViewItem {

    @PrimaryKey
    @NonNull
    private String id;
    private String uid;
    private boolean checkBox;
    private String title;
    private String date;
    private String time;

    public CardViewItem(String id,String uid, boolean checkBox, String title, String date, String time) {

        this.id = id;
        this.uid = uid;
        this.checkBox = checkBox;
        this.title = title;
        this.date = date;
        this.time = time;
    }

    public CardViewItem() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCheckBox(boolean checkBox) {
        this.checkBox = checkBox;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public boolean isCheckBox() {
        return checkBox;
    }

    public String getTitle() {
        return title;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}