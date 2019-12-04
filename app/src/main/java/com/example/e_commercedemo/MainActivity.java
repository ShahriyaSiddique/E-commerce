package com.example.e_commercedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.e_commercedemo.Model.Users;
import com.example.e_commercedemo.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button loginBtn,signUpBtn;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn = findViewById(R.id.main_login_btn);
        signUpBtn = findViewById(R.id.main_sign_up_btn);
        progressDialog = new ProgressDialog(this);

        Paper.init(this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        String userPhoneKey = Paper.book().read(Prevalent.userPhoneKey);
        String userPasswordKey = Paper.book().read(Prevalent.userPasswordKey);
        if ( !TextUtils.isEmpty(userPhoneKey) &&  !TextUtils.isEmpty(userPasswordKey))
        {
            allowUserAccess(userPhoneKey,userPasswordKey);

            progressDialog.setTitle("Login to Account");
            progressDialog.setMessage("Please wait..");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();


        }

    }

    private void allowUserAccess(final String userPhoneKey, final String userPasswordKey) {

        final DatabaseReference rootref;
        rootref = FirebaseDatabase.getInstance().getReference();


        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                if(dataSnapshot.child("Users").child(userPhoneKey).exists())
                {
                    Users usersData = dataSnapshot.child("Users").child(userPhoneKey).getValue(Users.class);

                    if(usersData.getPhone().equals(userPhoneKey))
                    {
                        if (usersData.getPassword().equals(userPasswordKey))
                        {
                            Toast.makeText(MainActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                            Prevalent.currentOnlineUser = usersData;

                            startActivity(new Intent(MainActivity.this,HomeActivity.class));
                        }
                    }

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    }

