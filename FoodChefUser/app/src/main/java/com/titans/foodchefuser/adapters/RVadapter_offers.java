package com.titans.foodchefuser.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.titans.foodchefuser.R;
import com.titans.foodchefuser.models.offerItems;

import java.util.ArrayList;

public class RVadapter_offers extends RecyclerView.Adapter<RVadapter_offers.ViewHolder> {

    private ArrayList<offerItems> offerItemsArrayList;
    private Context context;
    String TAG= "RVadapter_offers";

    public RVadapter_offers(ArrayList<offerItems> offerItemsArrayList, Context context){
        this.offerItemsArrayList=offerItemsArrayList;
        this.context=context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_coupon, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {



        offerItems offerItemsObject = offerItemsArrayList.get(position);
        holder.couponCodeTextView.setText("Code- "+ offerItemsObject.getCouponCode());
        holder.discountTextView.setText(offerItemsObject.getDiscountPercentage()+"%");

    }

    @Override
    public int getItemCount() {

        showLog("offerItemsArrayList.size()-->"+offerItemsArrayList.size());
        return offerItemsArrayList.size();
    }







    public class ViewHolder extends RecyclerView.ViewHolder
    {

        // creating variables for our views.
        private TextView couponNameTextView, couponCodeTextView, discountTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // initializing our views with their ids.
//            couponNameTextView = itemView.findViewById(R.id.couponNameTextView);
            couponCodeTextView = itemView.findViewById(R.id.couponCodeTextView);
            discountTextView = itemView.findViewById(R.id.discountTextView);
        }

    }


    public void showLog(String msg)
    {
        Log.i(TAG,msg);
    }
}
