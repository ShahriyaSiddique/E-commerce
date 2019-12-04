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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button signUp;
    private EditText nameET,phoneET,passwordET;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        signUp = findViewById(R.id.sign_up_btn);
        nameET = findViewById(R.id.sign_up_user_name_input);
        phoneET = findViewById(R.id.sign_up_phone_number_input);
        passwordET = findViewById(R.id.sign_up_password_input);
        progressDialog = new ProgressDialog(this);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });


    }

    private void createAccount() {
        String name = nameET.getText().toString().trim();
        String phone = phoneET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();

        if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Please Enter your name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please Enter your phone number", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please Enter your password", Toast.LENGTH_SHORT).show();
        }
        else
        {
            progressDialog.setTitle("Create Account");
            progressDialog.setMessage("Please wait..");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            validate(name, phone, password);


        }

    }

    private void validate(final String name, final String phone, final String password) {

        final DatabaseReference rootRefer;
        rootRefer = FirebaseDatabase.getInstance().getReference();

        rootRefer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("Users").child(phone).exists()))
                {
                    HashMap<String, Object> userHashMap = new HashMap<>();
                    userHashMap.put("name",name);
                    userHashMap.put("phone",phone);
                    userHashMap.put("password",password);

                    rootRefer.child("Users").child(phone).updateChildren(userHashMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(RegisterActivity.this, "Your Account created Successfully", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();

                                        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                    }
                                    else
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Network Error!! Try again later", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });

                }
                else
                {
                    Toast.makeText(RegisterActivity.this, phone + " already exists", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, " Try another number", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this,MainActivity.class));



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
