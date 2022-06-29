package com.titans.foodchefuser.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.titans.foodchefuser.FoodDetail;
import com.titans.foodchefuser.R;
import com.titans.foodchefuser.models.FoodItems;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adapter extends RecyclerView.Adapter<Adapter.holder> {

    List<FoodItems> data;

    private Context context;

    public Adapter(List<FoodItems> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.menu_display_row, parent, false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
//        Log.d("ERROR ADAPTER",data.get((position)).toString());
        FoodItems foodItem = (FoodItems) data.get(position);
        holder.titleTV.setText(foodItem.getFoodName());
        holder.priceTV.setText("₹"+foodItem.getPrice());
        if(foodItem.getBestsellerOrNot().equals("bestseller")){
            holder.isBestsellerTV.setText("BestSeller");
        }

        if(foodItem.getVegOrNonVeg().equals("non-veg")){
            Glide.with(context).load(R.mipmap.ic_non_veg).into(holder.isVeg);
        }

//        holder.detailTV.setText(foodItem.get);
        holder.caloriesTV.setText("Calories: "+foodItem.getCalories());
        holder.ingredientsTV.setText(foodItem.getIngredients());
        String foodImgUrl = foodItem.getFoodImgUrl();
        Glide.with(context).load(foodImgUrl).into(holder.img);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    class holder extends RecyclerView.ViewHolder implements View.OnClickListener {


        ImageView img, isVeg;
        FirebaseFirestore db;
        TextView titleTV, priceTV, caloriesTV, ingredientsTV, quantityTV, isBestsellerTV;
        Button addBtn, minusBtn, plusBtn;

        DocumentReference documentReference;

        private AdapterView.OnItemClickListener itemClickListener;

        public holder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.itemImage);
            isVeg = itemView.findViewById(R.id.vegOrNot);
            titleTV = itemView.findViewById(R.id.itemTitle);
            priceTV = itemView.findViewById(R.id.itemPrice);
            caloriesTV = itemView.findViewById(R.id.itemCalories);
            ingredientsTV = itemView.findViewById(R.id.itemIngredients);
            quantityTV = itemView.findViewById(R.id.quantity_tv);
            isBestsellerTV = itemView.findViewById(R.id.isBestSeller);
            minusBtn = itemView.findViewById(R.id.minusButton);
            plusBtn = itemView.findViewById(R.id.plusButton);
            addBtn = itemView.findViewById(R.id.addButton);
            db = FirebaseFirestore.getInstance();
            documentReference = db.collection("user").document(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).collection("cart").document("List");

            itemView.setOnClickListener(this);
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, FoodDetail.class);
                    intent.putExtra("name", data.get(getPosition()).getFoodName());
                    intent.putExtra("price", data.get(getPosition()).getPrice());
                   // intent.putExtra("description",data.get(getPosition()).getDescription());
                    context.startActivity(intent);

                }
            });

            minusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int q = Integer.parseInt(quantityTV.getText().toString());
                    if(q>0){
                        q--;
                        quantityTV.setText(Integer.toString(q));

                        int finalQ = q;
                        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Map<String, Object> cart = document.getData();
                                        cart.put(titleTV.getText().toString(), finalQ+","+priceTV.getText().toString().substring(1));
                                        documentReference.set(cart);

                                    } else {

                                        Map<String, String> cart = new HashMap<>();
                                        cart.put(titleTV.getText().toString(), finalQ+","+priceTV.getText().toString().substring(1));
                                        documentReference.set(cart);
                                    }
                                } else {

                                }
                            }
                        });


                    }else
                        return;
                }
            });

            plusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int q = Integer.parseInt(quantityTV.getText().toString());
                    if(q<10){
                        q++;
                        quantityTV.setText(Integer.toString(q));

                        int finalQ = q;
                        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Map<String, Object> cart = document.getData();
                                        cart.put(titleTV.getText().toString(), finalQ+","+priceTV.getText().toString().substring(1));
                                        documentReference.set(cart);

                                    } else {

                                        Map<String, String> cart = new HashMap<>();
                                        cart.put(titleTV.getText().toString(), finalQ+","+priceTV.getText().toString().substring(1));
                                        documentReference.set(cart);
                                    }
                                } else {

                                }
                            }
                        });

                    }else
                        return;

                }
            });

        }

        @Override
        public void onClick(View view) {
//            itemClickListener.onItemClick(view,getAbsoluteAdapterPosition());
//            if (view.getId() == titleTV.getId())
//            Toast.makeText(view.getContext(), data.get(getPosition()).getFoodName() + " " + data.get(getPosition()).getPrice(), Toast.LENGTH_SHORT).show();
//
//            Intent intent = new Intent(context, FoodDetail.class);
//            intent.putExtra("name", data.get(getPosition()).getFoodName());
//            intent.putExtra("price", data.get(getPosition()).getPrice());
//            intent.putExtra("description",data.get(getPosition()).getDescription());
//            context.startActivity(intent);

        }

        public AdapterView.OnItemClickListener getItemClickListener() {
            return itemClickListener;
        }

        public void setItemClickListener(AdapterView.OnItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }
    }

}
