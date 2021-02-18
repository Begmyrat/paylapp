package com.mobiloby.paylapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ScoreTableActivity extends AppCompatActivity{

    BottomNavigationView bottomNavigationView;
    FirebaseDatabase database;
    DatabaseReference myRef;
    SharedPreferences preferences;
    ListView listView;
    ArrayList<UserObject> users, users_copy;
    MyScoreListAdapter adapter;
    TextView t_utuk;
    EditText e_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_table);

        prepareMe();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.bottom_nav_main:
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        finish();
                        break;
                    case R.id.bottom_nav_profile:
                        Intent intent1 = new Intent(getApplicationContext(), ProfileActivity.class);
//                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        overridePendingTransition(0,0);
                        finish();
                        break;
                    case R.id.bottom_nav_scoreboard:
//                        Intent intent2 = new Intent(getApplicationContext(), ScoreTableActivity.class);
//                        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent2);
//                        overridePendingTransition(0,0);
//                        finish();
                        break;
                }

                return false;
            }
        });

    }

    public void getUsers(){

        users.clear();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                boolean isTaken = false;
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    UserObject u = d.getValue(UserObject.class);
                    users.add(u);
//                    Toast.makeText(ScoreTableActivity.this, ""+u.getUsername(), Toast.LENGTH_SHORT).show();
                }
                Collections.sort(users,
                        new Comparator<UserObject>() {
                            @Override
                            public int compare(UserObject o1, UserObject o2) {
                                if(Integer.parseInt(o1.getOffScore())>Integer.parseInt(o2.getOffScore()))  return -1;
                                else if(Integer.parseInt(o1.getOffScore())<Integer.parseInt(o2.getOffScore())) return 1;

                                return 0;
                            }
                        });

                for(int i=0;i<users.size();i++){
                    users_copy.add(users.get(i));
                }

                for (int i=0;i<users.size();i++){
                    if(users.get(i).getUsername().equals(preferences.getString("username",""))){
                        t_utuk.setText("#"+(i+1)+" orun. "+users.get(i).getOffScore() + " utuk");
                    }
                }



                adapter = new MyScoreListAdapter(ScoreTableActivity.this, users);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(ScoreTableActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void prepareMe() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorBackground));// set status background white
        // ignore edittext focus
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav_scoreboard);

        t_utuk = findViewById(R.id.t_utuk);
        e_search = findViewById(R.id.e_search);
        e_search.addTextChangedListener(new InputValidator());
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        database = FirebaseDatabase.getInstance();

        myRef = database.getReference().child("Users");

        users = new ArrayList<>();
        users_copy = new ArrayList<>();
        listView = findViewById(R.id.listview);

        getUsers();

        adapter = new MyScoreListAdapter(this, users);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void clickTemizle(View view) {
        e_search.setText("");
    }

    public void clickSearch(View view) {
        performSearch();
    }

    private void performSearch() {
        ArrayList<UserObject> copy = new ArrayList<>();
        String word = e_search.getText().toString();
        if(word.length()>0){
            for(int i=0;i<users_copy.size();i++){
                if (users_copy.get(i).getUsername().contains(word) || word.contains(users_copy.get(i).getUsername())){
                    copy.add(users_copy.get(i));
                }
            }
        }

        users.clear();

        for(int i=0;i<copy.size();i++){
            users.add(copy.get(i));
        }

        adapter.notifyDataSetChanged();
    }

    private class InputValidator implements TextWatcher {

        public void afterTextChanged(Editable s) {

        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length()==0){
                getUsers();
            }
            else{
//                makeAlert.uyarıVer("haha", "search "+edittext_search.getText().toString(), CategoriesPage.this, false);
                performSearch();
            }
        }
    }
}