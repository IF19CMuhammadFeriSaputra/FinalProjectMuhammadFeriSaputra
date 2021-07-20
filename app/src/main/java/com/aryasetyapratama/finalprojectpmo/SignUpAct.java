package com.aryasetyapratama.finalprojectpmo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpAct extends AppCompatActivity {

    ImageView btn_back;
    Button btn_next;
    EditText username, password,name, email_address;

    DatabaseReference reference, reference_username;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        name = findViewById(R.id.name);
        email_address = findViewById(R.id.email_address);

        btn_next = findViewById(R.id.btn_next);
        btn_back =findViewById(R.id.btn_back);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //mengubah state menjadi loading
                btn_next.setEnabled(false);
                btn_next.setText("Loading ...");

                //mengambil username pada firebase Database
                reference_username = FirebaseDatabase.getInstance().getReference().child("Users").child(username.getText().toString());
                reference_username.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //jika username tersedia
                        if(dataSnapshot.exists()){
                            Toast.makeText(getApplicationContext(),"Username sudah tersedia !", Toast.LENGTH_SHORT).show();
                            btn_next.setEnabled(true);
                            btn_next.setText("NEXT");

                        }
                        else {
                            // menyimpan data kepada local storage atau hp
                            SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(username_key, username.getText().toString());
                            editor.apply();

                            // menyimpan ke database
                            reference = FirebaseDatabase.getInstance().getReference()
                                    .child("Users").child(username.getText().toString());

                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    dataSnapshot.getRef().child("username").setValue(username.getText().toString());
                                    dataSnapshot.getRef().child("password").setValue(password.getText().toString());
                                    dataSnapshot.getRef().child("name").setValue(name.getText().toString());
                                    dataSnapshot.getRef().child("email_address").setValue(email_address.getText().toString());
                                    dataSnapshot.getRef().child("user_balance").setValue(80000);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            Intent gosignuptwo = new Intent(SignUpAct.this, com.aryasetyapratama.finalprojectpmo.SignUpTwo.class);
                            startActivity(gosignuptwo);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
