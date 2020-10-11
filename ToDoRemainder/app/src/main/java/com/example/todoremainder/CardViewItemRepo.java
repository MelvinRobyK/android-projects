package com.example.todoremainder;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

public class CardViewItemRepo {
    private CardViewItemDao cardViewItemDao;
    private LiveData<List<CardViewItem>> arrayListLiveData;

    public CardViewItemRepo(Application application){
        CardViewItemDatabase database = CardViewItemDatabase.getInstance(application);
        cardViewItemDao = database.cardViewItemDao();

    }

    public void insert(CardViewItem item){
        new InsertItemAsyncTask(cardViewItemDao).execute(item);
    }

    public void update(CardViewItem item){
        new UpdateItemAsyncTask(cardViewItemDao).execute(item);
    }
    public void delete(CardViewItem item){
        new DeleteItemAsyncTask(cardViewItemDao).execute(item);
    }
    public LiveData<List<CardViewItem>> getAll(){
        return arrayListLiveData;
    }

    public LiveData<List<CardViewItem>> getAll(String uid) {
        arrayListLiveData = cardViewItemDao.getAll(uid);
        return arrayListLiveData;
    }


    private static class InsertItemAsyncTask extends AsyncTask<CardViewItem, Void, Void> {
        private CardViewItemDao dao;
        private InsertItemAsyncTask(CardViewItemDao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(CardViewItem... cardViewItems) {
            dao.insert(cardViewItems[0]);
            return null;
        }
    }

    private static class UpdateItemAsyncTask extends AsyncTask<CardViewItem, Void, Void> {
        private CardViewItemDao dao;
        private UpdateItemAsyncTask(CardViewItemDao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(CardViewItem... cardViewItems) {
            dao.update(cardViewItems[0]);
            return null;
        }
    }

    private static class DeleteItemAsyncTask extends AsyncTask<CardViewItem, Void, Void> {
        private CardViewItemDao dao;
        private DeleteItemAsyncTask(CardViewItemDao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(CardViewItem... cardViewItems) {
            dao.delete(cardViewItems[0]);
            return null;
        }
    }
}
