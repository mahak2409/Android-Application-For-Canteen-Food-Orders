package com.example.vendor;

import static android.os.SystemClock.sleep;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SetMenuActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{


    String TAG="SetMenuActivity";
    String position_text="";


    // creating variables for our ui components.
    private Button addButton, updateDatabaseButton;
    private RecyclerView menuRV;

    // variable for our adapter class and array list
    private RVadapter adapter;
    private static ArrayList<FoodItems> foodItemsArrayList = new ArrayList<FoodItems>();

    //variable for Firebase Dataabse
    FirebaseFirestore fStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_menu);



        // initializing our variables.
        addButton = (Button) findViewById(R.id.addButton);
        updateDatabaseButton =(Button) findViewById (R.id.updateDatabaseButton);
        menuRV = (RecyclerView) findViewById(R.id.menuRV);


        // calling method to load data
        // from shared prefs.
        loadData();

        // calling method to build
        // recycler view.
        buildRecyclerView();

        addButton.setOnClickListener(this);
        updateDatabaseButton.setOnClickListener(this);
        fStore =  FirebaseFirestore.getInstance();




        // ------------------------------------ Spinner -----------------------------------------------
        //get the spinner from the xml.
        Spinner dropdown = findViewById(R.id.spinnerMenu);
        //create a list of items for the spinner.
        String[] items = new String[]{"maggie", "roll", "noodles","friedRice","paneerMasala","paneerParatha","cornChat","patties","biryani","boiledEgg","paoBhaji"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);
        //----------------------------------------------------------------------------------------------




    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        //toast.makeText(this, "Resume state-->"+ foodItemsArrayList.size(), Toast.LENGTH_SHORT).show();
//
//    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {


            case R.id.addButton:
                //Toast.makeText(this, "add button pressed!!!", Toast.LENGTH_SHORT).show();
                // below line is use to add data to array list. // add name, price, calories, ingredients, veg/non-veg, best seller not best-seller...
                //foodItemsArrayList.add(new FoodItems(foodnameEdittext2.getText().toString(), priceEdittext2.getText().toString(),descriptionEdittext2.getText().toString() ));


                String foodName= "";
                String price="";

                String calories="";
                String ingredients="";

                String vegOrNonVeg="";
                String bestsellerOrNot="";

                //Bitmap foodImg= BitmapFactory.decodeResource(getResources(), R.drawable.maggie);
                String foodImgUrl="";


                //Traversing map
                MapExample MapExampleobj = new MapExample();
                MapExampleobj.main();

                for(Map.Entry<String, details> entry:MapExampleobj.map.entrySet())
                {
                    String key=entry.getKey();
                    details b=entry.getValue();
                    showLog(key+" Details:");
                    showLog(b.foodName+" "+b.price+" "+b.calories+" "+b.ingredients+" "+b.vegOrNonVeg+" "+b.bestsellerOrNot+" "+b.foodImgUrl.toString());

                    if(b.foodName.equals(position_text))
                    {
                        foodName=b.foodName;
                        price=b.price;
                        calories=b.calories;
                        ingredients = b.ingredients;
                        vegOrNonVeg=b.vegOrNonVeg;
                        bestsellerOrNot=b.bestsellerOrNot;
                        foodImgUrl=b.foodImgUrl;

                    }
                }



                //foodItemsArrayList.add(new FoodItems( foodName, priceEdittext2.getText().toString(),descriptionEdittext2.getText().toString() ));
                foodItemsArrayList.add(new FoodItems( foodName, price,calories,ingredients,vegOrNonVeg,bestsellerOrNot,foodImgUrl ));

                // notifying adapter when new data added.
                adapter.notifyItemInserted(foodItemsArrayList.size());
                //Toast.makeText(this, "add button pressed!!!-->"+foodItemsArrayList, Toast.LENGTH_SHORT).show();


                //Toast.makeText(this, "save button pressed!!!", Toast.LENGTH_SHORT).show();
                // calling method to save data in shared prefs.
                saveData();
                break;



            case R.id.updateDatabaseButton:
                //Toast.makeText(this,"Updating Firebase Database! ",Toast.LENGTH_SHORT).show();
                updateFirebaseDatabase();
                break;

        }


    }



    public void showLog(String msg)
    {
        Log.i(TAG,msg);
    }


    private void buildRecyclerView() {
        // initializing our adapter class.
        adapter = new RVadapter(foodItemsArrayList, SetMenuActivity.this);

        // adding layout manager to our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(this);
        menuRV.setHasFixedSize(true);

        // setting layout manager to our recycler view.
        menuRV.setLayoutManager(manager);

        //Item Touch swipe ko set kar raha hu
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(menuRV);

        // setting adapter to our recycler view.
        menuRV.setAdapter(adapter);
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
        String json = sharedPreferences.getString("foodItems", null);

        // below line is to get the type of our array list.
        Type type = new TypeToken<ArrayList<FoodItems>>() {}.getType();

        // in below line we are getting data from gson
        // and saving it to our array list
        foodItemsArrayList = gson.fromJson(json, type);


        showLog("foodItemsArrayList  -->"+foodItemsArrayList);
        // checking below if the array list is empty or not
        if (foodItemsArrayList == null) {
            // if the array list is empty
            // creating a new array list.
            foodItemsArrayList = new ArrayList<>();
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
        String json = gson.toJson(foodItemsArrayList);

        // below line is to save data in shared
        // prefs in the form of string.
        editor.putString("foodItems", json);

        // below line is to apply changes
        // and save data in shared prefs.
        editor.apply();

        // after saving data we are displaying a toast message.
        //Toast.makeText(this, "Saved Array List to Shared preferences. ", Toast.LENGTH_SHORT).show();
    }

    private void updateFirebaseDatabase()
    {
        for( int counter=0;counter<foodItemsArrayList.size();counter++)
        {
            DocumentReference documentReference= fStore.collection("menu").document(String.valueOf(counter));
            Map<String, Object> foodItem= new HashMap<>();
            showLog(String.valueOf(documentReference));

            String foodNameValue=foodItemsArrayList.get(counter).getFoodName();
            String priceValue=foodItemsArrayList.get(counter).getPrice();
            String calories=foodItemsArrayList.get(counter).getCalories();
            String ingredients=foodItemsArrayList.get(counter).getIngredients();
            String vegOrNonVeg=foodItemsArrayList.get(counter).getVegOrNonVeg();
            String bestsellerOrNot=foodItemsArrayList.get(counter).getBestsellerOrNot();
            String foodImgUrl = foodItemsArrayList.get(counter).getFoodImgUrl();



            //FoodItems foodItemsobj=foodItemsArrayList.get(counter);
            //showLog(foodItemsobj.toString());
            //showLog(foodItemsArrayList.toString());
            //sleep(3000);

            //String foodNameValue="Maggie";
            //String priceValue="40";
            showLog(foodNameValue+"   "+priceValue);

            foodItem.put("foodName",foodNameValue);
            foodItem.put("price",priceValue);
            foodItem.put("calories",calories);
            foodItem.put("ingredients",ingredients);
            foodItem.put("vegOrNonVeg",vegOrNonVeg);
            foodItem.put("bestsellerOrNot",bestsellerOrNot);
            foodItem.put("foodImgUrl",foodImgUrl);

            documentReference.set(foodItem).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    showLog("Added new row to fire base database-->"+ foodNameValue +"   @ Rs."+priceValue);

                }
            });

        }

    }

    private  void deleteAndThenUpdateFirebaseDatabase(@NonNull RecyclerView.ViewHolder viewHolder)
    {

        int position=viewHolder.getAdapterPosition();
        //sabko ek ghar agge kardo fir last wala ko delte kardo bas!!!

        for (int i= position ;i<foodItemsArrayList.size()-1;i++)
        {
            DocumentReference documentReference= fStore.collection("menu").document(String.valueOf(i));
            Map<String, Object> foodItem= new HashMap<>();
            showLog(String.valueOf(documentReference));

            String foodNameValue=foodItemsArrayList.get(i+1).getFoodName();
            String priceValue=foodItemsArrayList.get(i+1).getPrice();
            String calories=foodItemsArrayList.get(i+1).getCalories();
            String ingredients=foodItemsArrayList.get(i+1).getIngredients();
            String vegOrNonVeg=foodItemsArrayList.get(i+1).getVegOrNonVeg();
            String bestsellerOrNot=foodItemsArrayList.get(i+1).getBestsellerOrNot();
            String foodImgUrl=foodItemsArrayList.get(i+1).getFoodImgUrl();


            showLog(foodNameValue+"   "+priceValue);

            foodItem.put("foodName",foodNameValue);
            foodItem.put("price",priceValue);
            foodItem.put("calories",calories);
            foodItem.put("ingredients",ingredients);
            foodItem.put("vegOrNonVeg",vegOrNonVeg);
            foodItem.put("bestsellerOrNot",bestsellerOrNot);
            foodItem.put("foodImgUrl",foodImgUrl);

            documentReference.set(foodItem).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    showLog("Added new row to fire base database-->"+ foodNameValue +"   @ Rs."+priceValue);

                }
            });

        }

        int last_position=foodItemsArrayList.size()-1;

        fStore.collection("menu").document(String.valueOf(last_position))
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

            foodItemsArrayList.remove(viewHolder.getAdapterPosition());
            adapter.notifyDataSetChanged();
            saveData();

        }
    };

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        position_text =adapterView.getItemAtPosition(position).toString();
        //Toast.makeText(adapterView.getContext(),position_text, Toast.LENGTH_SHORT).show();
        ((TextView) view).setTextColor(ContextCompat.getColor(SetMenuActivity.this, R.color.black));


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }







    class details
    {

                String foodName="";
                String price="";

                String calories="";
                String ingredients="";

                String vegOrNonVeg="";
                String bestsellerOrNot="";

                String foodImgUrl;

                public details(String foodName, String price, String calories , String ingredients , String vegOrNonVeg , String bestsellerOrNot , String foodImgUrl)
                {
                    this.foodName=foodName;
                    this.price=price;
                    this.calories=calories;
                    this.ingredients=ingredients;
                    this.vegOrNonVeg=vegOrNonVeg;
                    this.bestsellerOrNot=bestsellerOrNot;
                    this.foodImgUrl = foodImgUrl;
                }

    }


    public class MapExample {

        //Creating map of Books
        Map<String,details> map=new HashMap<String,details>();

        public void main() {
            //Creating Books

            //Bitmap maggie = BitmapFactory.decodeResource(getResources(), R.drawable.maggie);
            //Bitmap roll = BitmapFactory.decodeResource(getResources(), R.drawable.roll);

            String maggie="https://t3.ftcdn.net/jpg/03/98/52/26/360_F_398522692_to2qhHM4egRF6F1JH0RwHzqAb78tBe3u.jpg";
            String roll="https://1.bp.blogspot.com/-B2JqpwYv7mI/XvM09r21GSI/AAAAAAAAAA4/J67XoA1t4Fg4uEM4Uu7CnMPPPqcaNCmvwCK4BGAsYHg/s1200/photo.jpg";

            String noodles="https://www.indianhealthyrecipes.com/wp-content/uploads/2021/07/hakka-noodles-recipe.jpg";
            String friedRice="https://media.gettyimages.com/photos/asian-chinese-fried-rice-with-vegetable-and-egg-with-chopsticks-picture-id172789635?s=612x612";

            String paneerMasala="https://imagevars.gulfnews.com/2021/08/30/Paneer-Masala-_17b9680cdfb_medium.jpg";
            String paneerParatha="https://www.viniscookbook.com/wp-content/uploads/2018/09/20180924_224755.jpg";

            String cornChat="https://www.whiskaffair.com/wp-content/uploads/2021/06/Indian-Flavored-Corn-Chaat-2-3.jpg";
            String patties="https://philipsbakery.in/uploads/product/thumb/1561562161product.png";

            String biryani="https://www.licious.in/blog/wp-content/uploads/2020/12/Hyderabadi-chicken-Biryani-600x600.jpg";
            String boiledEgg="https://newspakistanenglish.s3.ap-south-1.amazonaws.com/wp-content/uploads/2017/07/egg-.jpg";
            String paoBhaji="https://thumbs.dreamstime.com/b/pav-bhaji-23718431.jpg";






            details maggieObj= new details("maggie","25","1000","maggie, masala, butter, veggies","veg","bestseller", maggie );
            details rollObj= new details("roll","35","1200","maida, oil, carrot, cheese, onion","veg","non-bestseller", roll);

            details noodlesObj= new details("noodles","45","1200","noodles, onion, masala, butter, veggies","veg","non-bestseller", noodles);
            details friedRiceObj= new details("friedRice","65","1400","rice, oil, carrot, masala, onion","veg","non-bestseller", friedRice);

            details paneerMasalaObj= new details("paneerMasala","85","600","panner, oil, masala, butter, onion","veg","non-bestseller", paneerMasala);
            details paneerParathaObj= new details("paneerParatha","40","1200","maida, oil, panner, cheese, onion","veg","non-bestseller", paneerParatha);

            details cornChatObj= new details("cornChat","40","200","corn, carrot, cheese, onion","veg","non-bestseller", cornChat);
            details pattiesObj= new details("patties","25","800","maida, oil, potato stuffings","veg","non-bestseller", patties);

            details biryaniObj= new details("biryani","65","1600","rice, oil, carrot, chicken, onion","non-veg","bestseller", biryani);
            details boiledEggObj= new details("boiledEgg","10","200","maida, oil, carrot, cheese, onion","non-veg","non-bestseller", boiledEgg);
            details paoBhajiObj= new details("paoBhaji","40","500","pao, oil, carrot, bhaji, onion","veg","non-bestseller", paoBhaji);


            //Adding Books to map
            map.put("maggie",maggieObj);
            map.put("roll",rollObj);

            map.put("noodles",noodlesObj);
            map.put("friedRice",friedRiceObj);

            map.put("paneerMasala",paneerMasalaObj);
            map.put("paneerParatha",paneerParathaObj);

            map.put("cornChat",cornChatObj);
            map.put("patties",pattiesObj);


            map.put("biryani",biryaniObj);
            map.put("boiledEgg",boiledEggObj);
            map.put("paoBhaji",paoBhajiObj);
//            map.put(2,b2);
//            map.put(3,b3);

//            Traversing map
//            for(Map.Entry<String, details> entry:map.entrySet()){
//                String key=entry.getKey();
//                details b=entry.getValue();
//                showLog(key+" Details:");
//                showLog(b.foodName+" "+b.price+" "+b.calories+" "+b.ingredients+" "+b.vegOrNonVeg+" "+b.bestsellerOrNot);
//            }

        }
    }




}