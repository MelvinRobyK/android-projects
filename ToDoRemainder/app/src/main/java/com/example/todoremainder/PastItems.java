package com.example.todoremainder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

public class PastItems extends AppCompatActivity {

    ArrayList<CardViewItem> arrayList = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private CustomAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String uid;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    private CardViewItem cardViewItem = null;
    private CardViewItemViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_items);

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference(uid);

        buildRecyclerView();

        viewModel = new ViewModelProvider(this).get(CardViewItemViewModel.class);
        viewModel.getAll(uid).observe(this, new Observer<List<CardViewItem>>() {
            @Override
            public void onChanged(List<CardViewItem> cardViewItems) {
                arrayList.clear();
                List<CardViewItem> temp = getPastRemainders(cardViewItems);
                arrayList.addAll(temp);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private List<CardViewItem> getPastRemainders(List<CardViewItem> cardViewItems) {
        ArrayList<CardViewItem> temp = new ArrayList<>();
        for(CardViewItem item:cardViewItems){
            Calendar calendar = getCalendar(item.getDate(),item.getTime());
            if(calendar.before(Calendar.getInstance()))
                temp.add(item);
        }
        return temp;
    }

    public void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView2);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new CustomAdapter(arrayList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new CustomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onDeleteClick(int position) {
                CardViewItem item = arrayList.get(position);
                databaseReference.child(arrayList.get(position).getId()).removeValue();
                viewModel.delete(item);
                //mAdapter.notifyItemRemoved(position);
            }

            @Override
            public void onCheckBoxClick(int position) {
                CardViewItem item = arrayList.get(position);
                item.setCheckBox(!item.isCheckBox());
                databaseReference.child(arrayList.get(position).getId()).setValue(item);
                viewModel.update(item);
                //mAdapter.notifyItemChanged(position);
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
        Log.i("calendarDate", DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime()));
        return calendar;
    }
}