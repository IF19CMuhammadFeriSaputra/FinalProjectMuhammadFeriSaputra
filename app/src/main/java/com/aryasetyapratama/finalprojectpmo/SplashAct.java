package com.aryasetyapratama.finalprojectpmo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashAct extends AppCompatActivity {

    ImageView app_logo;
    Animation app_splash;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getUsernameLocal();

        //load animation
        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash);

        //load element
        app_logo = findViewById(R.id.app_logo);

        //run animation
        app_logo.startAnimation(app_splash);
    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
        if(username_key_new.isEmpty()){
            //setting timer 2 detik
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent gosignin = new Intent(SplashAct.this, com.aryasetyapratama.finalprojectpmo.SignInAct.class);
                    startActivity(gosignin);
                    finish();
                }
            }, 2000);
        }
        else{
            //setting timer 2 detik
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent gohome = new Intent(SplashAct.this, com.aryasetyapratama.finalprojectpmo.HomeAct.class);
                    startActivity(gohome);
                    finish();
                }
            }, 2000);
        }
    }
}
