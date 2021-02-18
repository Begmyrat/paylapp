package com.mobiloby.paylapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPassword extends AppCompatActivity {

    Bundle extras;
    EditText e_privateNumber;
    String privateNumber;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorForgotPassword));// set status background white

        extras = getIntent().getExtras();
        e_privateNumber = findViewById(R.id.e_privateQuestion);

        if(extras!=null){
            privateNumber = extras.getString("privateQuestion");
            username = extras.getString("username");
        }
    }

    public void clickTamam(View view) {
        if(e_privateNumber.getText().toString().equals(privateNumber)){
            Intent intent = new Intent(this, UpdatePassword.class);
            intent.putExtra("username", username);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(this, "Syrly san dogry däl!!!", Toast.LENGTH_SHORT).show();
        }
    }
}