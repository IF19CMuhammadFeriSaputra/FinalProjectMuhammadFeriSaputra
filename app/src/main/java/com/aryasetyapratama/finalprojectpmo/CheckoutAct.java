package com.aryasetyapratama.finalprojectpmo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CheckoutAct extends AppCompatActivity {

    Button btn_view_order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        btn_view_order = findViewById(R.id.btn_view_order);
        btn_view_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goprofile = new Intent(CheckoutAct.this, EditProfileAct.class);
                startActivity(goprofile);
            }
        });
    }
}
