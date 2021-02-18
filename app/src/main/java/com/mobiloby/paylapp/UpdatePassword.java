package com.mobiloby.paylapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class UpdatePassword extends AppCompatActivity {

    String password, passwordConfirm;
    EditText e_password, e_passwordConfirm;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String username;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        database = FirebaseDatabase.getInstance();

        e_password = findViewById(R.id.e_password);
        e_passwordConfirm = findViewById(R.id.e_passwordConfirm);

        extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
        }
    }

    private void update() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("Password", password);
        database.getReference().child("Users").child(username).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("isLoggedIn", true);
                editor.commit();
                Intent intent = new Intent(UpdatePassword.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    public void clickUpdate(View view) {
        password = e_password.getText().toString();
        passwordConfirm = e_passwordConfirm.getText().toString();

        if(password.length()<6)    e_password.setError("Password must consist at least 6 characters");
        if(!password.equals(passwordConfirm))   e_passwordConfirm.setError("Password confirmation is not correct");

        if(password.length()>=6 && password.equals(passwordConfirm)){
            update();
        }
    }
}