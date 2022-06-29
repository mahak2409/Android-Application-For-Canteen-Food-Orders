package com.titans.foodchefuser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.titans.foodchefuser.adapters.RVadapter_offers;
import com.titans.foodchefuser.models.offerItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewOffersActivity extends AppCompatActivity {


    private RecyclerView offersRV;

    // variable for our adapter class and array list
    private RVadapter_offers adapter;
    private ArrayList<offerItems> offersItemsArrayList = new ArrayList<offerItems>();

    //variable for Firebase Dataabse
    FirebaseFirestore fStore;

    private void buildRecyclerView() {
        // initializing our adapter class.
        adapter = new RVadapter_offers(offersItemsArrayList, ViewOffersActivity.this);

        // adding layout manager to our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(this);
        offersRV.setHasFixedSize(true);

        // setting layout manager to our recycler view.
        offersRV.setLayoutManager(manager);


        // setting adapter to our recycler view.
        offersRV.setAdapter(adapter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_offers);

        offersRV = findViewById(R.id.offersRV);
        fStore = FirebaseFirestore.getInstance();

        buildRecyclerView();

        DocumentReference documentReference = fStore.collection("offers").document("list");

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> offerItems;
                if (documentSnapshot.exists()) {
                    offerItems = documentSnapshot.getData();

                    for(Map.Entry<String,Object> offerVal : offerItems.entrySet()){
                        offerItems obj = new offerItems(offerVal.getKey(),offerVal.getValue().toString());
                        offersItemsArrayList.add(obj);
                        adapter.notifyItemInserted(offersItemsArrayList.size());
                    }

                }

            }
        });


    }
}