package com.titans.foodchefuser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText mPhone;
    EditText mName;

    EditText mCountryCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        mPhone = findViewById(R.id.phoneNo);
        mName = findViewById(R.id.user_name_et);
        mCountryCode = findViewById(R.id.ccode);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null)
        {updateUI(currentUser);}

    }

    private void updateUI(FirebaseUser firebaseUser)
    {
        if(firebaseUser!=null)
        {
            Intent mainActivityIntent = new Intent( LoginActivity.this, MainActivity.class);
            startActivity(mainActivityIntent);
            finish();
        }
        else
        {
            Toast.makeText(this, "Failed to Login", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendCode(View view) {

        String ccd = mCountryCode.getText().toString();
        if (ccd.isEmpty()) {
            mCountryCode.setError("Country Code is required");
            mCountryCode.requestFocus();
            return;
        }


        String phoneString = mPhone.getText().toString();
        String nameString = mName.getText().toString();

        if (nameString.isEmpty()) {
            mName.setError("Name is required");
            mName.requestFocus();
            return;
        }

        if (phoneString.isEmpty()) {
            mPhone.setError("Phone number is required");
            mPhone.requestFocus();
            return;
        }

        if (phoneString.length() < 10) {
            mPhone.setError("Please enter a valid phone");
            mPhone.requestFocus();
            return;

        }

        phoneString = ccd + phoneString;

        Intent intent = new Intent(LoginActivity.this,OtpVerification.class);
        intent.putExtra("PHONE",phoneString);
        intent.putExtra("NAME",mName.getText().toString());
        startActivity(intent);
    }


}