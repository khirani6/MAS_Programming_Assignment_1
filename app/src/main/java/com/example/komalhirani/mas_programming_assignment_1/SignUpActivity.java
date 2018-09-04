package com.example.komalhirani.mas_programming_assignment_1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String TAG = "MainActivityTag";

    private Button mCreateAccountButton;
    private EditText mEmailField;
    private EditText mPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mEmailField = findViewById(R.id.signup_email_field);
        mPasswordField = findViewById(R.id.signup_password_field);
        mCreateAccountButton = findViewById(R.id.signup_create_account_button);
        mCreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailField.getText().toString();
                String password = mPasswordField.getText().toString();
                createAccount(email.trim(), password.trim());
            }
        });

        mAuth = FirebaseAuth.getInstance();
    }

    private void createAccount(String email, String password) {
        if (password.length() >= 6) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, take user to sms button
                                Log.d(TAG, "createUserWithEmail:success");
                                Intent intent = new Intent(
                                        SignUpActivity.this,
                                        DisplayMessageActivity.class);
                                startActivity(intent);

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }

                            // ...
                        }
                    });
        } else {
            Toast.makeText(
                    SignUpActivity.this,
                    getResources().getString(R.string.password_too_short_error),
                    Toast.LENGTH_SHORT);
        }

    }
}
