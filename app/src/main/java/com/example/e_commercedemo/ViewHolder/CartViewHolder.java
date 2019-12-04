package com.example.e_commercedemo.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commercedemo.Interface.ItemClickListener;
import com.example.e_commercedemo.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView cartProductName,cartProductPrice,cartProductQuantity;
    public ItemClickListener listener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        cartProductName = itemView.findViewById(R.id.cart_product_name);
        cartProductPrice = itemView.findViewById(R.id.cart_product_price);
        cartProductQuantity = itemView.findViewById(R.id.cart_product_quantity);
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view,getAdapterPosition(),false);

    }
    public void setItemClickListener(ItemClickListener listener)
    {
        this.listener = listener;

    }


}
