package com.titans.foodchefuser.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.titans.foodchefuser.FoodDetail;
import com.titans.foodchefuser.R;
import com.titans.foodchefuser.models.CartItems;
import com.titans.foodchefuser.models.FoodItems;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.holder> {

    List<CartItems> data;

    private Context context;

    public CartAdapter(List<CartItems> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public CartAdapter.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cart_display_row, parent, false);
        return new CartAdapter.holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {

//        holder.titleTV.setText(data.get(position).getFoodName());
//        holder.priceTV.setText("₹" + data.get(position).getPrice());
       CartItems cartItem = (CartItems) data.get(position);
        holder.titleTV.setText(cartItem.getName());
        holder.priceTV.setText("₹" + cartItem.getPrice());
        holder.qty.setText(cartItem.getQty());
        //String foodImgUrl = foodItem.getFoodImgUrl();
      //  Glide.with(context).load(foodImgUrl).into(holder.img);


    }


    @Override
    public int getItemCount() {
        return data.size();
//    return 0;
    }

    class holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView titleTV, priceTV,qty;

        private AdapterView.OnItemClickListener itemClickListener;

        public holder(@NonNull View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.itemTitleCart);
            priceTV = itemView.findViewById(R.id.itemPriceCart);
            qty = itemView.findViewById(R.id.quantity_tvCart);

            itemView.setOnClickListener(this);

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

