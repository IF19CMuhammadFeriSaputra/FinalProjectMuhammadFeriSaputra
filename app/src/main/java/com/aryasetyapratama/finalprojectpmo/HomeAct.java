package com.aryasetyapratama.finalprojectpmo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeAct extends AppCompatActivity {

    LinearLayout produk_naskun,produk_pisjo,produk_kupas,produk_sate,produk_iga,produk_mie;
    ImageView photo_home_user;
    TextView user_balance, nama_user;

    DatabaseReference reference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getUsernameLocal();

        nama_user = findViewById(R.id.nama_user);
        photo_home_user = findViewById(R.id.photo_home_user);
        user_balance = findViewById(R.id.user_balance);

        produk_naskun = findViewById(R.id.produk_naskun);
        produk_pisjo = findViewById(R.id.produk_pisjo);
        produk_kupas = findViewById(R.id.produk_kupas);
        produk_sate = findViewById(R.id.produk_sate);
        produk_iga = findViewById(R.id.produk_iga);
        produk_mie = findViewById(R.id.produk_mie);


        photo_home_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goprofile = new Intent(HomeAct.this, EditProfileAct.class);
                startActivity(goprofile);
            }
        });


        //mengambil data yg saat user itu coba login
        reference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(username_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nama_user.setText(dataSnapshot.child("name").getValue().toString());
                user_balance.setText("Rp. " + dataSnapshot.child("user_balance").getValue().toString());
                String url_photo = dataSnapshot.child("url_photo_profile").getValue().toString();
                if(url_photo.matches("")){

                }else{
                    Picasso.with(HomeAct.this)
                            .load(dataSnapshot.child("url_photo_profile")
                                    .getValue().toString()).centerCrop().fit()
                            .into(photo_home_user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        produk_naskun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotonaskun = new Intent(HomeAct.this, OrderDetailAct.class);
                //meletakan data ke intent
                gotonaskun.putExtra("jenis_makanan", "Nasi Kuning");
                startActivity(gotonaskun);
            }
        });

        produk_pisjo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotonaskun = new Intent(HomeAct.this, OrderDetailAct.class);
                //meletakan data ke intent
                gotonaskun.putExtra("jenis_makanan", "Es Pisang Ijo");
                startActivity(gotonaskun);
            }
        });

        produk_kupas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotonaskun = new Intent(HomeAct.this, OrderDetailAct.class);
                //meletakan data ke intent
                gotonaskun.putExtra("jenis_makanan", "Kue Pasar");
                startActivity(gotonaskun);
            }
        });

        produk_sate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotonaskun = new Intent(HomeAct.this, OrderDetailAct.class);
                //meletakan data ke intent
                gotonaskun.putExtra("jenis_makanan", "Sate Madura");
                startActivity(gotonaskun);
            }
        });

        produk_iga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotonaskun = new Intent(HomeAct.this, OrderDetailAct.class);
                //meletakan data ke intent
                gotonaskun.putExtra("jenis_makanan", "Iga Bakar");
                startActivity(gotonaskun);
            }
        });

        produk_mie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotonaskun = new Intent(HomeAct.this, OrderDetailAct.class);
                //meletakan data ke intent
                gotonaskun.putExtra("jenis_makanan", "Mie Warkop");
                startActivity(gotonaskun);
            }
        });
    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }

    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

}