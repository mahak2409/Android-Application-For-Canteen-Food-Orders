package com.titans.foodchefuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SubscriptionActivity extends AppCompatActivity implements PaymentResultListener {

    Button buy;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        //Payment Button
        buy = findViewById(R.id.buy_btn);
        //Total bill in the cart
        String bill = "500";
        //Conversion and round off
        int final_amount = Math.round(Float.parseFloat(bill) * 100);
        //Actions performed on clicking the payment button
        buy.setOnClickListener(new View.OnClickListener() {
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
                    object.put("amount", final_amount);
                    //Setting the contact number
                    object.put("prefill.contact", "8130782929");
                    checkout.open(SubscriptionActivity.this, object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    @Override
    public void onPaymentSuccess(String s) {
        //This runs when payment is successful
        //Initialize alert dialogue
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Setting message
        builder.setMessage(s);
        //Setting title
        builder.setTitle("Payment ID");
        //Displaying alert dialogue box
        builder.show();

        db.collection("Premium Members").document("list").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()) {
                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                        String formattedDate = df.format(c);
                        Map<String, Object> map = documentSnapshot.getData();
                        map.put(mAuth.getCurrentUser().getPhoneNumber(), formattedDate);
                        db.collection("Premium Members").document("list").set(map);
                    }else{

                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                        String formattedDate = df.format(c);
                        Map<String, Object> map = new HashMap<>();
                        map.put(mAuth.getCurrentUser().getPhoneNumber(), formattedDate);
                        db.collection("Premium Members").document("list").set(map);

                    }

                    Intent intent = new Intent(SubscriptionActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        });


    }

    @Override
    public void onPaymentError(int i, String s) {
        //This runs when payment is unsuccessful
        //Displaying the toast message
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

    }
}
