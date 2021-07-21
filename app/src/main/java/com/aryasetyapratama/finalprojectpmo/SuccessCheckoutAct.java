package com.aryasetyapratama.finalprojectpmo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SuccessCheckoutAct extends AppCompatActivity {

    Button btn_home,btn_profile;
    ImageView ilu_success;
    TextView title,desc;
    Animation app_splash, btt, ttb;
    RatingBar rating;
    Button btTampilkanRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_checkout);

        rating=(RatingBar)findViewById(R.id.rating);
        btTampilkanRating=(Button)findViewById(R.id.btTampilkanRating);
        btTampilkanRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nilairating= String.valueOf(rating.getRating());
                Toast.makeText(SuccessCheckoutAct.this, "Ratingnya adalah : "+nilairating, Toast.LENGTH_SHORT).show();
            }
        });

        btn_home = findViewById(R.id.btn_home);
        btn_profile = findViewById(R.id.btn_profile);

        //load animation
        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash);
        btt = AnimationUtils.loadAnimation(this, R.anim.btt);
        ttb = AnimationUtils.loadAnimation(this, R.anim.ttb);

        //load element
        title = findViewById(R.id.title);
        desc = findViewById(R.id.desc);
        ilu_success = findViewById(R.id.ilu_success);

        //run animation
        ilu_success.startAnimation(app_splash);

        title.startAnimation(ttb);
        desc.startAnimation(ttb);

        btn_home.startAnimation(btt);
        btn_profile.startAnimation(btt);

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backhome = new Intent(SuccessCheckoutAct.this, HomeAct.class);
                startActivity(backhome);
                finish();
            }
        });


        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goprofile = new Intent(SuccessCheckoutAct.this, EditProfileAct.class);
                startActivity(goprofile);
                finish();
            }
        });
    }
}
