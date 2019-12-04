package com.example.e_commercedemo.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commercedemo.Interface.ItemClickListener;
import com.example.e_commercedemo.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView productName,productDesc,productPrice;
    public ImageView productImage;
    public ItemClickListener listener;



    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);


        productName = itemView.findViewById(R.id.product_name);
        productDesc = itemView.findViewById(R.id.product_description);
        productImage = itemView.findViewById(R.id.product_image);
        productPrice = itemView.findViewById(R.id.product_price);
    }

    public void setItemClickListener(ItemClickListener listener)
    {
        this.listener = listener;

    }

    @Override
    public void onClick(View view) {
        listener.onClick(view,getAdapterPosition(),false);

    }
}
