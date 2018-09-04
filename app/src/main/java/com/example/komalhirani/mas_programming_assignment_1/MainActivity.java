package com.example.komalhirani.mas_programming_assignment_1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    public static final String USER_NAME = "Profile Created";

    private FirebaseAuth mAuth;
    private Button mCreateAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCreateAccountButton = findViewById(R.id.signup);

        mCreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        mAuth = FirebaseAuth.getInstance();
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
