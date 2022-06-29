package com.example.vendor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vendor.models.FoodItems;
import com.example.vendor.models.offerItems;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SetOffersActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    String TAG = "SetOffersActivity";
    String position_text = "";


    // creating variables for our ui components.
    private Button addOffersButton, updateOffersDatabaseButton;
    private RecyclerView offersRV;

    // variable for our adapter class and array list
    private RVadapter_offers adapter;
    private static ArrayList<offerItems> offersItemsArrayList = new ArrayList<offerItems>();

    //variable for Firebase Dataabse
    FirebaseFirestore fStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_offers);


        // initializing our variables.
        addOffersButton = (Button) findViewById(R.id.addOffersButton);
        updateOffersDatabaseButton = (Button) findViewById(R.id.updateOffersDatabaseButton);
        offersRV = (RecyclerView) findViewById(R.id.offersRV);


        // calling method to load data
        // from shared prefs.
        loadData();

        // calling method to build
        // recycler view.
        buildRecyclerView();

        addOffersButton.setOnClickListener(this);
        updateOffersDatabaseButton.setOnClickListener(this);
        fStore = FirebaseFirestore.getInstance();


        // ------------------------------------ Spinner -----------------------------------------------
        //get the spinner from the xml.
        Spinner dropdown = findViewById(R.id.spinnerOffers);
        //create a list of items for the spinner.
        String[] items = new String[]{"IPL-10", "XAMOVER-20", "BIGTREAT-25", "SINGLES-15", "WELCOME-10", "BIGGIES-20"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);
        //----------------------------------------------------------------------------------------------


    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {


            case R.id.addOffersButton:
                //toast.makeText(this, "add button pressed!!!", Toast.LENGTH_SHORT).show();
                // below line is use to add data to array list. // add name, price, calories, ingredients, veg/non-veg, best seller not best-seller...
                //foodItemsArrayList.add(new FoodItems(foodnameEdittext2.getText().toString(), priceEdittext2.getText().toString(),descriptionEdittext2.getText().toString() ));


                String couponName = "";
                String couponCode = "";
                String discountPercentage = "";

                //Traversing map
                SetOffersActivity.MapExampleOffers MapExampleobj = new SetOffersActivity.MapExampleOffers();
                MapExampleobj.main();

                for (Map.Entry<String, SetOffersActivity.details> entry : MapExampleobj.map.entrySet()) {
                    String key = entry.getKey();
                    SetOffersActivity.details b = entry.getValue();
                    showLog(key + " Details:");
                    showLog(b.couponName + " " + b.couponCode + " " + b.discountPercentage);

                    if (b.couponName.equals(position_text)) {
                        couponName = b.couponName;
                        couponCode = b.couponCode;
                        discountPercentage = b.discountPercentage;

                    }
                }


                //foodItemsArrayList.add(new FoodItems( foodName, priceEdittext2.getText().toString(),descriptionEdittext2.getText().toString() ));
                offersItemsArrayList.add(new offerItems(couponName, couponCode, discountPercentage));

                // notifying adapter when new data added.
                adapter.notifyItemInserted(offersItemsArrayList.size());
                //Toast.makeText(this, "add button pressed!!!-->"+foodItemsArrayList, Toast.LENGTH_SHORT).show();


                //Toast.makeText(this, "save button pressed!!!", Toast.LENGTH_SHORT).show();
                // calling method to save data in shared prefs.
                saveData();
                break;


            case R.id.updateOffersDatabaseButton:
                //toast.makeText(this,"Updating Firebase Database! ",Toast.LENGTH_SHORT).show();
                updateFirebaseDatabase();
                break;

        }


    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        position_text = adapterView.getItemAtPosition(position).toString();
        //toast.makeText(adapterView.getContext(),position_text, Toast.LENGTH_SHORT).show();
        ((TextView) view).setTextColor(ContextCompat.getColor(SetOffersActivity.this, R.color.black));


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private void buildRecyclerView() {
        // initializing our adapter class.
        adapter = new RVadapter_offers(offersItemsArrayList, SetOffersActivity.this);

        // adding layout manager to our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(this);
        offersRV.setHasFixedSize(true);

        // setting layout manager to our recycler view.
        offersRV.setLayoutManager(manager);

        //Item Touch swipe ko set kar raha hu
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(offersRV);

        // setting adapter to our recycler view.
        offersRV.setAdapter(adapter);
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
        String json = sharedPreferences.getString("offerItems", null);

        // below line is to get the type of our array list.
        Type type = new TypeToken<ArrayList<offerItems>>() {
        }.getType();

        // in below line we are getting data from gson
        // and saving it to our array list
        offersItemsArrayList = gson.fromJson(json, type);


        showLog("offersItemsArrayList  -->" + offersItemsArrayList);
        // checking below if the array list is empty or not
        if (offersItemsArrayList == null) {
            // if the array list is empty
            // creating a new array list.
            offersItemsArrayList = new ArrayList<>();
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
        String json = gson.toJson(offersItemsArrayList);

        // below line is to save data in shared
        // prefs in the form of string.
        editor.putString("offerItems", json);

        // below line is to apply changes
        // and save data in shared prefs.
        editor.apply();

        // after saving data we are displaying a toast message.
        //toast.makeText(this, "Saved Array List to Shared preferences. ", Toast.LENGTH_SHORT).show();
    }


    private void updateFirebaseDatabase() {
        DocumentReference documentReference = fStore.collection("offers").document("list");

        documentReference.delete();

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> offerItem;
                if (documentSnapshot.exists()) {
                    offerItem = documentSnapshot.getData();

                } else {
                    offerItem = new HashMap<>();
                }
                showLog(String.valueOf(documentReference));
                for (int counter = 0; counter < offersItemsArrayList.size(); counter++) {
                    int finalCounter = counter;
                    String couponCode = offersItemsArrayList.get(finalCounter).getCouponCode();
                    String discountPercentage = offersItemsArrayList.get(finalCounter).getDiscountPercentage();
                    offerItem.put(couponCode, discountPercentage);

                }
                documentReference.set(offerItem);

            }
        });


    }


    private void deleteAndThenUpdateFirebaseDatabase(@NonNull RecyclerView.ViewHolder viewHolder) {

        int position = viewHolder.getAdapterPosition();
        //sabko ek ghar agge kardo fir last wala ko delte kardo bas!!!
        DocumentReference documentReference = fStore.collection("offers").document("list");

        documentReference.delete();

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> offerItem;
                if (documentSnapshot.exists()) {
                    offerItem = documentSnapshot.getData();

                } else {
                    offerItem = new HashMap<>();
                }
                showLog(String.valueOf(documentReference));
                for (int i = position; i < offersItemsArrayList.size() - 1; i++) {

                    String couponCode = offersItemsArrayList.get(i + 1).getCouponCode();
                    String discountPercentage = offersItemsArrayList.get(i + 1).getDiscountPercentage();
                    offerItem.put(couponCode, discountPercentage);
                }
                documentReference.set(offerItem);

            }
        });


//        int last_position = offersItemsArrayList.size() - 1;
//        String log = "";
//        for(offerItems item : offersItemsArrayList){
//            log+=item.getCouponCode()+", ";
//        }
//        Log.d(TAG, "deleteAndThenUpdateFirebaseDatabase: "+ log);
//        String cc = offersItemsArrayList.get(last_position).getCouponCode();
//
//        fStore.collection("offers").document("list")
//                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if(documentSnapshot.exists()){
//                    Map<String, Object> map = documentSnapshot.getData();
//                    map.remove(cc);
//                    fStore.collection("offers").document("list")
//                            .set(map);
//                }
//            }
//        });

        updateFirebaseDatabase();


    }


    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

//            deleteAndThenUpdateFirebaseDatabase(viewHolder);
            offersItemsArrayList.remove(viewHolder.getAdapterPosition());
            updateFirebaseDatabase();
            adapter.notifyDataSetChanged();
            saveData();

        }
    };


    class details {

        String couponName = "";
        String couponCode = "";
        String discountPercentage = "";

        public details(String couponName, String couponCode, String discountPercentage) {
            this.couponName = couponName;
            this.couponCode = couponCode;
            this.discountPercentage = discountPercentage;
        }

    }


    public class MapExampleOffers {

        //Creating map of Books
        Map<String, SetOffersActivity.details> map = new HashMap<String, SetOffersActivity.details>();

        public void main() {
            //Creating Books


            SetOffersActivity.details offer1Obj = new SetOffersActivity.details("IPL-10", "IPL10", "10");
            SetOffersActivity.details offer2Obj = new SetOffersActivity.details("XAMOVER-20", "XAMOVER20", "20");
            SetOffersActivity.details offer3Obj = new SetOffersActivity.details("BIGTREAT-25", "BIGTREAT25", "25");
            SetOffersActivity.details offer4Obj = new SetOffersActivity.details("SINGLES-15", "SINGLES15", "15");
            SetOffersActivity.details offer5Obj = new SetOffersActivity.details("WELCOME-10", "WELCOME10", "10");
            SetOffersActivity.details offer6Obj = new SetOffersActivity.details("BIGGIES-20", "BIGGIES20", "20");

            //Adding Books to map
            map.put("IPL-10", offer1Obj);
            map.put("XAMOVER-20", offer2Obj);
            map.put("BIGTREAT-25", offer3Obj);
            map.put("SINGLES-15", offer4Obj);
            map.put("WELCOME-10", offer5Obj);
            map.put("BIGGIES-20", offer6Obj);

//            Traversing map
//            for(Map.Entry<String, details> entry:map.entrySet()){
//                String key=entry.getKey();
//                details b=entry.getValue();
//                showLog(key+" Details:");
//                showLog(b.foodName+" "+b.price+" "+b.calories+" "+b.ingredients+" "+b.vegOrNonVeg+" "+b.bestsellerOrNot);
//            }

        }
    }

    public void showLog(String msg) {
        Log.i(TAG, msg);
    }

}