package com.example.e_commercedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView tShirts,sportsTShirts,shirts,sweater;
    private ImageView bags,hats,shoes,glasses;
    private ImageView mobiles,laptops,headphones,watches;
    private Button logoutBtn,checkOrderBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_catagory);

        logoutBtn = findViewById(R.id.admin_logout_btn);
        checkOrderBtn= findViewById(R.id.check_order_btn);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        checkOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminCategoryActivity.this,AdminNewOrderActivity.class);
                startActivity(intent);

            }
        });
        tShirts = findViewById(R.id.t_shirts);
        shirts = findViewById(R.id.shirts);
        sportsTShirts = findViewById(R.id.sports_t_shirts);
        sweater = findViewById(R.id.sweater);

        bags = findViewById(R.id.purses_bags);
        hats = findViewById(R.id.hats);
        shoes = findViewById(R.id.shoes);
        glasses = findViewById(R.id.glasses);

        mobiles = findViewById(R.id.mobiles);
        laptops = findViewById(R.id.laptops);
        headphones = findViewById(R.id.headphones);
        watches = findViewById(R.id.watches);

        tShirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddProductActivity.class);
                intent.putExtra("category","tShirts");
                startActivity(intent);
            }
        });

        shirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddProductActivity.class);
                intent.putExtra("category","shirts");
                startActivity(intent);
            }
        });

        sportsTShirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddProductActivity.class);
                intent.putExtra("category","sportsTShirts");
                startActivity(intent);
            }
        });

        sweater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddProductActivity.class);
                intent.putExtra("category","sweater");
                startActivity(intent);
            }
        });

        bags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddProductActivity.class);
                intent.putExtra("category","bags");
                startActivity(intent);
            }
        });

        hats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddProductActivity.class);
                intent.putExtra("category","hats");
                startActivity(intent);
            }
        });

        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddProductActivity.class);
                intent.putExtra("category","shoes");
                startActivity(intent);
            }
        });

        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddProductActivity.class);
                intent.putExtra("category","glasses");
                startActivity(intent);
            }
        });

        mobiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddProductActivity.class);
                intent.putExtra("category","mobiles");
                startActivity(intent);
            }
        });

        laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddProductActivity.class);
                intent.putExtra("category","laptops");
                startActivity(intent);
            }
        });

        watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddProductActivity.class);
                intent.putExtra("category","watches");
                startActivity(intent);
            }
        });

        headphones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddProductActivity.class);
                intent.putExtra("category","headphones");
                startActivity(intent);
            }
        });

    }
}
