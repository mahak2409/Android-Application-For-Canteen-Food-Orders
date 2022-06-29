package com.titans.foodchefuser;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.titans.foodchefuser.models.pendingOrdersItems;

import java.util.ArrayList;

public class RVadapter_pendingOrders extends RecyclerView.Adapter<RVadapter_pendingOrders.ViewHolder>   {

    private ArrayList<pendingOrdersItems> pendingOrdersItemsArrayList;
    private Context context;
    String TAG= "RVadapter_pendingOrders";

    String phone="";

    public RVadapter_pendingOrders(ArrayList<pendingOrdersItems> pendingOrdersItemsArrayList, Context context) {
        this.pendingOrdersItemsArrayList = pendingOrdersItemsArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_pending_orders, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        pendingOrdersItems pendingOrdersItemsObject = pendingOrdersItemsArrayList.get(position);
        holder.nameTextView.setText(pendingOrdersItemsObject.getName());
        holder.orderListTextView.setText(pendingOrdersItemsObject.getOrderList());
        holder.totalBillAmountTextView.setText("Total Bill Amount - ₹ "+ pendingOrdersItemsObject.getTotalBillAmount());


        phone = pendingOrdersItemsObject.getPhone();
        holder.pendingOrdersItemsObject =pendingOrdersItemsObject ;


    }

    @Override
    public int getItemCount() {
        showLog("pendingOrdersItemsArrayList.size()-->"+pendingOrdersItemsArrayList.size());
        return pendingOrdersItemsArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {

        pendingOrdersItems pendingOrdersItemsObject;

        // creating variables for our views.
        private TextView nameTextView, orderListTextView , totalBillAmountTextView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // initializing our views with their ids.
            nameTextView = itemView.findViewById(R.id.nameTextView);
            orderListTextView = itemView.findViewById(R.id.orderListTextView);
            totalBillAmountTextView = itemView.findViewById(R.id.totalBillAmountTextView);

            //cardViewPendingOrders = itemView.findViewById(R.id.cardViewPendingOrders);
            //pendingOrdersLinearLayout = itemView.findViewById(R.id.pendingOrdersLinearLayout);






        }



    }


    private void showLog(String msg)
    {
        Log.i(TAG,msg);
    }
}
