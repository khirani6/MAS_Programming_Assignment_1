package com.example.komalhirani.mas_programming_assignment_1;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import static android.Manifest.permission.SEND_SMS;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DisplayMessageActivity extends AppCompatActivity {

    private static final int REQUEST_SMS = 0;

    private Button sendFallButton;
    //private String userName;
    private EditText mContactPhoneNumberField;
    public String firstName;
    public String lastName;
    private String fullName;
    private TextView userTextView;
    public User person;

    private BroadcastReceiver sentStatusReceiver, deliveredStatusReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        mContactPhoneNumberField = findViewById(R.id.display_message_contact_field);

        /*intent code is based on android's developers documentation:
        https://developer.android.com/training/basics/firstapp/starting-activity#java
         */

        //Intent intent = getIntent();
        //userName = intent.getStringExtra(MainActivity.USER_NAME);

        //TextView userTextView = findViewById(R.id.userNameText);
        //userTextView.setText(userName);
        userTextView = findViewById(R.id.userNameText);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        person = new User();
        if (user != null) {
            String email = user.getEmail();
            String emailWithCommas = email.replace('.', ',');
            Log.d("Email", email);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("Users");
            DatabaseReference userPerson = ref.child(emailWithCommas);
            DatabaseReference firstNameDataRef = userPerson.child("firstName");
            DatabaseReference lastNameDataRef = userPerson.child("lastName");

            firstNameDataRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    firstName = new String(dataSnapshot.getValue(String.class));
                    //person.setFirstName(firstName);
                    //userTextView.setText(firstName);
                    setFirstName(firstName);
                    Log.d("First name", firstName);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            lastNameDataRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    lastName = new String(dataSnapshot.getValue(String.class));
                    //person.setLastName(lastName);
                    setLastName(lastName);
                    if (lastName != null) {
                        setFullName(firstName, lastName);
                    }
                    Log.d("Last Name", lastName);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            //fullName = person.getFirstName() + " " + person.getLastName();
            //userTextView.setText(fullName);


            //boolean emailVerified = user.isEmailVerified();
        }

        sendFallButton = findViewById(R.id.fallButton);
        sendFallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFall();
            }
        });
        //String alertMessage = userName + " has fallen!";
        //Log.d("Alert message", alertMessage);

        /* Got help to integrate SMS from
        SMS Tutorial:
        https://www.androidtutorialpoint.com/basics/send-sms-programmatically-android-tutorial/
         */

        sendFallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                int hasSMSPermission = checkSelfPermission(Manifest.permission.SEND_SMS);
                if (hasSMSPermission != PackageManager.PERMISSION_GRANTED) {
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)) {
                        showMessageOKCancel("You need to allow access to Send SMS",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(new String[]{Manifest.permission.SEND_SMS},
                                                    REQUEST_SMS);
                                        }
                                    }
                                });
                        return;
                    }
                    requestPermissions(new String[]{Manifest.permission.SEND_SMS},
                            REQUEST_SMS);
                    return;
                }
                sendFall();
                }
            }
        });
    }

    public void setFirstName(String first) {
        firstName = first;
    }

    public void setLastName(String last) {
        lastName = last;
        //setFullName(firstName, lastName);
    }

    public void setFullName(String first, String last) {
        fullName = first + " " + last;
        userTextView.setText(fullName);
    }

    protected void onStart(){
        super.onStart();
        setFullName(firstName, lastName);
    }

    public void sendFall() {
        //will allow text message to be sent to a contact
        String alertMessage = fullName + " has fallen!";
        String contactNumber = mContactPhoneNumberField.getText().toString();
        //Remove whitespace and non-numeric characters
        contactNumber = contactNumber.trim();
        contactNumber = contactNumber.replaceAll("\\W", "");
        if (contactNumber.length() < 10) {
            Toast.makeText(getApplicationContext(), "Please Enter a Valid Phone Number", Toast.LENGTH_SHORT).show();
        } else {

            SmsManager sms = SmsManager.getDefault();
            PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT"), 0);
            PendingIntent deliveredIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED"), 0);
            sms.sendTextMessage(contactNumber, null, alertMessage, sentIntent, deliveredIntent);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(DisplayMessageActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public void onResume() {
        super.onResume();
        setFullName(firstName, lastName);
        sentStatusReceiver= new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent arg1) {
                String s = "Unknown Error";
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        s = "Message Sent Successfully !!";
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        s = "Generic Failure Error";
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        s = "Error : No Service Available";
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        s = "Error : Null PDU";
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        s = "Error : Radio is off";
                        break;
                    default:
                        break;
                }
            }
        };
        deliveredStatusReceiver=new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent arg1) {
                String s = "Message Not Delivered";
                switch(getResultCode()) {
                    case Activity.RESULT_OK:
                        s = "Message Delivered Successfully";
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
            }
        };
        registerReceiver(sentStatusReceiver, new IntentFilter("SMS_SENT"));
        registerReceiver(deliveredStatusReceiver, new IntentFilter("SMS_DELIVERED"));
    }

    public void onPause() {
        super.onPause();
        unregisterReceiver(sentStatusReceiver);
        unregisterReceiver(deliveredStatusReceiver);
    }

    private boolean checkPermission() {
        return ( ContextCompat.checkSelfPermission(getApplicationContext(), SEND_SMS ) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{SEND_SMS}, REQUEST_SMS);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_SMS:
                if (grantResults.length > 0 &&  grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access sms", Toast.LENGTH_SHORT).show();
                    sendFall();

                }else {
                    Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access and sms", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(SEND_SMS)) {
                            showMessageOKCancel("You need to allow access to both the permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(new String[]{SEND_SMS},
                                                        REQUEST_SMS);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }




}
