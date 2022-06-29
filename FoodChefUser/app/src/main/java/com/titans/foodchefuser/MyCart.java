package com.titans.foodchefuser;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.titans.foodchefuser.adapters.Adapter;
import com.titans.foodchefuser.adapters.CartAdapter;
import com.titans.foodchefuser.models.CartItems;
import com.titans.foodchefuser.models.FoodItems;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyCart extends AppCompatActivity implements PaymentResultListener {

    RecyclerView recyclerView;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    TextView totalCart;
    EditText couponCodeEt;
    Button placeOrderBtn, applyBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();
        couponCodeEt = findViewById(R.id.offerCodeET);
        applyBtn = findViewById(R.id.apply_btn);
        recyclerView = findViewById(R.id.recyclerViewCart);
        placeOrderBtn = findViewById(R.id.placeOrderButton);
        placeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Checkout checkout = new Checkout();
                //Setting the checkout image
                checkout.setImage(R.mipmap.rzp_logo);
                //Setting the checkout id
                checkout.setKeyID("rzp_test_Bl3pGNpilQ1bja");
                //Initializing the json object
                JSONObject object = new JSONObject();
                try {
                    //Inserting my email id
                    object.put("prefill.email", "parulsikri@gmail.com");
                    //Setting the color
                    object.put("theme.color", "#0093DD");
                    //Setting the currency system
                    object.put("currency", "INR");
                    //Inserting the name of the payment gateway
                    object.put("name", "CodeChef Payment");
                    //Description of the payment gateway
                    object.put("description", "Your own payment way");
                    //Inserting the final amount
                    object.put("amount", total*100);
                    //Setting the contact number
                    object.put("prefill.contact", "8130782929");
                    checkout.open(MyCart.this, object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        totalCart = findViewById(R.id.totalCart);
        try {

            fetchData();
        } catch (Exception e) {
            e.printStackTrace();
        }

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String coupon = couponCodeEt.getText().toString();

                if(coupon.length()==0){
                    couponCodeEt.setError("Enter Coupon Code");
                    couponCodeEt.requestFocus();
                    return;
                }

                DocumentReference documentReference = db.collection("offers").document("list");

                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Object> offerItem;
                        if (documentSnapshot.exists()) {
                            offerItem = documentSnapshot.getData();
                            if(offerItem.containsKey(coupon)){
                                int discountPerc = Integer.parseInt((String) offerItem.get(coupon));
                                total = total - ((discountPerc*total)/100);
                                totalCart.setText("₹" + total.toString());
                            }else{
                                couponCodeEt.setError("Invalid Code");
                            }
                        }else{
                            couponCodeEt.setError("Invalid Code");
                        }
                    }
                });


            }
        });



    }

    Double total;
    final String[] orderItemsList = {""};

    private void fetchData() {


        DocumentReference documentReference= db.collection("user").document(mAuth.getCurrentUser().getPhoneNumber()).collection("cart").document("List");

        total = 0.0;

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        Map<String,Object> cart = document.getData();
                        List<CartItems> cartItems = new ArrayList<>();
                        String name;
                        int price;
                        String qty;

                        for(Map.Entry<String,Object> map : cart.entrySet()){
                            String val = map.getValue().toString();
                            List<String> values = Arrays.asList(val.split(","));
                            name = map.getKey();
                            qty = values.get(0);
                            price = Integer.parseInt(values.get(0) )* Integer.parseInt(values.get(1));
                            Toast.makeText(getApplicationContext(),"Name "+name+" qty"+" price "+price,Toast.LENGTH_LONG).show();
                            CartItems cartObj = new CartItems(name,price,qty);
                            cartItems.add(cartObj);
                            total+=price;
                            orderItemsList[0] += name+" "+qty+"," ;
                        }


                        recyclerView.setAdapter(new CartAdapter(cartItems,MyCart.this));
                        totalCart.setText("₹" + total.toString());
                    }else{
                        Toast.makeText(getApplicationContext(),"Cart is Empty",Toast.LENGTH_LONG).show();
                    }
                }

            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });


    }



    @Override
    public void onPaymentSuccess(String s) {
//        Long tsLong = System.currentTimeMillis()/1000;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Setting message
        builder.setMessage(s);
        //Setting title
        builder.setTitle("Order Placed");
        //Displaying alert dialogue box
        builder.show();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        db.collection("orders").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                count++;
                            }

//                            String ts = tsLong.toString();
                            Map<String,String> orderValue = new HashMap<>();
                            orderValue.put("name", mAuth.getCurrentUser().getDisplayName());
                            orderValue.put("phone", mAuth.getCurrentUser().getPhoneNumber());
                            orderValue.put("totalBillAmount", total.toString());
                            String orderList = orderItemsList[0].substring(0,orderItemsList[0].length()-1);
                            orderValue.put("orderList", orderList);
                            db.collection("orders").document(count+"").set(orderValue);
                            db.collection("user").document(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).collection("My Orders").document(count+"").set(orderValue);
                            DocumentReference documentReference= db.collection("user").document(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).collection("cart").document("List");
                            documentReference.delete();

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
//                            String ts = tsLong.toString();
                            Map<String,String> orderValue = new HashMap<>();
                            orderValue.put("name", mAuth.getCurrentUser().getDisplayName());
                            orderValue.put("phone", mAuth.getCurrentUser().getPhoneNumber());
                            orderValue.put("totalBillAmount", total.toString());
                            String orderList = orderItemsList[0].substring(0,orderItemsList[0].length()-1);
                            orderValue.put("orderList", orderList);
                            db.collection("orders").document("0").set(orderValue);
                            db.collection("user").document(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).collection("My Orders").document("0").set(orderValue);
                            DocumentReference documentReference= db.collection("user").document(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).collection("cart").document("List");
                            documentReference.delete();

                        }

                        Intent intent = new Intent(MyCart.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });



    }

    @Override
    public void onPaymentError(int i, String s) {

    }

    public void viewOffers(View view) {

        startActivity(new Intent(MyCart.this,ViewOffersActivity.class));

    }
}