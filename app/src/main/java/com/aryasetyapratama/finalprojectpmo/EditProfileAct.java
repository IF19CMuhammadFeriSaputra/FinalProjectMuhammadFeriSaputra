package com.aryasetyapratama.finalprojectpmo;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditProfileAct extends AppCompatActivity {

    ImageView btn_back,photo_edit_profile;
    Button btn_save,btn_sign_out,btn_hapus_photo;
    EditText xpassword,xname,xemail;

    Uri photo_location;
    Integer photo_max = 1;

    DatabaseReference reference, reference2;
    StorageReference storage;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        btn_back = findViewById(R.id.btn_back);
        btn_save = findViewById(R.id.btn_save);
        btn_sign_out = findViewById(R.id.btn_sign_out);
        btn_hapus_photo = findViewById(R.id.btn_hapus_photo);

        photo_edit_profile = findViewById(R.id.photo_edit_profile);

        xpassword = findViewById(R.id.xpassword);
        xname = findViewById(R.id.xname);
        xemail = findViewById(R.id.xemail);

        getUsernameLocal();



        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent backtohome = new Intent(EditProfileAct.this, com.aryasetyapratama.finalprojectpmo.HomeAct.class);
               startActivity(backtohome);
            }
        });

        //mengambil data yg saat user itu coba login
        reference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(username_key_new);
        storage = FirebaseStorage.getInstance().getReference().child("Photousers").child(username_key_new);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                xpassword.setText(dataSnapshot.child("password").getValue().toString());
                xname.setText(dataSnapshot.child("name").getValue().toString());
                xemail.setText(dataSnapshot.child("email_address").getValue().toString());
                String url_photo = dataSnapshot.child("url_photo_profile").getValue().toString();
                if(url_photo.matches("")){

                }else{
                    Picasso.with(EditProfileAct.this)
                            .load(dataSnapshot.child("url_photo_profile")
                                    .getValue().toString()).centerCrop().fit()
                            .into(photo_edit_profile);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_hapus_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference2 = FirebaseDatabase.getInstance().getReference()
                        .child("Users").child(username_key_new).child("url_photo_profile");
                reference2.setValue("");
                Toast.makeText(getApplicationContext(), "Foto Terhapus", Toast.LENGTH_SHORT).show();
                Intent refresh = new Intent(EditProfileAct.this, EditProfileAct.class);
                startActivity(refresh);
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ubah state mnjdi loading
                btn_save.setEnabled(false);
                btn_save.setText("Loading ...");

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("password").setValue(xpassword.getText().toString());
                        dataSnapshot.getRef().child("name").setValue(xname.getText().toString());
                        dataSnapshot.getRef().child("email_address").setValue(xemail.getText().toString());
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //validasi untuk file (apakah ada?)
                if(photo_location != null){
                    final StorageReference storageReference1 = storage.child(System.currentTimeMillis() + "." +
                            getFileExtension(photo_location));

                    storageReference1.putFile(photo_location)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    storageReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String uri_photo = uri.toString();
                                            reference.getRef().child("url_photo_profile").setValue(uri_photo);
                                        }
                                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            //tampilin toast
                                            Toast.makeText(getApplicationContext(), "Profile Berhasi Diubah", Toast.LENGTH_SHORT).show();
                                            btn_save.setEnabled(true);
                                            btn_save.setText("SAVE DATA");
                                        }
                                    });

                                }
                            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        }
                    });

                }else{
                    Toast.makeText(getApplicationContext(), "Profile Berhasi Diubah", Toast.LENGTH_SHORT).show();
                    btn_save.setEnabled(true);
                    btn_save.setText("SAVE DATA");
                }
            }
        });

        btn_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //menghapus isi / nilai / value dari username local
                // menyimpan data kepada local storage atau hp
                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(username_key, null);
                editor.apply();

                // pindah act
                Intent gosign = new Intent(EditProfileAct.this, SplashAct.class);
                startActivity(gosign);
                finish();
            }
        });


        photo_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPhoto();
            }
        });
    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }

    String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void findPhoto(){
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic, photo_max);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == photo_max && resultCode == RESULT_OK && data != null && data.getData() != null){

            photo_location = data.getData();
            Picasso.with(this).load(photo_location).centerCrop().fit().into(photo_edit_profile);

        }

    }
}
