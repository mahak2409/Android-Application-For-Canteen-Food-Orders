package com.example.vendor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vendor.models.offerItems;
import com.example.vendor.models.pendingOrdersItems;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class RVadapter_pendingOrders  extends RecyclerView.Adapter<RVadapter_pendingOrders.ViewHolder>   {

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
    public RVadapter_pendingOrders.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_pending_orders, parent, false);
        return new RVadapter_pendingOrders.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVadapter_pendingOrders.ViewHolder holder, int position) {

        pendingOrdersItems pendingOrdersItemsObject = pendingOrdersItemsArrayList.get(position);
        holder.nameTextView.setText(pendingOrdersItemsObject.getName());
        holder.phoneTextView.setText("Phone - "+ pendingOrdersItemsObject.getPhone());
        holder.orderListTextView.setText(pendingOrdersItemsObject.getOrderList());
        holder.totalBillAmountTextView.setText("Total Bill Amount - ₹ "+ pendingOrdersItemsObject.getTotalBillAmount());


        phone = pendingOrdersItemsObject.getPhone();
        holder.pendingOrdersItemsObject =pendingOrdersItemsObject ;


        //setting button clicks inside items of a specific positio inside recyler view....
        holder.callImageButton.setOnClickListener(new View.OnClickListener() {
            //https://www.youtube.com/watch?v=FA5cGLLiSWs&ab_channel=MohamedShehab
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "calling... " + pendingOrdersItemsObject.getPhone(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" +pendingOrdersItemsObject.getPhone() ));
                context.startActivity(intent);
            }
        });


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
        private TextView nameTextView, phoneTextView, orderListTextView , totalBillAmountTextView;
        //private CardView cardViewPendingOrders;
        //private LinearLayout pendingOrdersLinearLayout;
        ImageButton callImageButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // initializing our views with their ids.
            nameTextView = itemView.findViewById(R.id.nameTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            orderListTextView = itemView.findViewById(R.id.orderListTextView);
            totalBillAmountTextView = itemView.findViewById(R.id.totalBillAmountTextView);

            //cardViewPendingOrders = itemView.findViewById(R.id.cardViewPendingOrders);
            //pendingOrdersLinearLayout = itemView.findViewById(R.id.pendingOrdersLinearLayout);

            callImageButton = itemView.findViewById(R.id.callImageButton);





        }



    }


    private void showLog(String msg)
    {
        Log.i(TAG,msg);
    }
}
