package com.codewithharry.mmmutmeityproject.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.codewithharry.mmmutmeityproject.ActivityForCard;
import com.codewithharry.mmmutmeityproject.Model_for_camera;
import com.codewithharry.mmmutmeityproject.R;
import com.codewithharry.mmmutmeityproject.databinding.FragmentHomeBinding;
import com.codewithharry.mmmutmeityproject.fragment.fragment_for_gallery;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    ImageView imageView_camera;
    StorageReference mStorageRef;
    DatabaseReference root= FirebaseDatabase.getInstance().getReference("image_from_camera");
    int SELECT_PHOTO= 1;

    public CardView c11, c12, c21, c22, c31, c32;
    ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        c11= binding.card11;
        c12= binding.card12;
        c21= binding.card21;
        c22= binding.card22;
        c31= binding.card31;
        c32= binding.card32;



        c11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent11= new Intent(getContext(), ActivityForCard.class);
                startActivity(intent11);
            }
        });

        c12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent12= new Intent(getContext(), ActivityForCard.class);
                startActivity(intent12);
            }
        });
        c21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent21= new Intent(getContext(), ActivityForCard.class);
                startActivity(intent21);
            }
        });
        c22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent22= new Intent(getContext(), ActivityForCard.class);
                startActivity(intent22);
            }
        });
        c31.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent31= new Intent(getContext(), ActivityForCard.class);
                startActivity(intent31);
            }
        });
        c32.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent32= new Intent(getContext(), ActivityForCard.class);
                startActivity(intent32);
            }
        });


        mStorageRef= FirebaseStorage.getInstance().getReference();

        imageView_camera= binding.camera;
        imageView_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setIcon(R.drawable.image_search)
                        .setTitle("Choose Image")
                        .setMessage("Select your image resource")
                        .setPositiveButton("CAMERA", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(intent, SELECT_PHOTO);
                                }catch (Exception e){
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }

                            }
                        }).setNegativeButton("GALLERY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        fragment_for_gallery fragment_for_gallery= new fragment_for_gallery();
                        fragment_for_gallery.show(getActivity().getSupportFragmentManager(), fragment_for_gallery.getTag());

                    }
                }).show();
            }
        });

        return root;
    }

    // image from camera


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== SELECT_PHOTO){
//            imageUri= data.getData();
            onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data){
        Bitmap thumbnail= (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes= new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        byte bb[]= bytes.toByteArray();


        uploadToFirebase(bb);

    }

    private void uploadToFirebase(byte[] bb){
        StorageReference sr= mStorageRef.child("images/space.jpg");
        sr.putBytes(bb).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

//                sr.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//
//                        Model_for_camera mfc= new Model_for_camera(bb.toString());
//                        String model_cameraId= root.push().getKey();
//                        root.child(model_cameraId).setValue(mfc);
//                        Toast.makeText(getContext(), "Successfully Upload", Toast.LENGTH_SHORT).show();
//                        progressBar.setVisibility(View.INVISIBLE);
//                    }
//                });

                Toast.makeText(getContext(), "we'll resolve this soon", Toast.LENGTH_SHORT).show();


            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed To upload", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}