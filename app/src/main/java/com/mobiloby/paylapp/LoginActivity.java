package com.mobiloby.paylapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    SharedPreferences preferences;
    EditText e_username, e_password;
    String username, password;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prepareMe();
    }

    private void prepareMe() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorWhiteLight));// set status background white

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        database = FirebaseDatabase.getInstance();
        e_username = findViewById(R.id.e_username);
        e_password = findViewById(R.id.e_password);
    }

    public void clickLogin(View view) {
        username = e_username.getText().toString();
        password = e_password.getText().toString();

        myRef = database.getReference().child("Users").child(username);

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                boolean isTaken = false;
                UserObject u = dataSnapshot.getValue(UserObject.class);
                if(u!=null){
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("totalQuestion", u.getTotalQuestion());
                    editor.putString("wrongAnswer", u.getWrongAnswer());
                    editor.putString("trueAnswer", u.getTrueAnswer());
                    editor.putString("currentScore",u.getOffScore());
                    editor.commit();
                }
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    if(d.getKey().toString().equals("Password") && d.getValue().toString().equals(password)){
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("username", username);
                        editor.putBoolean("isLoggedIn", true);
                        editor.commit();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        finish();
                    }
                    else if(d.getKey().toString().equals("Password") && !d.getValue().toString().equals(password)){
                        Toast.makeText(LoginActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void clickRegister(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
//        overridePendingTransition(0,0);
    }

    public void clickForgotPassword(View view) {

        if(e_username.getText().toString().length()>0) {

            myRef = database.getReference().child("Users").child(e_username.getText().toString());

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    if(dataSnapshot!=null) {
                        UserObject u = dataSnapshot.getValue(UserObject.class);
                        if(u==null){
                            Toast.makeText(LoginActivity.this, "Bu ulanyjy ady ÿok!!!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
                            intent.putExtra("privateQuestion", u.getPrivateNumber());
                            intent.putExtra("username", e_username.getText().toString());
                            startActivity(intent);
                            finish();
                        }
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Bu ulanyjy ady ÿok!!!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            e_username.setError("Ulanyjy adyñyzy giriñ");
        }
    }
}