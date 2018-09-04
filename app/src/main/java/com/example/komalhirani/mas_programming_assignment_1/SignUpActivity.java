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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String TAG = "MainActivityTag";

    private Button mCreateAccountButton;
    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mFirstNameField;
    private EditText mLastNameField;

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
        mFirstNameField = findViewById(R.id.signup_first_name);
        mLastNameField = findViewById(R.id.signup_last_name);

        mAuth = FirebaseAuth.getInstance();

    }

    private void createAccount(final String email, String password) {
        if (password.length() >= 6) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, take user to sms button

                                String firstName = mFirstNameField.getText().toString();
                                String lastName = mLastNameField.getText().toString();

                                User user = new User(firstName, lastName, email);
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference ref = database.getReference("Users");
                                ref.child(user.getEncodedEmail()).setValue(user);

                                Log.d(TAG, "createUserWithEmail:success");

                                Toast.makeText(getApplicationContext(),
                                        getResources().getString(R.string.signup_account_created_confirmation),
                                        Toast.LENGTH_SHORT).show();
                                onBackPressed();
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
                    Toast.LENGTH_SHORT).show();
        }

    }

}
