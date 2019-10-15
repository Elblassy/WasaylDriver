package com.elblasy.wasayldriver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.elblasy.wasayldriver.utiles.LocaleUtils;
import com.elblasy.wasayldriver.utiles.SharedPref;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class VerifyDeriver extends AppCompatActivity {

    private Button frontImage, backImage, next;
    private CircleImageView profileImage;
    private EditText idNumber;

    private Uri frontImagePath, backImagePath, profileImagePath;

    private final int PICK_FRONT_IMAGE_REQUEST = 1;
    private final int PICK_BACK_IMAGE_REQUEST = 2;
    private final int PICK_PROFILE_IMAGE_REQUEST = 3;


    FirebaseStorage storage;
    StorageReference storageReference;
    SharedPref sharedPref;
    byte[] frontByte, backByte;

    DatabaseReference databaseReference;

    public VerifyDeriver() {
        LocaleUtils.updateConfig(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_deriver);

        frontImage = findViewById(R.id.id_image_front);
        backImage = findViewById(R.id.id_image_back);
        next = findViewById(R.id.next);
        profileImage = findViewById(R.id.circleImageView);
        idNumber = findViewById(R.id.id_number);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        sharedPref = new SharedPref(this);

        frontImage.setOnClickListener(view -> chooseFrontImage());
        backImage.setOnClickListener(view -> chooseBackImage());

        databaseReference = FirebaseDatabase.getInstance().getReference("users").
                child("Drivers")
                .child(SharedPref.getSessionValue("City"))
                .child(SharedPref.getSessionValue("PhoneNumber"));

        next.setOnClickListener(view -> {

            if (frontImagePath == null || backImagePath == null) {
                Toast.makeText(VerifyDeriver.this, "من فضلك قم بؤفع صورة البطاقة وجه وظهر", Toast.LENGTH_LONG).show();
                return;
            }
            if (idNumber.getText().toString().isEmpty() || idNumber.getText().toString().length() < 14) {
                Toast.makeText(VerifyDeriver.this, "من فضلك قم بادخال رقم بطاقتك بشكل صحيح", Toast.LENGTH_LONG).show();
                return;
            }
            uploadImage();
        });

        profileImage.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PROFILE_IMAGE_REQUEST);
        });

    }

    private void chooseFrontImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_FRONT_IMAGE_REQUEST);
    }

    private void chooseBackImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_BACK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FRONT_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            frontImagePath = data.getData();

            Bitmap bm = null;
            try {
                bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), frontImagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 20, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            frontByte = b;

        }
        if (requestCode == PICK_BACK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            backImagePath = data.getData();

            Bitmap bm = null;
            try {
                bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), backImagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 20, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            backByte = b;

        }

        if (requestCode == PICK_PROFILE_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            profileImagePath = data.getData();
            Log.e("LOOK", profileImagePath.toString());
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), profileImagePath);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                byte[] b = baos.toByteArray();
                String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                sharedPref.setProfileImage(encodedImage);
                profileImage.setImageURI(profileImagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }



        }
    }

    private void uploadImage() {

        if (frontImagePath != null && backImagePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            //upload front image
            StorageReference ref = storageReference.child("IDs/" + SharedPref.getSessionValue("PhoneNumber")).child("front");
            ref.putBytes(frontByte)
                    .addOnSuccessListener(taskSnapshot -> {
                        progressDialog.dismiss();
                        Toast.makeText(VerifyDeriver.this, "Uploaded front", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(VerifyDeriver.this, "من فضلك قم برفع صور بطاقتك الشخصية", Toast.LENGTH_SHORT).show();
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Uploaded front image  " + (int) progress + "%");
                    });
            //upload back image
            StorageReference ref2 = storageReference.child("IDs/" + SharedPref.getSessionValue("PhoneNumber")).child("back");
            ref2.putBytes(backByte)
                    .addOnSuccessListener(taskSnapshot -> {
                        progressDialog.dismiss();
                        Intent intent = new Intent(VerifyDeriver.this,MainActivity.class);
                        startActivity(intent);
                        databaseReference.child("verified").setValue(1);
                        Toast.makeText(VerifyDeriver.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(VerifyDeriver.this, "من فضلك قم برفع صور بطاقتك الشخصية", Toast.LENGTH_SHORT).show();
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Uploaded back image  " + (int) progress + "%");
                    });

        }
    }
}
