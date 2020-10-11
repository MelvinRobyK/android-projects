package com.example.todoremainder;

import android.app.Application;
import android.os.AsyncTask;

public class UserDataRepo {
    private UserDataDao userDataDao;

    public UserDataRepo(Application application){
        CardViewItemDatabase database = CardViewItemDatabase.getInstance(application);
        userDataDao = database.userDataDao();
    }

    public void insert(UserData item){
        new InsertItemAsyncTask(userDataDao).execute(item);
    }

    private static class InsertItemAsyncTask extends AsyncTask<UserData, Void, Void> {
        private UserDataDao dao;
        private InsertItemAsyncTask(UserDataDao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(UserData... userData) {
            dao.insert(userData[0]);
            return null;
        }
    }

    public UserData getUser(String username,String password){
        final UserData[] data = new UserData[1];
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                data[0] = userDataDao.getUser(username,password);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return data[0];
    }
}
