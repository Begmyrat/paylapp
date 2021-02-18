package com.mobiloby.paylapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class MainActivity extends AppCompatActivity implements MyRecycleListAdapter.ItemClickListener{

    SharedPreferences preferences;
    String username;
    ArrayList<QuestionObject> questions;
    FirebaseDatabase database;
    DatabaseReference myRef;
    BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<MenuObject> menus;
    MyRecycleListAdapter adapter;
    HorizontalCalendar horizontalCalendar;
    Calendar startDate,endDate;
    JSONObject jsonObject;
    JSONParser jsonParser;
    String oldScore, currentScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prepareMe();

//        getKonular();

//        Intent servIntent = new Intent(this, MyService.class);
//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            startForegroundService(servIntent);
//        }
//        else{
//            startService(servIntent);
//        }



        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                //do something
            }
        });

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {

            }

            @Override
            public void onCalendarScroll(HorizontalCalendarView calendarView,
                                         int dx, int dy) {

            }

            @Override
            public boolean onDateLongClicked(Calendar date, int position) {
                return true;
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.bottom_nav_main:
//                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                        startActivity(intent);
//                        overridePendingTransition(0,0);
//                        finish();
                        break;
                    case R.id.bottom_nav_profile:
                        Intent intent1 = new Intent(getApplicationContext(), ProfileActivity.class);
//                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        overridePendingTransition(0,0);
                        finish();
                        break;
                    case R.id.bottom_nav_scoreboard:
                        Intent intent2 = new Intent(getApplicationContext(), ScoreTableActivity.class);
//                        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent2);
                        overridePendingTransition(0,0);
                        finish();
                        break;
                }

                return false;
            }
        });

        getQuestions();

    }

    private void prepareMe() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorWhiteLight));// set status background white

        startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);
        endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .build();

        horizontalCalendar.goToday(true);


        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        username = preferences.getString("username","bigimi");

        questions = new ArrayList<>();
        database = FirebaseDatabase.getInstance();

        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav_main);

        recyclerView = findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        menus = new ArrayList<>();
        menus.add(new MenuObject("Sorag Çöz", "1243"));
        menus.add(new MenuObject("Sorag Ýaz", "43"));
//        menus.add(new MenuObject("Taryh", "612"));
//        menus.add(new MenuObject("Sport", "933"));
        adapter = new MyRecycleListAdapter(this, menus);
        recyclerView.setAdapter(adapter);

        adapter.setClickListener(this);

        oldScore = preferences.getString("oldScore","-1");

        myRef = database.getReference().child("Users").child(username).child("OffScore");

        if(oldScore.equals("-1")){
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if(snapshot.getValue()!=null){
                        oldScore = snapshot.getValue().toString();
                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            currentScore = oldScore;
        }
        else{
            currentScore = preferences.getString("currentScore","0");
        }

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("oldScore", oldScore);
        editor.putString("currentScore", currentScore);
        editor.commit();

//        updateScore();

        if(!oldScore.equals(currentScore)){
            updateScore();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        oldScore = preferences.getString("oldScore","0");

        myRef = database.getReference().child("Users").child(username).child("OffScore");

        if(oldScore.equals("0")){
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if(snapshot.getValue()!=null){
                        oldScore = snapshot.getValue().toString();
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("oldScore", oldScore);
                        editor.putString("currentScore", oldScore);
                        editor.commit();
                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        else{
            currentScore = preferences.getString("currentScore","0");
            if(!oldScore.equals(currentScore)){
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("oldScore", currentScore);
//                editor.putString("currentScore", currentScore);
                editor.commit();
                updateScore();
            }
        }


//        Toast.makeText(this, "user: " + username + "  main old: " + oldScore + " current: " + currentScore, Toast.LENGTH_SHORT).show();

//        if(!oldScore.equals(currentScore)){
//            updateScore();
//        }


    }

    private void updateScore() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("OffScore", currentScore);
        map.put("TotalQuestion", preferences.getString("totalQuestion","0"));
        map.put("TrueAnswer", preferences.getString("trueAnswer","0"));
        map.put("WrongAnswer", preferences.getString("wrongAnswer","0"));
        database.getReference().child("Users").child(username).updateChildren(map);
    }

    public void getQuestions(){

        myRef = database.getReference().child("Questions").child("General");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                boolean isTaken = false;
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    QuestionObject q = d.getValue(QuestionObject.class);
                    questions.add(q);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {

        if(position==0){
            Intent intent1 = new Intent(getApplicationContext(), OfflineModeActivity.class);
//            if(position==0){
                intent1.putExtra("category", "Read");
//            }
//            else if(position==1){
//                intent1.putExtra("category", "Write");
//            }
//        else if(position==2){
//            intent1.putExtra("category", "History");
//        }
//        else if(position==3){
//            intent1.putExtra("category", "Sport");
//        }
            startActivity(intent1);
//        overridePendingTransition(0,0);
        }
        else{
            Intent intent1 = new Intent(getApplicationContext(), InsertQuestion.class);
//            if(position==0){
//                intent1.putExtra("category", "Read");
//            }
//            else if(position==1){
//                intent1.putExtra("category", "Write");
//            }
//        else if(position==2){
//            intent1.putExtra("category", "History");
//        }
//        else if(position==3){
//            intent1.putExtra("category", "Sport");
//        }
            startActivity(intent1);
//        overridePendingTransition(0,0);
        }


    }

}