package com.example.todoremainder;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {CardViewItem.class,UserData.class},version = 1)
public abstract class CardViewItemDatabase extends RoomDatabase {

    private static CardViewItemDatabase instance;

    public abstract CardViewItemDao cardViewItemDao();

    public abstract UserDataDao userDataDao();

    public static synchronized CardViewItemDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    CardViewItemDatabase.class,"ToDo Database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
