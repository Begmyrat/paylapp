package com.mobiloby.paylapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class InsertQuestion extends AppCompatActivity {

    EditText e_soru, e_a1,e_a2,e_a3,e_a4, e_correct;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Spinner spinner, spinner_answers;
    String correctAnswer = "", username;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_question);

        prepareMe();

    }

    private void prepareMe() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorWhite));// set status background white
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        database = FirebaseDatabase.getInstance();
        e_soru = findViewById(R.id.e_question);
        e_a1 = findViewById(R.id.e_a1);
        e_a2 = findViewById(R.id.e_a2);
        e_a3 = findViewById(R.id.e_a3);
        e_a4 = findViewById(R.id.e_a4);
        e_correct = findViewById(R.id.e_correct);
        spinner = findViewById(R.id.spinner_categories);
        spinner_answers = findViewById(R.id.spinner_answers);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        username = preferences.getString("username","belliDal");
    }

    public void clickTamamla(View view) {
        correctAnswer = "";
        if(spinner_answers.getSelectedItem().toString().equals("A"))    correctAnswer = "A1";
        else if(spinner_answers.getSelectedItem().toString().equals("B"))    correctAnswer = "A2";
        else if(spinner_answers.getSelectedItem().toString().equals("Ç"))    correctAnswer = "A3";
        else if(spinner_answers.getSelectedItem().toString().equals("D"))    correctAnswer = "A4";
        final HashMap<String, Object> map = new HashMap<>();
        map.put("Question", e_soru.getText().toString());
        map.put("A1", e_a1.getText().toString());
        map.put("A2", e_a2.getText().toString());
        map.put("A3", e_a3.getText().toString());
        map.put("A4", e_a4.getText().toString());
        map.put("Answer", correctAnswer);
        map.put("Report", "0");
        map.put("Confirmation", "0");
        map.put("Author", username);

        if(e_soru.getText().toString().length()>0 && e_a1.getText().toString().length()>0 && e_a2.getText().toString().length()>0 && e_a3.getText().toString().length()>0 && e_a4.getText().toString().length()>0 && correctAnswer.length()>0){
            myRef = database.getReference().child("Questions").child(spinner.getSelectedItem().toString()).push();
            myRef.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    startActivity(new Intent(InsertQuestion.this, InsertQuestion.class));
                    finish();
                }
            });

        }


//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                myRef.updateChildren(map);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    public void initAnswers(){
        findViewById(R.id.t_answer1).setBackgroundColor(getColor(R.color.colorWhiteLight));
        findViewById(R.id.t_answer2).setBackgroundColor(getColor(R.color.colorWhiteLight));
        findViewById(R.id.t_answer3).setBackgroundColor(getColor(R.color.colorWhiteLight));
        findViewById(R.id.t_answer4).setBackgroundColor(getColor(R.color.colorWhiteLight));
    }

    public void clickA1(View view) {
        initAnswers();
        findViewById(R.id.t_answer1).setBackgroundColor(getColor(R.color.colorAccent));
        correctAnswer = "A1";
    }

    public void clickA2(View view) {
        initAnswers();
        findViewById(R.id.t_answer2).setBackgroundColor(getColor(R.color.colorAccent));
        correctAnswer = "A2";
    }

    public void clickA3(View view) {
        initAnswers();
        findViewById(R.id.t_answer3).setBackgroundColor(getColor(R.color.colorAccent));
        correctAnswer = "A3";
    }

    public void clickA4(View view) {
        initAnswers();
        findViewById(R.id.t_answer4).setBackgroundColor(getColor(R.color.colorAccent));
        correctAnswer = "A4";
    }
}