package com.example.todoremainder;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface UserDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserData data);

    @Query("SELECT * from UserData where username = :username AND password = :password" )
    UserData getUser(String username,String password);
}
