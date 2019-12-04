package com.example.e_commercedemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commercedemo.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {

    private CircleImageView profileIV;
    private EditText nameET,phoneET,addressET;
    private TextView closeTVBtn,updateTVBTn,profileChangeTVBtn;

    private Uri imageUri;
    private String myUrl = "";
    private StorageReference storageProfileImageRef;
    private String checker = "";

    private StorageTask uploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        storageProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Pictures");

        profileIV = findViewById(R.id.profile_pic_setting);
        nameET= findViewById(R.id.name_setting);
        phoneET = findViewById(R.id.phone_setting);
        addressET = findViewById(R.id.address_setting);
        closeTVBtn = findViewById(R.id.close_setting_btn);
        updateTVBTn = findViewById(R.id.update_setting_btn);
        profileChangeTVBtn = findViewById(R.id.change_image_setting_btn);



        userInfoDisplay(profileIV,nameET,phoneET,addressET);

        closeTVBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        updateTVBTn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checker.equals("clicked"))
                {
                    userInfoSave();
                }
                else
                {
                    updateOnlyInfo();

                }

            }
        });

        profileChangeTVBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker = "clicked";

                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(SettingActivity.this);


            }
        });

    }

    private void updateOnlyInfo()
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Profile Updating");
        progressDialog.setMessage("Please waiting your profile is uploading");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String,Object> userMap = new HashMap<>();
        userMap.put("name",nameET.getText().toString()) ;
        userMap.put("phoneOrder",phoneET.getText().toString()) ;
        userMap.put("address",addressET.getText().toString()) ;

        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

        startActivity(new Intent(SettingActivity.this,HomeActivity.class));
        Toast.makeText(SettingActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void userInfoSave() {

        if(TextUtils.isEmpty(nameET.getText().toString()))
        {
            Toast.makeText(this, "Please Enter Name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phoneET.getText().toString()))
        {
            Toast.makeText(this, "Please Enter Phone number", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(addressET.getText().toString()))
        {
            Toast.makeText(this, "Please Enter address", Toast.LENGTH_SHORT).show();
        }
        else if(checker.equals("clicked"))
        {
            uploadImage();

        }
    }

    private void uploadImage()
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Profile Updating");
        progressDialog.setMessage("Please waiting your profile is uploading");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if(imageUri!=null)
        {
            final StorageReference fileRef = storageProfileImageRef
                    .child(Prevalent.currentOnlineUser.getPhone()+".jpg");

            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful())
                    {
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                        HashMap<String,Object> userMap = new HashMap<>();
                        userMap.put("name",nameET.getText().toString()) ;
                        userMap.put("phoneOrder",phoneET.getText().toString()) ;
                        userMap.put("address",addressET.getText().toString()) ;
                        userMap.put("image",myUrl);

                        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

                        progressDialog.dismiss();

                        startActivity(new Intent(SettingActivity.this,HomeActivity.class));
                        Toast.makeText(SettingActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });


        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data!=null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            profileIV.setImageURI(imageUri);

        }
        else
        {
            Toast.makeText(this, "Error : Try Again", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(SettingActivity.this,SettingActivity.class));
            finish();
        }
    }

 /*   private void userInfoDisplay(final CircleImageView profileIV, final EditText nameET, final EditText phoneET, final EditText addressET)
    {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    if (dataSnapshot.child("image").exists())
                    {
                        String name = dataSnapshot.child("name").getValue().toString();
                        String phone = dataSnapshot.child("phone").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();
                        String image = dataSnapshot.child("image").getValue().toString();

                        Picasso.get().load(image).into(profileIV);
                        nameET.setText(name);
                        phoneET.setText(phone);
                        addressET.setText(address);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }*/

    private void userInfoDisplay(final CircleImageView profileImageView, final EditText fullNameEditText, final EditText userPhoneEditText, final EditText addressEditText)
    {
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    if (dataSnapshot.child("image").exists())
                    {
                        String image = dataSnapshot.child("image").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();
                        String phone = dataSnapshot.child("phone").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();

                        Picasso.get().load(image).into(profileImageView);
                        fullNameEditText.setText(name);
                        userPhoneEditText.setText(phone);
                        addressEditText.setText(address);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
}}
