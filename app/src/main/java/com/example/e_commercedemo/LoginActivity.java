package com.example.e_commercedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commercedemo.Model.Users;
import com.example.e_commercedemo.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText phoneET,passwordET;
    private Button loginBtn;
    private CheckBox rememberMeCB;
    private ProgressDialog progressDialog;
    private TextView adminPanelLink,notadminPanelLink;
    private String parentDbName = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneET = findViewById(R.id.login_phone_number_input);
        passwordET = findViewById(R.id.login_password_input);
        loginBtn = findViewById(R.id.login_btn);
        rememberMeCB = findViewById(R.id.remember_me_check_box);
        adminPanelLink = findViewById(R.id.admin_panel_link);
        notadminPanelLink= findViewById(R.id.not_admin_panel_link);
        progressDialog = new ProgressDialog(this);

        Paper.init(this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        adminPanelLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginBtn.setText("Admin Login");
                adminPanelLink.setVisibility(View.INVISIBLE);
                notadminPanelLink.setVisibility(View.VISIBLE);
                rememberMeCB.setVisibility(View.INVISIBLE);
                parentDbName = "Admins";
            }
        });

        notadminPanelLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginBtn.setText("Login");
                adminPanelLink.setVisibility(View.VISIBLE);
                notadminPanelLink.setVisibility(View.INVISIBLE);
                parentDbName = "Users";

            }
        });

    }

    private void loginUser() {

        String phone = phoneET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();

        if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please Enter your phone number", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please Enter your password", Toast.LENGTH_SHORT).show();
        }
        else
        {
            progressDialog.setTitle("Login to Account");
            progressDialog.setMessage("Please wait..We are checking you phone and password");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            allowAccessToAccount(phone, password);


        }

    }

    private void allowAccessToAccount(final String phone, final String password) {

        if(rememberMeCB.isChecked())
        {
            Paper.book().write(Prevalent.userPhoneKey,phone);
            Paper.book().write(Prevalent.userPasswordKey,password);

        }

        final DatabaseReference rootref;
        rootref = FirebaseDatabase.getInstance().getReference();


        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                if(dataSnapshot.child(parentDbName).child(phone).exists())
                {
                    Users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);

                    if(usersData.getPhone().equals(phone))
                    {
                        if (usersData.getPassword().equals(password))
                        {
                            if (parentDbName.equals("Admins"))
                            {
                                Toast.makeText(LoginActivity.this, "Welcome Admin..", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                                startActivity(new Intent(LoginActivity.this,AdminCategoryActivity.class));
                            }
                            else if (parentDbName.equals("Users"))
                            {
                                Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                                Prevalent.currentOnlineUser = usersData;

                                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                            }


                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "Please enter correct password", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }

                }
                else
                {
                    Toast.makeText(LoginActivity.this, "This " + phone +" number don't Register", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
