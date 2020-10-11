package com.example.todoremainder;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

public class CardViewItemViewModel extends AndroidViewModel {
    private CardViewItemRepo repo;
    private LiveData<List<CardViewItem>> arrayListLiveData;

    public CardViewItemViewModel(@NonNull Application application) {
        super(application);
        repo = new CardViewItemRepo(application);
    }

    public void insert(CardViewItem item) {
        repo.insert(item);
    }
    public void update(CardViewItem item) {
        repo.update(item);
    }
    public void delete(CardViewItem item) {
        repo.delete(item);
    }
    public LiveData<List<CardViewItem>> getAll(String uid){
        arrayListLiveData = repo.getAll(uid);
        return arrayListLiveData;
    }
}
