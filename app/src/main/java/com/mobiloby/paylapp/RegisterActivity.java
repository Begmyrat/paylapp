package com.mobiloby.paylapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    SharedPreferences preferences;
    EditText e_username, e_phoneNumber, e_password, e_passwordConfirm;
    String username, phoneNumber, password, passwordConfirm;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Boolean isInserted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        prepareMe();

    }



    private void prepareMe() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorWhiteLight));// set status background white

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        database = FirebaseDatabase.getInstance();
//        myRef = database.getReference("message");

        e_username = findViewById(R.id.e_username);
        e_phoneNumber = findViewById(R.id.e_phone);
        e_password = findViewById(R.id.e_password);
        e_passwordConfirm = findViewById(R.id.e_passwordConfirm);
    }

    public void clickRegister(View view) {
        username = e_username.getText().toString();
        phoneNumber = e_phoneNumber.getText().toString();
        password = e_password.getText().toString();
        passwordConfirm = e_passwordConfirm.getText().toString();

        if(username.length()<6)    e_username.setError("Username must consist at least 6 characters");
        if(phoneNumber.length()<6) e_phoneNumber.setError("Phone Number can not be empty");
        if(password.length()<6)    e_password.setError("Password must consist at least 6 characters");
        if(!password.equals(passwordConfirm))   e_passwordConfirm.setError("Password confirmation is not correct");

        if(username.length()>=6 && phoneNumber.length()>=6 && password.length()>=6 && password.equals(passwordConfirm)){
            registerUser(username, phoneNumber, password);
        }
    }

    private void registerUser(final String username, final String phoneNumber, final String password) {

        myRef = database.getReference().child("Users").child(username);

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                boolean isTaken = false;
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    isTaken = true;
                    break;
                }
                if(isTaken){
                    if(!isInserted)
                        Toast.makeText(RegisterActivity.this, username + " is already taken. Please try different usernames", Toast.LENGTH_SHORT).show();
                    else{
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("username", username);
                        editor.putBoolean("isLoggedIn", true);
                        editor.putString("totalQuestion","0");
                        editor.putString("wrongAnswer","0");
                        editor.putString("trueAnswer","0");
                        editor.putString("currentScore","0");
                        editor.commit();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        finish();
                    }
                }
                else{
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("Username", username);
                    map.put("Phone", phoneNumber);
                    map.put("Password", password);
                    map.put("Money", 0);
                    map.put("OffScore", 0);
                    map.put("OnScore", 0);
                    myRef.updateChildren(map);
                    isInserted = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void clickLogin(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(0,0);
        finish();
    }
}