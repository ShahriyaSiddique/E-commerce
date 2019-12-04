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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddProductActivity extends AppCompatActivity {

    /*private TextView categoryName;
    private String name;*/

    private Button addProductBtn;
    private EditText addProductNameET, addProductDescriptionET, addProductPriceET;
    private ImageView addProductIV;
    private ProgressDialog loading;
    private static final int imagePick = 1;
    private Uri imageUri;
    private String categoryName,productName, productDescription, productPrice, productRandomKey,downloadedImageUrl;
    private String saveCurrentDate, saveCurrentTime;
    private StorageReference productImageRef;
    private DatabaseReference productDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_product);

        /*categoryName = findViewById(R.id.category_name);

        name = getIntent().getExtras().get("category").toString();

        categoryName.setText(name);*/

        addProductBtn = findViewById(R.id.add_product_button);
        addProductNameET = findViewById(R.id.add_product_name);
        addProductDescriptionET = findViewById(R.id.add_product_description);
        addProductPriceET = findViewById(R.id.add_product_price);
        addProductIV = findViewById(R.id.add_image_for_product);
        loading = new  ProgressDialog(this);

        productImageRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        productDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Products");


        categoryName = getIntent().getExtras().get("category").toString();

        addProductIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validateProductData();
            }
        });

    }

    private void validateProductData() {
        productName = addProductNameET.getText().toString().trim();
        productDescription = addProductDescriptionET.getText().toString().trim();
        productPrice = addProductPriceET.getText().toString().trim();

        if (imageUri == null) {
            Toast.makeText(this, "Please Enter Product Image", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(productName)) {
            Toast.makeText(this, "Please Enter Product Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(productDescription)) {
            Toast.makeText(this, "Please Enter Product Description", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(productPrice)) {
            Toast.makeText(this, "Please Enter Product Price", Toast.LENGTH_SHORT).show();
        } else {
            storeProductInformation();
        }


    }

    private void storeProductInformation() {

        loading.setTitle("Adding New Product");
        loading.setMessage("Please Wait Adding Producing is in process");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = productImageRef.child(imageUri.getLastPathSegment()+productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                loading.dismiss();
                Toast.makeText(AdminAddProductActivity.this, "Error :"+e.toString(), Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddProductActivity.this, "Product Image Uploaded Successfully", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }

                        //downloadedImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful())
                        {
                            downloadedImageUrl = task.getResult().toString();
                            Toast.makeText(AdminAddProductActivity.this, "Got the product image url successfully", Toast.LENGTH_SHORT).show();

                            saveProductInfoToDatabase();
                        }
                    }
                });

            }
        });

    }

    private void saveProductInfoToDatabase()
    {

        HashMap<String,Object> productMap = new HashMap<>();
        productMap.put("pId",productRandomKey);
        productMap.put("pName",productName);
        productMap.put("category",categoryName);
        productMap.put("description",productDescription);
        productMap.put("price",productPrice);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("imageUrl",downloadedImageUrl);

        productDatabaseRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful())
                        {
                            startActivity(new Intent(AdminAddProductActivity.this,AdminCategoryActivity.class));

                            loading.dismiss();
                            Toast.makeText(AdminAddProductActivity.this, "Product is added successfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loading.dismiss();
                            Toast.makeText(AdminAddProductActivity.this, "Error" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, imagePick);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == imagePick && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();

            addProductIV.setImageURI(imageUri);

        }
    }
}
