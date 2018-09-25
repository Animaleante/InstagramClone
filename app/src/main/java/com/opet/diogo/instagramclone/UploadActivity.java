package com.opet.diogo.instagramclone;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import me.iwf.photopicker.PhotoPicker;

/**
 * Created by opet on 24/09/2018.
 */

public class UploadActivity extends Activity {

    private String selectedPhoto;

    private ImageView imagePreview;
    private EditText txtComment;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_upload);

        imagePreview = findViewById(R.id.imagem_preview);
        txtComment = findViewById(R.id.txt_comment);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
    }

    public void selectPhoto(View view) {
        PhotoPicker.builder()
                .setPhotoCount(1)
                .setShowCamera(true)
                .setShowGif(true)
                .setPreviewEnabled(false)
                .start(this, PhotoPicker.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if(data != null) {
                selectedPhoto = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS).get(0);
                imagePreview.setImageURI(Uri.parse(selectedPhoto));
            }
        }
    }

    public void savePhoto(View view) {
        if(selectedPhoto != null && !selectedPhoto.equals("") && !txtComment.getText().toString().equals("")) {
            Uri file = Uri.fromFile(new File(selectedPhoto));
            StorageReference photoRef = mStorageRef.child("images/"+System.currentTimeMillis());
            photoRef.putFile(file)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Photo photo = new Photo(taskSnapshot.getDownloadUrl().toString(), txtComment.getText().toString());
                            mDatabaseRef.child("photos").child(String.valueOf(System.currentTimeMillis())).setValue(photo)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(UploadActivity.this, "Foto salva com sucesso!", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UploadActivity.this, "Falha ao salvar foto.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(UploadActivity.this, "Dados incompletos", Toast.LENGTH_SHORT).show();
        }
    }
}
