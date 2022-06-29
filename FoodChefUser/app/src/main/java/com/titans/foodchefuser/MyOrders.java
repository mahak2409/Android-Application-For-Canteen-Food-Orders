package com.titans.foodchefuser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.titans.foodchefuser.models.pendingOrdersItems;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyOrders extends AppCompatActivity {

    String TAG="PendingOrdersActivity";
    String position_text="";


    // creating variables for our ui components.
    private RecyclerView pendingOrdersRV;

    // variable for our adapter class and array list
    private RVadapter_pendingOrders adapter;
    private ArrayList<pendingOrdersItems> pendingOrdersItemsArrayList = new ArrayList<pendingOrdersItems>();

    //variable for Firebase Database
    FirebaseFirestore fStore;
    ProgressDialog progressDialog;


    TextView pendingOrdersTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data ...");
        progressDialog.show();





        // initializing our variables.
        pendingOrdersRV = (RecyclerView) findViewById(R.id.pendingOrdersRV);
        pendingOrdersTextView = (TextView) findViewById(R.id.pendingOrdersTextView);


        // calling method to load data
        // from shared prefs.
        //loadData();

        // calling method to build
        // recycler view.
        buildRecyclerView();
        fStore =  FirebaseFirestore.getInstance();


        EventChangeListener();






    }



    private  void  EventChangeListener()
    {
        fStore.collection("user").document(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).collection("My Orders")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null)
                        {

                            if(progressDialog.isShowing())
                            {
                                progressDialog.dismiss();
                            }
                            showLog(error.getMessage());
                            return;
                        }

                        for(DocumentChange dc : value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){
                                pendingOrdersItemsArrayList.add(dc.getDocument().toObject(pendingOrdersItems.class));
                            }

                            adapter.notifyDataSetChanged();


                            if(progressDialog.isShowing())
                            {
                                progressDialog.dismiss();
                            }
                        }


                        pendingOrdersTextView.setText("My Orders - "+pendingOrdersItemsArrayList.size());
                        if(pendingOrdersItemsArrayList.size()==0)
                            progressDialog.dismiss();
                    }
                });

    }







    private void buildRecyclerView() {
        // initializing our adapter class.
        adapter = new RVadapter_pendingOrders(pendingOrdersItemsArrayList, MyOrders.this);

        // adding layout manager to our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(this);
        pendingOrdersRV.setHasFixedSize(true);

        // setting layout manager to our recycler view.
        pendingOrdersRV.setLayoutManager(manager);

        // setting adapter to our recycler view.
        pendingOrdersRV.setAdapter(adapter);
    }


    private void loadData() {
        // method to load arraylist from shared prefs
        // initializing our shared prefs with name as
        // shared preferences.
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);

        // creating a variable for gson.
        Gson gson = new Gson();

        // below line is to get to string present from our
        // shared prefs if not present setting it as null.
        String json = sharedPreferences.getString("pendingOrdersItems", null);

        // below line is to get the type of our array list.
        Type type = new TypeToken<ArrayList<pendingOrdersItems>>() {}.getType();

        // in below line we are getting data from gson
        // and saving it to our array list
        pendingOrdersItemsArrayList = gson.fromJson(json, type);


        showLog("pendingOrdersItemsArrayList  -->"+pendingOrdersItemsArrayList);
        // checking below if the array list is empty or not
        if (pendingOrdersItemsArrayList == null) {
            // if the array list is empty
            // creating a new array list.
            pendingOrdersItemsArrayList = new ArrayList<>();
        }
    }



    private void saveData() {
        // method for saving the data in array list.
        // creating a variable for storing data in
        // shared preferences.
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);

        // creating a variable for editor to
        // store data in shared preferences.
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // creating a new variable for gson.
        Gson gson = new Gson();

        // getting data from gson and storing it in a string.
        String json = gson.toJson(pendingOrdersItemsArrayList);

        // below line is to save data in shared
        // prefs in the form of string.
        editor.putString("pendingOrdersItems", json);

        // below line is to apply changes
        // and save data in shared prefs.
        editor.apply();

        // after saving data we are displaying a toast message.
        //toast.makeText(this, "Saved Array List to Shared preferences. ", Toast.LENGTH_SHORT).show();
    }




    private void showLog(String msg)
    {
        Log.i(TAG,msg);
    }

}