package com.codewithharry.mmmutmeityproject.fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ColorSpace;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.codewithharry.mmmutmeityproject.R;
import com.codewithharry.mmmutmeityproject.model;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class fragment_for_gallery extends BottomSheetDialogFragment {

    ImageView imageView;
    Button pick, upload;
    ProgressBar progressBar;

    Uri uri;
    int SELECT_IMAGE_CODE= 1;

    StorageReference mStorageRef;

    DatabaseReference root = FirebaseDatabase.getInstance().getReference("Images_from_gallery");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_for_gallery, container, false);

        imageView= view.findViewById(R.id.image_from_gallery);
        progressBar= view.findViewById(R.id.progress_in_gallery);
        pick= view.findViewById(R.id.open);
        upload= view.findViewById(R.id.upload_image);

        progressBar.setVisibility(View.INVISIBLE);

        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uri != null){
                    uploadImage();
                }
                else{
                    Toast.makeText(getContext(), "Please Select A Image", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }
    private void openGallery() {
        Intent intent= new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "title"), SELECT_IMAGE_CODE );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== SELECT_IMAGE_CODE){
            uri= data.getData();
            imageView.setImageURI(uri);
            pick.setText("Picked");

        }
    }

    private void uploadImage() {
        mStorageRef= FirebaseStorage.getInstance().getReference("images/gallery.jpg");

        mStorageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {


            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        model m1= new model(uri.toString());
                        String modelId= root.push().getKey();
                        root.child(modelId).setValue(m1);
                        Toast.makeText(getContext(), "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
                imageView.setImageURI(null);

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "Not Uploaded Succesfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //    private void uploadImage() {
//
//        progressDialog= new ProgressDialog(getContext());
//        progressDialog.setTitle("Uploading File");
//        progressDialog.show();
//
//        SimpleDateFormat formatter= new SimpleDateFormat("yyyy_MM_dd__HH__mm__ss", Locale.CANADA);
//        Date now = new Date();
//        String fileName= formatter.format(now);
//
//        storageReference= FirebaseStorage.getInstance().getReference("images_gallery"+fileName);
//        storageReference.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                imageView.setImageURI(null);
//                Toast.makeText(getContext(), "Succesfully Uploaded", Toast.LENGTH_SHORT).show();
//
//                if(progressDialog.isShowing()){
//                    progressDialog.dismiss();
//                }
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                if(progressDialog.isShowing()){
//                    progressDialog.dismiss();
//                }
//
//                Toast.makeText(getContext(), "Failed To Upload", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

//    private void pickImageFromGallery() {
//        Intent intent= new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        startActivityForResult(intent, IMAGE_PICK_CODE);
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if(resultCode== RESULT_OK && requestCode== IMAGE_PICK_CODE){
//            imageView.setImageURI(data.getData());
//        }
//    }
}