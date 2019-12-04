package com.example.e_commercedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.e_commercedemo.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmOrderActivity extends AppCompatActivity {
    private EditText nameET,phoneET,addressET,cityET;
    private Button confirm;
    private String totalPrice= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        nameET = findViewById(R.id.enter_name);
        phoneET = findViewById(R.id.enter_phone);
        addressET = findViewById(R.id.enter_address);
        cityET = findViewById(R.id.enter_city);
        confirm = findViewById(R.id.confirm_order_btn);

        totalPrice = getIntent().getStringExtra("Total Price");

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

    }

    private void validate() {

        if(TextUtils.isEmpty(nameET.getText().toString()))
        {
            Toast.makeText(this, "Please enter customer name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phoneET.getText().toString()))
        {
            Toast.makeText(this, "Please enter Phone number", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(addressET.getText().toString()))
        {
            Toast.makeText(this, "Please enter Address ", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(cityET.getText().toString()))
        {
            Toast.makeText(this, "Please enter City name", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Thaank you", Toast.LENGTH_SHORT).show();
            confirmOrder();
        }

    }

    private void confirmOrder()
    {
        String saveCurrentDate,saveCurrentTime;

        Calendar callForCal = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(callForCal.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callForCal.getTime());

        final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders").child(Prevalent.currentOnlineUser.getPhone());

        HashMap<String,Object> ordersMap = new HashMap<>();

        ordersMap.put("totalPrice",totalPrice);
        ordersMap.put("name",nameET.getText().toString());
        ordersMap.put("phone",phoneET.getText().toString());
        ordersMap.put("address",addressET.getText().toString());
        ordersMap.put("city",cityET.getText().toString());
        ordersMap.put("date",saveCurrentDate);
        ordersMap.put("time",saveCurrentTime);
        ordersMap.put("state","Not shipped");

        orderRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    FirebaseDatabase.getInstance().getReference()
                            .child("Cart List")
                            .child("User View")
                            .child(Prevalent.currentOnlineUser.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(ConfirmOrderActivity.this, "Your Orders confirmed", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(ConfirmOrderActivity.this,HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }

                                }
                            });

                }
            }
        });


    }
}
