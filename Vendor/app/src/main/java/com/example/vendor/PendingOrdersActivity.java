package com.example.vendor;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vendor.models.offerItems;
import com.example.vendor.models.pendingOrdersItems;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PendingOrdersActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_pending_orders);

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
        fStore.collection("orders")
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


                        pendingOrdersTextView.setText("Pending Orders - "+pendingOrdersItemsArrayList.size());
                    }
                });

    }







    private void buildRecyclerView() {
        // initializing our adapter class.
        adapter = new RVadapter_pendingOrders(pendingOrdersItemsArrayList, PendingOrdersActivity.this);

        // adding layout manager to our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(this);
        pendingOrdersRV.setHasFixedSize(true);

        // setting layout manager to our recycler view.
        pendingOrdersRV.setLayoutManager(manager);

        //Item Touch swipe ko set kar raha hu
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(pendingOrdersRV);

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












    private  void deleteAndThenUpdateFirebaseDatabase(@NonNull RecyclerView.ViewHolder viewHolder)
    {

        int position=viewHolder.getAdapterPosition();
        //sabko ek ghar agge kardo fir last wala ko delte kardo bas!!!

        for (int i= position ;i<pendingOrdersItemsArrayList.size()-1;i++)
        {
            DocumentReference documentReference= fStore.collection("orders").document(String.valueOf(i));
            Map<String, Object> pendingOrdersItem= new HashMap<>();
            showLog(String.valueOf(documentReference));

            String name=pendingOrdersItemsArrayList.get(i+1).getName();
            String phone=pendingOrdersItemsArrayList.get(i+1).getPhone();
            String orderList=pendingOrdersItemsArrayList.get(i+1).getOrderList();
            String totalBillAmount=pendingOrdersItemsArrayList.get(i+1).getTotalBillAmount();


            showLog(name+"   "+totalBillAmount);

            pendingOrdersItem.put("name",name);
            pendingOrdersItem.put("phone",phone);
            pendingOrdersItem.put("orderList",orderList);
            pendingOrdersItem.put("totalBillAmount",totalBillAmount);


            documentReference.set(pendingOrdersItem).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    showLog("Added new row to fire base database-->"+ name +"  having a BILL of Rs."+totalBillAmount);

                }
            });

        }

        int last_position=pendingOrdersItemsArrayList.size()-1;

        fStore.collection("orders").document(String.valueOf(last_position))
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });

        //updateFirebaseDatabase();


    }















    ItemTouchHelper.SimpleCallback itemTouchHelperCallback=  new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT)
    {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            deleteAndThenUpdateFirebaseDatabase(viewHolder);

            pendingOrdersItemsArrayList.remove(viewHolder.getAdapterPosition());
            adapter.notifyDataSetChanged();
            saveData();

        }


    };




    private void showLog(String msg)
    {
        Log.i(TAG,msg);
    }

}