package com.example.e_commercedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.e_commercedemo.Model.Products;
import com.example.e_commercedemo.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    private FloatingActionButton addToCartFBtn;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productDescription,productName,productPrice;
    private String pId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        pId = getIntent().getStringExtra("pId");


        addToCartFBtn = findViewById(R.id.add_to_cart_btn);
        productImage = findViewById(R.id.product_image_details);
        numberButton = findViewById(R.id.elegant_button);
        productDescription = findViewById(R.id.product_description_details);
        productName = findViewById(R.id.product_name_details);
        productPrice = findViewById(R.id.product_price_details);

        getProductDetails(pId);

        addToCartFBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart();
            }
        });

    }

    private void addToCart() {

        String saveCurrentDate,saveCurrentTime;

        Calendar callForCal = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(callForCal.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callForCal.getTime());

        final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String,Object> cartMap = new HashMap<>();
        cartMap.put("pId",pId);
        cartMap.put("pName",productName.getText().toString());
        cartMap.put("description",productDescription.getText().toString());
        cartMap.put("price",productPrice.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("quantity", numberButton.getNumber());

        cartRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                .child("Products").child(pId).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    cartRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone())
                            .child("Products").child(pId).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                            {
                                Toast.makeText(ProductDetailsActivity.this, "Product added on your cart", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ProductDetailsActivity.this,HomeActivity.class));
                            }
                            else 
                            {
                                Toast.makeText(ProductDetailsActivity.this, "Unable to add on Your cart", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }
            }
        });






    }

    private void getProductDetails(String pId) {
        final DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productRef.child(pId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    Products products = dataSnapshot.getValue(Products.class);
                    productName.setText(products.getpName());
                    productPrice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());
                    Picasso.get().load(products.getImageUrl()).into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
