package com.example.muvmuv2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public class EditHeaderProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView photoHeader;
    private TextView cancel, save;
    private Uri imageUri;
    private FirebaseUser currentUser;
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_header_profile);

        photoHeader = findViewById(R.id.photoHeader);
        cancel = findViewById(R.id.cancel);
        save = findViewById(R.id.save);

        // Initialize Firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
        mStorageRef = FirebaseStorage.getInstance().getReference();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        photoHeader.setOnClickListener(v -> openFileChooser());
        save.setOnClickListener(v -> {
            if (imageUri != null) {
                uploadImageToFirebase();
            } else {
                Toast.makeText(EditHeaderProfileActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
            }
        });

        cancel.setOnClickListener(v -> finish());
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                photoHeader.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebase() {
        progressDialog.show();

        final StorageReference fileReference = mStorageRef.child("photoHeader/" + UUID.randomUUID().toString() + ".jpg");

        photoHeader.setDrawingCacheEnabled(true);
        photoHeader.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) photoHeader.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = fileReference.putBytes(data);
        uploadTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    String downloadUrl = uri.toString();
                    mDatabase.child("photo").setValue(downloadUrl)
                            .addOnCompleteListener(task1 -> {
                                progressDialog.dismiss();
                                if (task1.isSuccessful()) {
                                    Toast.makeText(EditHeaderProfileActivity.this, "Profile header updated", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(EditHeaderProfileActivity.this, "Failed to update profile header", Toast.LENGTH_SHORT).show();
                                }
                            });
                });
            } else {
                progressDialog.dismiss();
                Toast.makeText(EditHeaderProfileActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
