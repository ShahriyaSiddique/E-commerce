package com.example.e_commercedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commercedemo.Model.AdminOrders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrderActivity extends AppCompatActivity {

    private RecyclerView orderList;
    private DatabaseReference orderRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_order);

        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        orderList = findViewById(R.id.order_list_rl);
        orderList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options = new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(orderRef,AdminOrders.class)
                .build();

        FirebaseRecyclerAdapter<AdminOrders,AdminOrderViewHolder> adapter = new FirebaseRecyclerAdapter<AdminOrders, AdminOrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminOrderViewHolder holder, final int position, @NonNull final AdminOrders model)
            {
                holder.userName.setText("Name = "+model.getName());
                holder.userTotalPrice.setText("Total Price = "+model.getTotalPrice()+"$");
                holder.userPhone.setText("Phone = "+ model.getPhone());
                holder.userShippingAddress.setText("Shipping Address = "+model.getAddress()+","+model.getCity());
                holder.userDateTime.setText("Ordered at = "+model.getDate()+" "+model.getTime());

                holder.showOrderBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String uid = getRef(position).getKey();

                        Intent intent = new Intent(AdminNewOrderActivity.this,AdminUsersProductsActivity.class);
                        intent.putExtra("uid",uid);
                        startActivity(intent);

                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {

                        CharSequence options[] = new CharSequence[]{
                                    "Yes",
                                    "No"
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrderActivity.this);

                        builder.setTitle("Shipped the Product?");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                if (i==0)
                                {
                                    String uid = getRef(position).getKey();
                                    removeOrder(uid);

                                }
                                if (i==1)
                                {
                                    Toast.makeText(AdminNewOrderActivity.this, "Please make the shipping in time", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                        builder.show();

                    }
                });

            }

            @NonNull
            @Override
            public AdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout,parent,false);
               return new AdminOrderViewHolder(view);
            }
        };
        orderList.setAdapter(adapter);
        adapter.startListening();
    }

    private void removeOrder(String uid)
    {
        orderRef.child(uid).removeValue();


    }

    public static class AdminOrderViewHolder extends RecyclerView.ViewHolder {

        public TextView userName,userPhone,userTotalPrice,userDateTime,userShippingAddress;
        public Button showOrderBtn;

    public AdminOrderViewHolder(@NonNull View itemView)
    {
        super(itemView);

        userName = itemView.findViewById(R.id.user_name);
        userPhone = itemView.findViewById(R.id.order_phone_name);
        userShippingAddress = itemView.findViewById(R.id.order_address);
        userDateTime = itemView.findViewById(R.id.order_date_time);
        userTotalPrice = itemView.findViewById(R.id.order_total_price);
        showOrderBtn = itemView.findViewById(R.id.show_all_product_btn);


    }
}
}
