package com.example.todoremainder;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface CardViewItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CardViewItem item);

    @Update
    void update(CardViewItem note);

    @Delete
    void delete(CardViewItem note);

    @Query("SELECT * from ToDo_Item where uid = :uid")
    LiveData<List<CardViewItem>> getAll(String uid);

}
