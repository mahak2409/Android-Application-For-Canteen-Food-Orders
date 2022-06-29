package com.example.vendor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    Button signOutButton;
    Button setMenuButton;
    Button setOffersButton;
    Button pendingOrdersButton;

    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso);


        //Initialization
        signOutButton = (Button) findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(this);
        setMenuButton = (Button) findViewById(R.id.setMenuButton);
        setMenuButton.setOnClickListener(this);
        setOffersButton = (Button) findViewById(R.id.setOffersButton);
        setOffersButton.setOnClickListener(this);
        pendingOrdersButton = (Button) findViewById(R.id.pendingOrdersButton);
        pendingOrdersButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

            switch (view.getId())
        {
            case R.id.signOutButton:
                //toast.makeText(this, "Sign out button pressed!!!", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                googleSignInClient.signOut();
                Intent intent= new Intent(MenuActivity.this,MainActivity.class);
                startActivity(intent);
                break;

            case R.id.setMenuButton:
                Intent intent2= new Intent(MenuActivity.this,SetMenuActivity.class);
                startActivity(intent2);
                break;

            case R.id.setOffersButton:

                //Toast.makeText(this, "set offers button pressed!!!", Toast.LENGTH_SHORT).show();
                Intent intent3= new Intent(MenuActivity.this,SetOffersActivity.class);
                startActivity(intent3);
                break;

            case R.id.pendingOrdersButton:

                //Toast.makeText(this, "set offers button pressed!!!", Toast.LENGTH_SHORT).show();
                Intent intent4= new Intent(MenuActivity.this,PendingOrdersActivity.class);
                startActivity(intent4);
                break;


        }


    }


}