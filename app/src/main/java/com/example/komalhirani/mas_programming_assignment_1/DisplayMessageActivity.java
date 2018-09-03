package com.example.komalhirani.mas_programming_assignment_1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;
import android.view.View;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        /*intent code is based on android's developers documentation:
        https://developer.android.com/training/basics/firstapp/starting-activity#java
         */

        Intent intent = getIntent();
        String userName = intent.getStringExtra(MainActivity.USER_NAME);

        TextView userTextView = findViewById(R.id.userNameText);
        userTextView.setText(userName);
    }

    public void sendFall(View view) {
        //will allow text message to be sent to a contact
    }
}
