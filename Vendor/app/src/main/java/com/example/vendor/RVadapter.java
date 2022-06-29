package com.example.vendor;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vendor.models.FoodItems;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class RVadapter extends RecyclerView.Adapter<RVadapter.ViewHolder>
{

    private ArrayList<FoodItems> foodItemsArrayList;
    private Context context;
    String TAG= "RVadapter";

    public RVadapter(ArrayList<FoodItems> foodItemsArrayList,Context context){
        this.foodItemsArrayList=foodItemsArrayList;
        this.context=context;
    }


    @NonNull
    @Override
    public RVadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVadapter.ViewHolder holder, int position) {
        // setting data to our views of recycler view.

        FoodItems foodItemsObject = foodItemsArrayList.get(position);
        holder.foodnameTextView.setText(foodItemsObject.getFoodName());
        holder.priceTextView.setText(foodItemsObject.getPrice());

        String foodImgUrl = foodItemsObject.getFoodImgUrl();
        Glide.with(context).load(foodImgUrl).into(holder.foodImg); //Setting food image

    }

    @Override
    public int getItemCount() {

        showLog("foodItemsArrayList.size()-->"+foodItemsArrayList.size());
//        return 10;
        return foodItemsArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {

        // creating variables for our views.
        private TextView foodnameTextView, priceTextView;
        private ImageView foodImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // initializing our views with their ids.
            foodnameTextView = itemView.findViewById(R.id.foodnameTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            foodImg = itemView.findViewById(R.id.foodImg);
        }
    }


    public void showLog(String msg)
    {
        Log.i(TAG,msg);
    }

}