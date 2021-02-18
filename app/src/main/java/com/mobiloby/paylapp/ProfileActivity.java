package com.mobiloby.paylapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    SharedPreferences preferences;
    TextView t_username, t_correct, t_wrong, t_toplam, t_phone, t_money, t_offScore, t_subtitle;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
//                        Intent intent1 = new Intent(getApplicationContext(), ProfileActivity.class);
//                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent1);
//                        overridePendingTransition(0,0);
//                        finish();
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

    }

    private void prepareMe() {
        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav_profile);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorWhiteLight));// set status background white
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        t_username = findViewById(R.id.t_username);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        username = preferences.getString("username","");
        t_username.setText(username);
        t_correct = findViewById(R.id.t_correct);
        t_wrong = findViewById(R.id.t_wrong);
        t_toplam = findViewById(R.id.t_total);
        t_phone = findViewById(R.id.t_phone);
        t_money = findViewById(R.id.t_money);
        t_offScore = findViewById(R.id.t_score);
        t_subtitle = findViewById(R.id.t_subtitle);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Users").child(username);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.getValue()!=null){
                    UserObject u = snapshot.getValue(UserObject.class);
                    t_toplam.setText(""+u.getTotalQuestion());
                    t_correct.setText(""+u.getTrueAnswer());
                    t_wrong.setText(""+u.getWrongAnswer());
                    t_phone.setText(u.getPhone());
                    t_money.setText(""+u.getMoney() + " manat");
                    t_offScore.setText(""+u.getOffScore() + " utuk");
                    t_subtitle.setText(""+u.getPhone());
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void clickEdit(View view) {
        //edit username
    }

    public void clickBack(View view) {
        finish();
        overridePendingTransition(0,0);
    }

    public void clickLogOut(View view) {
        //logout
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.putString("offScore", "0");
        editor.putString("currentScore", "0");
        editor.commit();
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(0,0);
        finish();

    }

    public void clickInsertQuestion(View view) {
        if(Integer.parseInt(t_offScore.getText().toString().substring(0,t_offScore.getText().toString().indexOf(" ")))>=10)
            startActivity(new Intent(this, InsertQuestion.class));
        else{
            Toast.makeText(this, "Sorag ÿazmak üçin 10 utuk toplamaly!!!", Toast.LENGTH_SHORT).show();
        }
    }
}