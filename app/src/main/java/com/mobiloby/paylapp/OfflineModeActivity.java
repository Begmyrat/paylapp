package com.mobiloby.paylapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.autofill.Dataset;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class OfflineModeActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef, myRef2;
    Bundle extras;
    ArrayList<QuestionObject> questions;
    int questionIndex = -1, oldQuestionIndex=-2;
    TextView t_question, t_answer1, t_answer2, t_answer3, t_answer4, t_countdown, t_score;
    Vibrator v;
    BottomNavigationView bottomNavigationView;
    Boolean isFifty = false;
    SharedPreferences preferences;
    String username;
    int oldScore, currentScore, totalQuestion, totalScore, second;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_mode);

        prepareMe();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.bottom_nav_fifty:
                        clickFifty();
                        break;
                    case R.id.bottom_nav_next:
                        Toast.makeText(OfflineModeActivity.this, "sorag sany: " + questions.size(), Toast.LENGTH_SHORT).show();
                        getCurrentQuestion();
                        initAllAnswerBoxes();
                        break;
                    case R.id.bottom_nav_like:
                        // like it!!!
                        Toast.makeText(OfflineModeActivity.this, "Dislike", Toast.LENGTH_SHORT).show();
                        reportQuestion();
                        break;
                }

                return false;
            }
        });

    }

    private void reportQuestion() {
        final HashMap<String, Object> map = new HashMap<>();
        Toast.makeText(this, "reportQuestion: " + questions.get(questionIndex).getKey(), Toast.LENGTH_SHORT).show();

        myRef = database.getReference().child("Questions").child(questions.get(questionIndex).getCategory()).child(questions.get(questionIndex).getKey()).child("Report");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                if(dataSnapshot.exists()){
                    int reportNumber = 0;
                    if(dataSnapshot.getValue()!=null) {
                        reportNumber = Integer.parseInt(dataSnapshot.getValue().toString());
                        myRef = database.getReference().child("Questions").child(questions.get(questionIndex).getCategory()).child(questions.get(questionIndex).getKey());
                        map.put("Report", ""+(reportNumber+1));
                        myRef.updateChildren(map);
                        if(reportNumber>200){
                            database.getReference().child("Questions").child(questions.get(questionIndex).getCategory()).child(questions.get(questionIndex).getKey()).removeValue();
                            Toast.makeText(OfflineModeActivity.this, "deleted: " + reportNumber, Toast.LENGTH_SHORT).show();
                            questions.remove(questionIndex);
//                        getCurrentQuestion();
                        }
                    }
                }
//                else{
//                    myRef = database.getReference().child("Questions").child(questions.get(questionIndex).getCategory()).child(questions.get(questionIndex).getKey());
//                    HashMap<String, Object> reportMap  = new HashMap<>();
//                    reportMap.put("Report", ""+0);
//                    Toast.makeText(OfflineModeActivity.this, "key: " + questions.get(questionIndex).getKey(), Toast.LENGTH_SHORT).show();
//                    Toast.makeText(OfflineModeActivity.this, "Hehehe", Toast.LENGTH_SHORT).show();
//                    myRef.updateChildren(reportMap);
//                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(OfflineModeActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void clickFifty() {

        if(totalScore>10){
            currentScore = 0;
            totalScore -= 10;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("currentScore", ""+totalScore);
            editor.commit();
            isFifty = true;
            QuestionObject currentQuestion = questions.get(questionIndex);
            int correctIndex = Integer.parseInt(currentQuestion.getAnswer().substring(1));
            int kalanCevap = (correctIndex + 4) % correctIndex;
            if(kalanCevap==0){
                kalanCevap = (correctIndex+1)%4;
            }
            if(correctIndex==kalanCevap)
                kalanCevap = (kalanCevap+1)%4;
            for (int i=0;i<4;i++){
                if(i==0 && i!=kalanCevap && i!=correctIndex && correctIndex!=4)
                    findViewById(R.id.r_answer4).setVisibility(View.INVISIBLE);
                else if(i==1 && i!=kalanCevap && i!=correctIndex)
                    findViewById(R.id.r_answer1).setVisibility(View.INVISIBLE);
                else if(i==2 && i!=kalanCevap && i!=correctIndex)
                    findViewById(R.id.r_answer2).setVisibility(View.INVISIBLE);
                else if(i==3 && i!=kalanCevap && i!=correctIndex)
                    findViewById(R.id.r_answer3).setVisibility(View.INVISIBLE);
            }
        }
        else{
            Toast.makeText(this, "Ÿeterlik utuk ÿok.\nIñ az 10 utuk gerek", Toast.LENGTH_SHORT).show();
        }


    }

    private void getCurrentQuestion() {

        findViewById(R.id.r_unclick).setVisibility(View.GONE);

        if(countDownTimer!=null)
            countDownTimer.cancel();
        second = 31;
        countDownTimer = new CountDownTimer(31000, 1000) {

            public void onTick(long millisUntilFinished) {
                second -= 1;
                t_countdown.setText("" + second);
                t_score.setText("+"+(second/2+currentScore+((second/2)%2)+1));
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
//                t_countdown.setText("done!");
//                v.vibrate(400);
//                clickNext();
                getCurrentQuestion();
            }

        }.start();

        isFifty = false;
        initAllAnswerBoxes();
        oldQuestionIndex = questionIndex;
        questionIndex = Integer.MAX_VALUE;
        while(questionIndex>=questions.size() && questions.size()!=0 && questionIndex!=oldQuestionIndex)
            questionIndex = (int) (Math.random()*(questions.size()-1));
//        questionIndex++;
//        Toast.makeText(this, "index: " + questionIndex, Toast.LENGTH_SHORT).show();
        if(questionIndex<questions.size()  && questions.size()!=0) {
            QuestionObject q = questions.get(questionIndex);
            t_question.setText(q.getQuestion());
            t_answer1.setText(q.getA1());
            t_answer2.setText(q.getA2());
            t_answer3.setText(q.getA3());
            t_answer4.setText(q.getA4());
        }
    }

    private void prepareMe() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorWhiteLight));// set status background white
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        username = preferences.getString("username","");
        oldScore = Integer.parseInt(preferences.getString("oldScore", "0"));
        totalQuestion = Integer.parseInt(preferences.getString("totalQuestion", "0"));
        totalScore = oldScore;
        currentScore = 0;

        extras = getIntent().getExtras();
        questions = new ArrayList<>();
        t_question = findViewById(R.id.t_question);
        t_answer1 = findViewById(R.id.t_answer1);
        t_answer2 = findViewById(R.id.t_answer2);
        t_answer3 = findViewById(R.id.t_answer3);
        t_answer4 = findViewById(R.id.t_answer4);
        t_countdown = findViewById(R.id.t_countDown);
        t_score = findViewById(R.id.t_score);

        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav_next);

        if(extras!=null){
            database = FirebaseDatabase.getInstance();

            final ArrayList<String> kategoriler = new ArrayList<>();
            kategoriler.add("General");
            kategoriler.add("Geography");
            kategoriler.add("History");
            kategoriler.add("Sport");
            kategoriler.add("Turkmen");

            if(extras.getString("category").equals("Read")){
                for(int i=0;i<kategoriler.size();i++) {

                    myRef = database.getReference().child("Questions").child(kategoriler.get(i));
                    Query query = myRef.orderByChild("Confirmation").equalTo("0");

//                    myRef = database.getReference().child("Questions").child("Turkmen");

                    final int index = i;

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot d : snapshot.getChildren()) {
                                if (d != null) {
                                    QuestionObject q = d.getValue(QuestionObject.class);

                                    //   Toast.makeText(OfflineModeActivity.this, "" + q.getQuestion(), Toast.LENGTH_SHORT).show();
                                    q.setKey(d.getKey());
                                    q.setCategory(kategoriler.get(index));
                                    questions.add(q);
//                                    myRef2 = database.getReference().child("Questions").child(kategoriler.get(index)).child(d.getKey());
//                                    HashMap<String, Object> m = new HashMap<>();
//                                    m.put("Confirmation", "0");
//                                    myRef2.updateChildren(m);
                                }
                            }
                            getCurrentQuestion();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    //            Toast.makeText(this, "extras geldi", Toast.LENGTH_SHORT).show();
//                    myRef.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            // This method is called once with the initial value and again
//                            // whenever data at this location is updated.
//
//                            for (DataSnapshot d : dataSnapshot.getChildren()) {
//                                if (d != null) {
//                                    QuestionObject q = d.getValue(QuestionObject.class);
//
//                                    //   Toast.makeText(OfflineModeActivity.this, "" + q.getQuestion(), Toast.LENGTH_SHORT).show();
//                                    q.setKey(d.getKey());
//                                    q.setCategory(kategoriler.get(index));
//                                    questions.add(q);
////                                    myRef2 = database.getReference().child("Questions").child(kategoriler.get(index)).child(d.getKey());
////                                    HashMap<String, Object> m = new HashMap<>();
////                                    m.put("Confirmation", "0");
////                                    myRef2.updateChildren(m);
//                                }
//                            }
//
////                            for(int j=0;j<questions.size();j++){
//////                                HashMap<String, Object> m = new HashMap<>();
//////                                m.put("Report", "0");
//////                                myRef2.updateChildren(m);
////                                if(questions.get(j).getQuestion()==null || questions.get(j).getQuestion().length()==0){
////                                    myRef2 = database.getReference().child("Questions").child("Turkmen").child(questions.get(j).getKey());
////                                    myRef2.removeValue();
////                                    questions.remove(j);
////                                }
////                            }
//
//                            getCurrentQuestion();
//
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError error) {
//                            // Failed to read value
//                            Toast.makeText(OfflineModeActivity.this, "Error", Toast.LENGTH_SHORT).show();
//                        }
//                    });
                }
            }
            else{
//                myRef = database.getReference().child("Questions").child(extras.getString("category"));
//
//                //            Toast.makeText(this, "extras geldi", Toast.LENGTH_SHORT).show();
//                myRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        // This method is called once with the initial value and again
//                        // whenever data at this location is updated.
//
//                        for (DataSnapshot d : dataSnapshot.getChildren()) {
//                            if (d != null) {
//                                QuestionObject q = d.getValue(QuestionObject.class);
//                                //   Toast.makeText(OfflineModeActivity.this, "" + q.getQuestion(), Toast.LENGTH_SHORT).show();
////                                Toast.makeText(OfflineModeActivity.this, "key: " + d.getKey(), Toast.LENGTH_SHORT).show();d.getKey();
//                                q.setKey(d.getKey());
//                                q.setCategory(extras.getString("category"));
//                                questions.add(q);
//                            }
//                        }
//
//                        getCurrentQuestion();
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError error) {
//                        // Failed to read value
//                        Toast.makeText(OfflineModeActivity.this, "Error", Toast.LENGTH_SHORT).show();
//                    }
//                });

            }

        }


    }

    public void clickBack(View view) {
        finish();
    }

    public void clickNext(View v) {
        // get next question
        initAllAnswerBoxes();
        getCurrentQuestion();
    }

    public void initAllAnswerBoxes(){

        findViewById(R.id.r_answer1).setVisibility(View.VISIBLE);
        findViewById(R.id.r_answer2).setVisibility(View.VISIBLE);
        findViewById(R.id.r_answer3).setVisibility(View.VISIBLE);
        findViewById(R.id.r_answer4).setVisibility(View.VISIBLE);

        t_answer1.setTextColor(getResources().getColor(R.color.colorTextColor));
        t_answer2.setTextColor(getResources().getColor(R.color.colorTextColor));
        t_answer3.setTextColor(getResources().getColor(R.color.colorTextColor));
        t_answer4.setTextColor(getResources().getColor(R.color.colorTextColor));
        findViewById(R.id.r_answer1).setBackground(getDrawable(R.drawable.answer_background));
        findViewById(R.id.r_answer2).setBackground(getDrawable(R.drawable.answer_background));
        findViewById(R.id.r_answer3).setBackground(getDrawable(R.drawable.answer_background));
        findViewById(R.id.r_answer4).setBackground(getDrawable(R.drawable.answer_background));
    }
    public void checkAnswer(int index){

        findViewById(R.id.r_unclick).setVisibility(View.VISIBLE);

        totalQuestion++;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("totalQuestion", ""+totalQuestion);
        editor.commit();

        if(index==1){
            findViewById(R.id.r_answer1).setBackground(getDrawable(R.drawable.yellow_answer_background));
        }
        else if(index==2){
            findViewById(R.id.r_answer2).setBackground(getDrawable(R.drawable.yellow_answer_background));
        }
        else if(index==3){
            findViewById(R.id.r_answer3).setBackground(getDrawable(R.drawable.yellow_answer_background));
        }
        else if(index==4){
            findViewById(R.id.r_answer4).setBackground(getDrawable(R.drawable.yellow_answer_background));
        }

        final int userAnswerIndex = index;

        Handler handler = new Handler();

        if(isFifty){
            handler.postDelayed(new Runnable() {
                public void run() {
                    // yourMethod();
                    if(questions.get(questionIndex).getAnswer().equals("A"+userAnswerIndex)){
//                        myRef = database.getReference().child("Users").child(username).child("OffScore");
//                        myRef.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot snapshot) {
//                                if(snapshot.getValue()!=null){
//                                    Toast.makeText(OfflineModeActivity.this, "s: " + snapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
//                                    s = Integer.parseInt(snapshot.getValue().toString());
//                                }
//
//                            }
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//                            }
//                        });
                        makeCorrectAnswer(userAnswerIndex);
                    }
                    else{
                        makeWrongAnswer(userAnswerIndex);
                    }
                }
            }, 200);   //2 seconds
        }
        else{
            handler.postDelayed(new Runnable() {
                public void run() {
                    // yourMethod();
                    initAllAnswerBoxes();
                    if(questions.get(questionIndex).getAnswer().equals("A"+userAnswerIndex)){
                        v.vibrate(100);
                        makeCorrectAnswer(userAnswerIndex);

//                        myRef = database.getReference().child("Users").child(username).child("OffScore");
//
//                        myRef.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot snapshot) {
//                                if(snapshot.getValue()!=null){
//                                    Toast.makeText(OfflineModeActivity.this, "s: " + snapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
//                                    s = Integer.parseInt(snapshot.getValue().toString());
//                                }
//
//                            }
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//                            }
//                        });
                    }
                    else{
                        makeWrongAnswer(userAnswerIndex);
                    }

                }
            }, 200);   //2 seconds
        }
//
//        if(questions.get(questionIndex).getAnswer().equals("A"+userAnswerIndex))
//            database.getReference().child("Users").child(username).child("OffScore").setValue(s+1);

        Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            public void run() {
                // yourMethod();
//                countDownTimer.cancel();
                getCurrentQuestion();

            }
        }, 1000);   //2 seconds
    }

    public void makeCorrectAnswer(int index){

        currentScore++;
        totalScore += (second/2+currentScore+((second/2)%2));
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("currentScore", ""+totalScore);
        editor.putString("trueAnswer",""+ (Integer.parseInt(preferences.getString("trueAnswer", "0"))+1));
        editor.commit();
        v.vibrate(100);

        Toast.makeText(this, "" + (second/2+currentScore+((second/2)%2)) + " utuk gazandyňyz", Toast.LENGTH_SHORT).show();

        final HashMap<String, Object> map = new HashMap<>();

        if(index==1){
//            t_answer1.setTextColor(getResources().getColor(R.color.colorWhite));
            findViewById(R.id.r_answer1).setBackground(getDrawable(R.drawable.correct_answer_background));
        }
        else if(index==2){
//            t_answer2.setTextColor(getResources().getColor(R.color.colorWhite));
            findViewById(R.id.r_answer2).setBackground(getDrawable(R.drawable.correct_answer_background));
        }
        else if(index==3){
//            t_answer3.setTextColor(getResources().getColor(R.color.colorWhite));
            findViewById(R.id.r_answer3).setBackground(getDrawable(R.drawable.correct_answer_background));
        }
        else if(index==4){
//            t_answer4.setTextColor(getResources().getColor(R.color.colorWhite));
            findViewById(R.id.r_answer4).setBackground(getDrawable(R.drawable.correct_answer_background));
        }


    }

    public void makeWrongAnswer(int index){

        currentScore = 0;

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("wrongAnswer",""+ (Integer.parseInt(preferences.getString("wrongAnswer", "0"))+1));
        editor.commit();

        v.vibrate(100);
        if(index==1){
//            t_answer1.setTextColor(getResources().getColor(R.color.colorWhite));
            findViewById(R.id.r_answer1).setBackground(getDrawable(R.drawable.wrong_answer_background));
        }
        else if(index==2){
//            t_answer2.setTextColor(getResources().getColor(R.color.colorWhite));
            findViewById(R.id.r_answer2).setBackground(getDrawable(R.drawable.wrong_answer_background));
        }
        else if(index==3){
//            t_answer3.setTextColor(getResources().getColor(R.color.colorWhite));
            findViewById(R.id.r_answer3).setBackground(getDrawable(R.drawable.wrong_answer_background));
        }
        else if(index==4){
//            t_answer4.setTextColor(getResources().getColor(R.color.colorWhite));
            findViewById(R.id.r_answer4).setBackground(getDrawable(R.drawable.wrong_answer_background));
        }
    }

    public void clickA1(View view) {
        checkAnswer(1);
    }

    public void clickA2(View view) {
        checkAnswer(2);
    }

    public void clickA3(View view) {
        checkAnswer(3);
    }

    public void clickA4(View view) {
        checkAnswer(4);
    }
}