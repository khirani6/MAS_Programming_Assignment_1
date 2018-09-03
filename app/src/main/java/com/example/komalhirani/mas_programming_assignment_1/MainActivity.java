package com.example.komalhirani.mas_programming_assignment_1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static final String USER_NAME = "Profile Created";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendMessage(View view) {
        /*intent code is based on android's developers documentation:
        https://developer.android.com/training/basics/firstapp/starting-activity#java
         */
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText userName = findViewById(R.id.userNameField);
        String userNameStr = userName.getText().toString();
        EditText passWord = findViewById(R.id.passwordField);
        //needed for Firebase data I'm assuming
        String passwordStr = passWord.getText().toString();
        intent.putExtra(USER_NAME, userNameStr);
        startActivity(intent);

    }
}
