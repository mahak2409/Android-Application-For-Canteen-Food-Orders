package com.titans.foodchefuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.titans.foodchefuser.models.FoodItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodDetail extends AppCompatActivity {

    TextView food_name, food_price, food_description;
    Button add_to_cart;

    FirebaseAuth mAuth;

    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        mAuth = FirebaseAuth.getInstance();
        food_name = findViewById(R.id.itemName);
        food_price = findViewById(R.id.itemPrice);
        food_description = findViewById(R.id.itemDescription);
        add_to_cart = findViewById(R.id.addToCartButton);
        db = FirebaseFirestore.getInstance();


        Intent intent = getIntent();

        food_name.setText(intent.getStringExtra("name"));
        food_description.setText(intent.getStringExtra("description"));
        food_price.setText("Rs"+intent.getStringExtra("price"));

        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference documentReference= db.collection("user").document(mAuth.getCurrentUser().getPhoneNumber()).collection("cart").document("list");
                FoodItems foodItem = new FoodItems(intent.getStringExtra("name"),"Rs"+intent.getStringExtra("price"),
                        intent.getStringExtra("calories"),intent.getStringExtra("ingredients"),intent.getStringExtra("vegOrNonVeg"),
                        intent.getStringExtra("bestSellerOrnot"),intent.getStringExtra("foodImgUrl"));
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            List<FoodItems> cart = (List<FoodItems>) documentSnapshot.get("list");
                            cart.add(foodItem);

                            Map<String, Object> docData = new HashMap<>();
                            docData.put("list", cart);
                            db.collection("user").document(mAuth.getCurrentUser().getPhoneNumber()).collection("cart").document("list")
                                    .set(docData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(FoodDetail.this, "Added to cart", Toast.LENGTH_SHORT).show();
                                            Log.d("update cart", "DocumentSnapshot successfully written!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            Log.w("update cart", "Error writing document", e);
                                        }
                                    });


                        }else {

                            List<FoodItems> cart = new ArrayList<>();
                            cart.add(foodItem);

                            Map<String, Object> docData = new HashMap<>();
                            docData.put("list", cart);
                            db.collection("user").document(mAuth.getCurrentUser().getPhoneNumber()).collection("cart").document("list")
                                    .set(docData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(FoodDetail.this, "Added to cart", Toast.LENGTH_SHORT).show();
                                            Log.d("update cart", "DocumentSnapshot successfully written!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("update cart", "Error writing document", e);
                                        }
                                    });


                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });

            }
        });


        }
}