package com.example.komalhirani.mas_programming_assignment_1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    //public static final String USER_NAME = "Profile Created";

    private FirebaseAuth mAuth;
    private Button mCreateAccountButton;
    private EditText emailField;
    private EditText passwordField;
    private Button mLoginButton;

    private String TAG = "MainActivityTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCreateAccountButton = findViewById(R.id.signup);
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        mLoginButton = findViewById(R.id.submitButton);

        mCreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);

            }

        });
        mLoginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString();
                String pass = passwordField.getText().toString();
                loginSent(email, pass);
            }
        });

        mAuth = FirebaseAuth.getInstance();
    }

    private void loginSent(String email, String password) {
        /*intent code is based on android's developers documentation:
        https://developer.android.com/training/basics/firstapp/starting-activity#java
         */
        /*Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText userName = findViewById(R.id.emailField);
        String userNameStr = userName.getText().toString();
        EditText passWord = findViewById(R.id.passwordField);
        //needed for Firebase data I'm assuming
        String passwordStr = passWord.getText().toString();
        intent.putExtra(USER_NAME, userNameStr);
        startActivity(intent);*/

        //Attempting Firebase
        if (password.length() >= 6) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                Intent intent = new Intent(
                                        MainActivity.this,
                                        DisplayMessageActivity.class);
                                startActivity(intent);

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

        } else {
            Toast.makeText(
                    MainActivity.this,
                    getResources().getString(R.string.password_too_short_error),
                    Toast.LENGTH_SHORT);
        }

    }
}
