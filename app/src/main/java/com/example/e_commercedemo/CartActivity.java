package com.example.e_commercedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commercedemo.Model.Cart;
import com.example.e_commercedemo.Prevalent.Prevalent;
import com.example.e_commercedemo.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CartActivity extends AppCompatActivity {

    private RecyclerView cartListRL;
    private RecyclerView.LayoutManager layoutManager;
    private Button nextBtn;
    private TextView totalPriceTV;
    private int totalPrice=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartListRL = findViewById(R.id.cart_list_rl);
        cartListRL.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        cartListRL.setLayoutManager(layoutManager);

        nextBtn = findViewById(R.id.next_btn);
        totalPriceTV = findViewById(R.id.total_price);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               totalPriceTV.setText("Total Price = "+String.valueOf(totalPrice)+"$");

                Intent intent = new Intent(CartActivity.this,ConfirmOrderActivity.class);
                intent.putExtra("Total Price",String.valueOf(totalPrice));
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        //totalPriceTV.setText("Total Price = "+String.valueOf(totalPrice)+"$");

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>().setQuery(cartListRef.child("User View")
                .child(Prevalent.currentOnlineUser.getPhone()).child("Products"), Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model)
            {
                holder.cartProductName.setText(model.getpName());
                holder.cartProductPrice.setText("Price = "+model.getPrice()+"$");
                holder.cartProductQuantity.setText("Quantity = " + model.getQuantity());

                int sameTypeProductPrice = (Integer.valueOf(model.getPrice())) * (Integer.valueOf(model.getQuantity()));
                totalPrice+=sameTypeProductPrice;

                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        final CharSequence options[] = new CharSequence[]
                                {"Edit","Delete"};

                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Option");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                switch (i)
                                {
                                    case 0:
                                        Intent intent = new Intent(CartActivity.this,ProductDetailsActivity.class);
                                        intent.putExtra("pId",model.getpId());
                                        startActivity(intent);
                                        break;
                                    case 1:

                                        cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                                                .child("Products").child(model.getpId()).removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        Toast.makeText(CartActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();

                                                    }
                                                });

                                        break;
                                }

                            }
                        });

                        builder.show();

                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout,parent,false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;

            }
        };

        cartListRL.setAdapter(adapter);
        adapter.startListening();
    }
}
