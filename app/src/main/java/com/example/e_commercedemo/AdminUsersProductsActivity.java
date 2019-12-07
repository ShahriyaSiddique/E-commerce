package com.example.e_commercedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.e_commercedemo.Model.Cart;
import com.example.e_commercedemo.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminUsersProductsActivity extends AppCompatActivity {

    private RecyclerView productsListRl ;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartListRef;

    private String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_users_products);

        userId = getIntent().getStringExtra("uid");

        productsListRl = findViewById(R.id.product_list_rl);
        productsListRl.setHasFixedSize(true);

        layoutManager =new LinearLayoutManager(this);
        productsListRl.setLayoutManager(layoutManager);

        cartListRef = FirebaseDatabase.getInstance().getReference()
                .child("Cart List")
        .child("Admin View")
                .child(userId)
        .child("Products");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef,Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {

                holder.cartProductName.setText(model.getpName());
                holder.cartProductPrice.setText("Price = "+model.getPrice()+"$");
                holder.cartProductQuantity.setText("Quantity = " + model.getQuantity());
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout,parent,false);
                return new CartViewHolder(view);

            }
        };

        productsListRl.setAdapter(adapter);
        adapter.startListening();


    }
}
