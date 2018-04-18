package com.appify.andelatakeaclimbchallenge;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class EditProfileActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;
    private EditText nameEt;
    private Button btnAdd;
    private Toolbar toolbar;
    private DatabaseReference mDatabaseRef;
    private ArrayList<User> mUsers;
    private User mUser;
    private Uri filePath;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");
        id = FirebaseAuth.getInstance().getUid();

        imageView = (ImageView)findViewById(R.id.imageView);
        nameEt = (EditText)findViewById(R.id.nameEt);
        btnAdd = (Button)findViewById(R.id.btnAdd);
        toolbar = (Toolbar)findViewById(R.id.toolbar);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog = new ProgressDialog(EditProfileActivity.this);
                progressDialog.show();

                if (filePath != null) {
                    StorageReference storage = FirebaseStorage.getInstance().getReference("User Images/" + id + ".jpg");
                    storage.putFile(filePath)
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    int progress = (int)(taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount()) * 100;
                                    progressDialog.setProgress(progress);
                                }
                            })
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.dismiss();
                                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                    String imgUrl = downloadUrl.toString();
                                    User user = new User(id, nameEt.getText().toString(), imgUrl);
                                    mDatabaseRef.child(id).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(EditProfileActivity.this, "User profile updated successfully", Toast.LENGTH_LONG)
                                                    .show();
                                            finish();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                        }
                    });
                } else {
                    User user = new User(id, nameEt.getText().toString(), "");
                    mDatabaseRef.child(id).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(EditProfileActivity.this, "User added successfully", Toast.LENGTH_LONG)
                                    .show();
                            finish();
                        }
                    });
                }
            }
        });

        setSupportActionBar(toolbar);
        toolbar.setTitle("Edit Profile ");
        EditProfileActivity.this.setTitle("Edit Profile");
    }

    @Override
    protected void onStart() {
        super.onStart();

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUsers = new ArrayList<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    mUsers.add(user);
                }
                for (User user: mUsers) {
                    if (user.getId().equals(id)) {
                        mUser = user;
                        nameEt.setText(mUser.getName());

                        if (mUser.getImageUrl().equals(""))
                            imageView.setImageResource(R.drawable.common_google_signin_btn_icon_dark);
                        else {
                            ImageLoader.getInstance().displayImage(mUser.getImageUrl(), imageView);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
