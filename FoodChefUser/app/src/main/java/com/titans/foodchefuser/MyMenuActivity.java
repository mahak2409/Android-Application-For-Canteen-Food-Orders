package com.titans.foodchefuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.titans.foodchefuser.adapters.Adapter;
import com.titans.foodchefuser.models.FoodItems;

import java.util.ArrayList;
import java.util.List;

public class MyMenuActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseFirestore db;
    List<FoodItems> foodItemsList;
    Button gotocart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_menu);
        db = FirebaseFirestore.getInstance();
        gotocart = findViewById(R.id.goToCart);
        foodItemsList = new ArrayList<>();

        DocumentReference documentReference= db.collection("user").document(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).collection("cart").document("List");
        documentReference.delete();

        gotocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyMenuActivity.this,MyCart.class));
            }
        });

        fetchData();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    Boolean flag;
    private void fetchData(){
        flag = true;
        int i = 0;
        while(i<10){ //TODO

            DocumentReference documentReference= db.collection("menu").document(String.valueOf(i));

            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()){
                        FoodItems item = new FoodItems(documentSnapshot.get("foodName").toString(), documentSnapshot.get("price").toString(),
                                documentSnapshot.get("calories").toString(),documentSnapshot.get("ingredients").toString(),
                                documentSnapshot.get("vegOrNonVeg").toString(),documentSnapshot.get("bestsellerOrNot").toString(),
                                documentSnapshot.get("foodImgUrl").toString());
                        foodItemsList.add(item);
                        recyclerView.setAdapter(new Adapter(foodItemsList,MyMenuActivity.this));

                    }else {
                        flag = false;
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    flag = false;
                }
            });

            i++;
        }

    }

    @Override
    protected void onDestroy() {

        DocumentReference documentReference= db.collection("user").document(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).collection("cart").document("List");
        documentReference.delete();

        super.onDestroy();
    }
}