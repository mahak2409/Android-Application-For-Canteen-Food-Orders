package com.example.vendor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vendor.models.FoodItems;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity  implements View.OnClickListener{

    String TAG="SignUpActivity";
    EditText editTextTextUsernameSignUp,editTextTextPasswordSignUp;
    Button SignUpButton;

//    public static String currentUsername="admin";
//    public static String currentPassword="admin";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Initializations
        editTextTextUsernameSignUp = (EditText) findViewById(R.id.editTextTextUsernameSignUp);
        editTextTextPasswordSignUp = (EditText) findViewById(R.id.editTextTextPasswordSignUp);
        SignUpButton= (Button) findViewById(R.id.SignUpButton);
        SignUpButton.setOnClickListener(this);



        // calling method to load data
        // from shared prefs.
        //loadData();


//        if(savedInstanceState!= null){
//            currentUsername=savedInstanceState.getString("currentUsername");
//            currentPassword=savedInstanceState.getString("currentPassword");
//
////            Toast.makeText(this,"$activity_name is retrieving data!!", Toast.LENGTH_SHORT).show()
//
//        }
        //showLog(" current Username--->" + currentUsername +" current Password--->"+currentPassword);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.SignUpButton:




                //toast.makeText(this, "Sign in button pressed!!!", Toast.LENGTH_SHORT).show();

                MainActivity mainActivity=new MainActivity();
                mainActivity.currentUsername = editTextTextUsernameSignUp.getText().toString();
                mainActivity.currentPassword = editTextTextPasswordSignUp.getText().toString();

                showLog(" current Username--->" + mainActivity.currentUsername +" current Password--->"+mainActivity.currentPassword);



                showLog("curr username-->"+mainActivity.currentUsername+"   curr password-->"+mainActivity.currentPassword);

                break;
        }

    }


    public void showLog(String msg)
    {
        Log.i(TAG,msg);
    }


//    private void loadData() {
//        // method to load arraylist from shared prefs
//        // initializing our shared prefs with name as
//        // shared preferences.
//        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
//
//        // creating a variable for gson.
//        //Gson gson = new Gson();
//
//        // below line is to get to string present from our
//        // shared prefs if not present setting it as null.
//        //String json = sharedPreferences.getString("foodItems", null);
//        currentUsername = sharedPreferences.getString("currentUsername", null);
//        currentPassword = sharedPreferences.getString("currentPassword", null);
//
//        // below line is to get the type of our array list.
//        ////Type type = new TypeToken<ArrayList<FoodItems>>() {}.getType();
//
//        // in below line we are getting data from gson
//        // and saving it to our array list
//        ////foodItemsArrayList = gson.fromJson(json, type);
//
//
//        ////showLog("foodItemsArrayList  -->"+foodItemsArrayList);
//        // checking below if the array list is empty or not
//        if (currentUsername.equals(null)  ||  currentPassword.equals(null) )
//        {
//            // if the array list is empty
//            // creating a new array list.
//            Toast.makeText(this, "Account invalid!", Toast.LENGTH_SHORT).show();
//
//
//        }
//    }
//
//    private void saveData() {
//        // method for saving the data in array list.
//        // creating a variable for storing data in
//        // shared preferences.
//        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
//
//        // creating a variable for editor to
//        // store data in shared preferences.
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//        // creating a new variable for gson.
//        ////Gson gson = new Gson();
//
//        // getting data from gson and storing it in a string.
//        ////String json = gson.toJson(foodItemsArrayList);
//
//
//        // below line is to save data in shared
//        // prefs in the form of string.
//        editor.putString("currentUsername", currentUsername);
//        editor.putString("currentPassword", currentPassword);
//
//        // below line is to apply changes
//        // and save data in shared prefs.
//        editor.apply();
//
//        // after saving data we are displaying a toast message.
//        //Toast.makeText(this, "Saved Array List to Shared preferences. ", Toast.LENGTH_SHORT).show();
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //saveData();
    }



//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//        super.onSaveInstanceState(savedInstanceState);
//        // Save UI state changes to the savedInstanceState.
//        // This bundle will be passed to onCreate if the process is
//        // killed and restarted.
//
//
//        showLog("Saving data...");
//        savedInstanceState.putString("currentUsername", currentUsername);
//        savedInstanceState.putString("currentPassword", currentPassword);
//    }
//
//
//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        // Restore UI state from the savedInstanceState.
//        // This bundle has also been passed to onCreate.
//
//        showLog("Loading data...");
//        currentUsername = savedInstanceState.getString("currentUsername");
//        currentPassword = savedInstanceState.getString("currentPassword");
//    }
}