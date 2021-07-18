package com.aryasetyapratama.finalprojectpmo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class OrderDetailAct extends AppCompatActivity {

    ImageView btn_back,header_order_detail;
    Button btn_order, btnplus, btnmines;

    TextView nama_produk, kategori_produk, nilai_produk,
            harga_produk, deskripsi_produk, saldo, jumlah,
            total_bayar;

    Integer mybalance = 0;
    Integer valueJumlah = 1;
    Integer valuetotalharga = 0;
    Integer valuehargaproduk;
    Integer sisa_balance = 0;

    DatabaseReference reference,reference1,reference2,reference3;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    //  generate nomor integer scra random krna ingin membuat transasksi scra unik
    Integer nomor_transaksi = new Random().nextInt();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        getUsernameLocal();

        btn_back = findViewById(R.id.btn_back);
        btn_order = findViewById(R.id.btn_order);


        header_order_detail = findViewById(R.id.header_order_detail);

        nama_produk = findViewById(R.id.nama_produk);
        kategori_produk = findViewById(R.id.kategori_produk);
        nilai_produk = findViewById(R.id.nilai_produk);
        harga_produk = findViewById(R.id.harga_produk);
        deskripsi_produk = findViewById(R.id.deskripsi_produk);
        saldo = findViewById(R.id.saldo);
        jumlah = findViewById(R.id.jumlah);
        total_bayar = findViewById(R.id.total_bayar);


        //mengambil data dari Intent
        Bundle bundle = getIntent().getExtras();
        final String jenis_makanan_baru = bundle.getString("jenis_makanan");

        //inisialisasi setting value baru untuk beberapa komponen
        jumlah.setText(valueJumlah.toString());

        reference1 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //menimpa data yang ada dengan data yg baru

                //saldo.setText(dataSnapshot.child("user_balance").getValue().toString());

                mybalance = Integer.valueOf(dataSnapshot.child("user_balance").getValue().toString());
                saldo.setText("Rp. "+mybalance+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //run the button




        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //menyimpan data users dari firebase dan membuat tabel baru "My Order"
                reference2 = FirebaseDatabase.getInstance().getReference().child("My Orders").child(username_key_new).child(nama_produk.getText().toString() + nomor_transaksi );
                reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        reference2.getRef().child("id_produk").setValue(nama_produk.getText().toString() + nomor_transaksi);
                        reference2.getRef().child("nama_produk").setValue(nama_produk.getText().toString());
                        reference2.getRef().child("kategori_produk").setValue(kategori_produk.getText().toString());
                        reference2.getRef().child("nilai_produk").setValue(nilai_produk.getText().toString());
                        reference2.getRef().child("deskripsi_produk").setValue(deskripsi_produk.getText().toString());
                        reference2.getRef().child("jumlah_produk").setValue(harga_produk.toString());

                        Intent gotosuccess = new Intent(OrderDetailAct.this, SuccessCheckoutAct.class);
                        startActivity(gotosuccess);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                //update data balance ke user (yang saat ini login)
                //mengambil data dari firebase berdasarkan Intent
                reference3 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
                reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        sisa_balance = mybalance - valuetotalharga;
                        reference3.getRef().child("user_balance").setValue(sisa_balance);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        //mengambil data dari firebase berdasarkan Intent
        reference = FirebaseDatabase.getInstance().getReference().child("Makanan").child(jenis_makanan_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()){
                        //menimpa data yang ada dengan data yg baru
                        nama_produk.setText(dataSnapshot.child("nama_produk").getValue(String.class));
                        kategori_produk.setText(dataSnapshot.child("kategori_produk").getValue(String.class));
                        nilai_produk.setText(dataSnapshot.child("nilai_produk").getValue(String.class));
                        harga_produk.setText("Rp. "+dataSnapshot.child("harga_produk").getValue(String.class));
                        valuehargaproduk = Integer.valueOf(dataSnapshot.child("harga_produk").getValue().toString());
                        valuetotalharga = valuehargaproduk*valueJumlah;
                        total_bayar.setText("Rp. " + valuetotalharga.toString());
                        deskripsi_produk.setText(dataSnapshot.child("deskripsi_produk").getValue(String.class));
                        Picasso.with(OrderDetailAct.this).load(dataSnapshot.child("url_thumbnail").getValue(String.class)).centerCrop().fit().into(header_order_detail);

                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }
}
