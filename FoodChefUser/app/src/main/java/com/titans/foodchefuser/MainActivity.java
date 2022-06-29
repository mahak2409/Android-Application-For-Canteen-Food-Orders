package com.titans.foodchefuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button orderFoodButton, signout, myOrdersButton, buyPremiuimBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        orderFoodButton = findViewById(R.id.orderFoodButton);
        myOrdersButton = findViewById(R.id.myOrdersButton);
        buyPremiuimBtn = findViewById(R.id.buyPremiuimButton);
        signout = findViewById(R.id.signOutButton);

        FirebaseFirestore.getInstance().collection("Premium Members").document("list").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                        String formattedDate = df.format(c);
                        Map<String, Object> map = documentSnapshot.getData();

                        if (map.containsKey(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())) {
                            buyPremiuimBtn.setVisibility(View.GONE);
                            if (getActionBar() != null)
                                getActionBar().setTitle("PREMIUM");
                        }

                    }
                }
            }
        });

        orderFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(MainActivity.this, MyMenuActivity.class);
                startActivity(intent2);
            }
        });

        myOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(MainActivity.this, MyOrders.class);
                startActivity(intent2);
            }
        });

        buyPremiuimBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(MainActivity.this, SubscriptionActivity.class);
                startActivity(intent2);
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });


    }
}