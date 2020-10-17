package com.example.todoremainder;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity" ;

    ArrayList<CardViewItem> arrayList = new ArrayList<>();
    DatabaseReference databaseReference;
    String title, date, time;
    AlarmManager alarmManager;
    private RecyclerView mRecyclerView;
    private CustomAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String uid;
    private FirebaseAuth mAuth;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private CardViewItem cardViewItem = null;
    private CardViewItemViewModel viewModel;

    SharedPreferences.Editor editor;

    FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            if(firebaseAuth.getCurrentUser() == null){
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Toast.makeText(getApplicationContext(),"Logged Out Successfully",Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToDoItem();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(authStateListener);
        uid = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference(uid);

        Intent intent = getIntent();
        if(intent.hasExtra("uid"))
            uid = intent.getStringExtra("uid");

        buildRecyclerView();
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        viewModel = new ViewModelProvider(this).get(CardViewItemViewModel.class);
        viewModel.getAll(uid).observe(this, new Observer<List<CardViewItem>>() {
            @Override
            public void onChanged(List<CardViewItem> cardViewItems) {
                ArrayList<CardViewItem> list = new ArrayList<>();
                for(CardViewItem item:cardViewItems){
                    databaseReference.child(item.getId()).setValue(item);
                    if(!item.isCheckBox())
                        list.add(item);
                }
                list = sortItems(list);
                arrayList.clear();
                arrayList.addAll(list);
                mAdapter.notifyDataSetChanged();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    private ArrayList<CardViewItem> sortItems(ArrayList<CardViewItem> list) {
        for(int i = 0;i <= list.size();i++){
            for(int j = 1;j < list.size()-i;j++){
                long a = getCalendar(list.get(j-1).getDate(),list.get(j-1).getTime()).getTimeInMillis();
                long b = getCalendar(list.get(j).getDate(),list.get(j).getTime()).getTimeInMillis();
                if(a > b){
                    CardViewItem item = new CardViewItem(list.get(j-1));
                    list.set(j-1,list.get(j));
                    list.set(j,item);
                }
            }
        }
        return list;
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot itemSnapShot : snapshot.getChildren()) {
                    CardViewItem item = itemSnapShot.getValue(CardViewItem.class);
                    viewModel.insert(item);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void addToDoItem() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        final EditText txt_inputText = (EditText) mView.findViewById(R.id.editTextTitle);
        final EditText in_date = (EditText) mView.findViewById(R.id.in_date);
        final EditText in_time = (EditText) mView.findViewById(R.id.in_time);
        if (cardViewItem != null) {
            txt_inputText.setText(cardViewItem.getTitle());
            in_date.setText(cardViewItem.getDate());
            in_time.setText(cardViewItem.getTime());
        }
        Button btn_ok = (Button) mView.findViewById(R.id.btn_ok);
        Button btn_date = (Button) mView.findViewById(R.id.btn_date);
        Button btn_time = (Button) mView.findViewById(R.id.btn_time);
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(true);
        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in_date.setError(null);
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                in_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in_time.setError(null);
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,R.style.DialogTheme,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                if(hourOfDay<10 && minute<10)
                                    in_time.setText("0"+hourOfDay + ":0" + minute);
                                else if(hourOfDay<10)
                                    in_time.setText("0"+hourOfDay + ":" + minute);
                                else if(minute<10)
                                    in_time.setText(hourOfDay + ":0" + minute);
                                else
                                    in_time.setText(hourOfDay+":"+minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txt_inputText.getText().toString().trim().equals("")) {
                    txt_inputText.setError("Title is required");
                    txt_inputText.requestFocus();
                    return;
                }
                if (in_date.getText().toString().trim().equals("")) {
                    in_date.setError("Date is required");
                    in_date.requestFocus();
                    return;
                }
                if (in_time.getText().toString().trim().equals("")) {
                    in_time.setError("Time is required");
                    in_time.requestFocus();
                    return;
                }
                title = txt_inputText.getText().toString().trim();
                date = in_date.getText().toString().trim();
                time = in_time.getText().toString().trim();
                if(!isValidDate(date)){
                    in_date.setError("Enter proper date format(DD-MM-YYYY)");
                    in_date.requestFocus();
                    return;
                }
                if (Build.VERSION.SDK_INT >= 26) {
                    if (!isValidTime(time)) {
                        in_time.setError("Enter proper time format(DD-MM)");
                        in_time.requestFocus();
                        return;
                    }
                }
                addToDB(title, date, time);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private boolean isValidDate(String date){
        String dateFormat = "dd-MM-yyyy";
        DateFormat df =new SimpleDateFormat(dateFormat);
        df.setLenient(false);
        try {
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean isValidTime(String time){
        try {
            LocalTime.parse(time);
           return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void addToDB(String title, String date, String time) {
        CardViewItem item;
        String id, toastMessage;
        if (cardViewItem != null) {
            id = cardViewItem.getId();
            item = new CardViewItem(id,uid, cardViewItem.isCheckBox(), title, date, time);
            cardViewItem = null;
            viewModel.update(item);
            toastMessage = "To-Do Item Updated";
        } else {
            id = databaseReference.push().getKey();
            item = new CardViewItem(id,uid, false, title, date, time);
            viewModel.insert(item);
            toastMessage = "To-Do Item added";
        }
        databaseReference.child(id).setValue(item);
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
        setAlarm(item);
    }

    public void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new CustomAdapter(arrayList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new CustomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                cardViewItem = arrayList.get(position);
                addToDoItem();
            }

            @Override
            public void onDeleteClick(int position) {
                CardViewItem item = arrayList.get(position);
                databaseReference.child(arrayList.get(position).getId()).removeValue();
                // arrayList.remove(position);
                viewModel.delete(item);
               // mAdapter.notifyItemRemoved(position);
                cancelAlarm(item);
            }

            @Override
            public void onCheckBoxClick(int position) {
                CardViewItem item = arrayList.get(position);
                item.setCheckBox(!item.isCheckBox());
                databaseReference.child(arrayList.get(position).getId()).setValue(item);
                viewModel.update(item);
               // mAdapter.notifyItemChanged(position);
                if (item.isCheckBox()) {
                    cancelAlarm(item);
                    Log.i(TAG, "onCheckBoxClick: Cancel Alarm");
                } else {
                    Log.i(TAG, "onCheckBoxClick: Set Alarm");
                    setAlarm(item);
                }
            }
        });
    }

    private Calendar getCalendar(String date, String time) {
        Calendar calendar = Calendar.getInstance();
        StringTokenizer tokenDate = new StringTokenizer(date, "-");
        StringTokenizer tokenTime = new StringTokenizer(time, ":");
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(tokenDate.nextToken()));
        calendar.set(Calendar.MONTH, Integer.parseInt(tokenDate.nextToken()) - 1);
        calendar.set(Calendar.YEAR, Integer.parseInt(tokenDate.nextToken()));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tokenTime.nextToken()));
        calendar.set(Calendar.MINUTE, Integer.parseInt(tokenTime.nextToken()));
        calendar.set(Calendar.SECOND, 0);
        Log.i(TAG, DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime()));
        return calendar;
    }

    private void setAlarm(CardViewItem item) {
        if (item.isCheckBox())
            return;
        Calendar c = getCalendar(item.getDate(), item.getTime());
        if(c.before(Calendar.getInstance()))
            return;
        c = getPresetTime(c);
        Intent intent = new Intent(this, AlertReceiver.class);
        String id = item.getId();
        String title = item.getTitle();
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        Log.i(TAG, "setAlarm: "+id+" "+title);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id.hashCode(), intent, 0);
        if (Build.VERSION.SDK_INT >= 23)
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        else if (Build.VERSION.SDK_INT >= 19)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        else
            alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private Calendar getPresetTime(Calendar c) {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String alarmPreset = sharedPreferences.getString("alarmPreset","Exact Time");
        if(alarmPreset.equals("Exact Time"))
            return c;
        else{
            StringTokenizer tokenizer = new StringTokenizer(alarmPreset);
            int presetTime = Integer.parseInt(tokenizer.nextToken());
            if(tokenizer.nextToken().equals("Minutes"))
                c.add(Calendar.MINUTE,-presetTime);
            else
                c.add(Calendar.HOUR_OF_DAY,-presetTime);
            return c;
        }
    }

    private void cancelAlarm(CardViewItem item) {
        String id = item.getId();
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id.hashCode(), intent, 0);
        alarmManager.cancel(pendingIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.toggle_darkMode) {
            if (ApplicationStart.isDarkModeOn) {

                // if dark mode is on it
                // will turn it off
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                ApplicationStart.isDarkModeOn = false;
                // it will set isDarkModeOn
                // boolean to false
                editor.putBoolean("isDarkModeOn", false);
            }
            else {

                // if dark mode is off
                // it will turn it on
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                ApplicationStart.isDarkModeOn = true;

                // it will set isDarkModeOn
                // boolean to true
                editor.putBoolean("isDarkModeOn", true);
            }
            editor.apply();
            return true;
        }
        else if(id ==R.id.past_items){
            startActivity(new Intent(this,PastItems.class));
            return true;
            }
        else if(id ==R.id.settings){
            startActivity(new Intent(this,Settings.class));
            return true;
        }
        else if(id ==R.id.logout){
             mAuth.signOut();
             return true;
        }
        return super.onOptionsItemSelected(item);
    }
}