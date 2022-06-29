package com.example.vendor;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private GoogleSignInClient googleSignInClient;
    private final static int  RC_SIGN_IN=123;
    private FirebaseAuth mAuth;
    String TAG= "MainActivity";
    public static String currentUsername="admin";
    public static String currentPassword="admin";

    ProgressDialog progressDialog;


    Button signInButton,loginButton;
    EditText editTextTextUsername,editTextTextPassword;

    TextView createAcountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);

        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();


        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso);



        //Initialization
        signInButton = (Button) findViewById(R.id.signInButton);
        signInButton.setOnClickListener(this);
        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);

        editTextTextUsername= (EditText) findViewById(R.id.editTextTextUsername);
        editTextTextPassword= (EditText) findViewById(R.id.editTextTextPassword);

        createAcountTextView= (TextView) findViewById(R.id.createAcountTextView);
        createAcountTextView.setOnClickListener(this);

        //showLog(savedInstanceState.toString());

//        if(savedInstanceState!= null){
            showLog("Loading data...");
//            currentUsername = savedInstanceState.getString("currentUsernameSavedInstance");
//            currentPassword = savedInstanceState.getString("currentPasswordSavedInstance");
//            Toast.makeText(this,"$activity_name is retrieving data!!", Toast.LENGTH_SHORT).show()

//        }


    }



    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
            
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {




        try {




            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = task.getResult(ApiException.class);
            //Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
            showLog("firebaseAuthWithGoogle:" + account.getEmail());

            firebaseAuthWithGoogle(account);


            if(progressDialog.isShowing())
            {
                progressDialog.dismiss();
            }

            } 
            catch (ApiException e) {


                //toast.makeText(this, "cred-->"+e.toString(), Toast.LENGTH_SHORT).show();
                // Google Sign In failed, update UI appropriately
                //Log.w(TAG, "Google sign in failed", e);
                showLog("Google sign in failed " + e);


                if(progressDialog.isShowing())
                {
                    progressDialog.dismiss();
                }
            }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        showLog("credential-->"+credential);


        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            //toast.makeText(getApplicationContext(), "success to Login", Toast.LENGTH_SHORT).show();

                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            showLog( "signInWithCredential:success");

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {


                            //toast.makeText(getApplicationContext(), "FAILED to Login", Toast.LENGTH_SHORT).show();

                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            showLog("signInWithCredential:failure"+ task.getException());

                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser firebaseUser)
    {
        if(firebaseUser!=null)
        {
            Intent menuActivityIntent = new Intent( MainActivity.this, MenuActivity.class);
            startActivity(menuActivityIntent);
            finish();
        }
        else
        {
            //toast.makeText(this, "Failed to Login", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.signInButton:


                progressDialog = new ProgressDialog(this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Logging In ...");
                progressDialog.show();


                //toast.makeText(this, "Sign in button pressed!!!", Toast.LENGTH_SHORT).show();
                signIn();
                break;

            case R.id.loginButton:

                progressDialog = new ProgressDialog(this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Logging In ...");
                progressDialog.show();

                //SignUpActivity signUpActivity= new SignUpActivity();

                showLog(" current Username--->" + currentUsername +" current Password--->"+currentPassword);

                if((editTextTextUsername.getText().toString().equals( currentUsername)) && (editTextTextPassword.getText().toString().equals( currentPassword)))
                {
                    Intent menuActivityIntent = new Intent( MainActivity.this, MenuActivity.class);
                    startActivity(menuActivityIntent);
                    finish();
                }
                else
                {        Toast.makeText(this, "Invalid Username or Password!", Toast.LENGTH_SHORT).show();             }


                if(progressDialog.isShowing())
                {
                    progressDialog.dismiss();
                }
                break;




            case R.id.createAcountTextView:
                //Toast.makeText(this, "Create ACcount!!!", Toast.LENGTH_SHORT).show();

                Intent signUpActivityIntent = new Intent( MainActivity.this, SignUpActivity.class);
                startActivity(signUpActivityIntent);
                break;






            //case R.id.signInButton:
                //Toast.makeText(this, "Sign in button pressed!!!", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null)
        {updateUI(currentUser);}

    }

    public void showLog(String msg)
    {
        Log.i(TAG,msg);
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.


        showLog("Saving data...");
        savedInstanceState.putString("currentUsernameSavedInstance", currentUsername);
        savedInstanceState.putString("currentPasswordSavedInstance", currentPassword);
    }


    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.

        showLog("Loading data...");
        currentUsername = savedInstanceState.getString("currentUsernameSavedInstance");
        currentPassword = savedInstanceState.getString("currentPasswordSavedInstance");
    }

}