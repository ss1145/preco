package com.example.lenovo.preco;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

public class SplashMain extends AppCompatActivity {
    final String welcomeScreenShownPref = "welcomeScreenShown";
    LinearLayout l2, l3;
    Animation uptodown, downtoup;
    SharedPreferences sharedPreferences;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashmain);
        l2 = findViewById(R.id.l2);
        l3 = findViewById(R.id.l3);
        uptodown = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        l2.setAnimation(uptodown);
        downtoup = AnimationUtils.loadAnimation(this, R.anim.downtoup);
        l3.setAnimation(downtoup);

        sharedPreferences = getSharedPreferences("ShaPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean firstTime = sharedPreferences.getBoolean("first", true);
        if (firstTime) {
            editor.putBoolean("first", false);
            Intent intent = new Intent(SplashMain.this, Splash.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(SplashMain.this, SplashMain.class);
            startActivity(intent);
        }
       /* mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean welcomeScreenShown = mPrefs.getBoolean(welcomeScreenShownPref, false);

        if (!welcomeScreenShown) {

          Intent i = new Intent(this,Splash.class);
          startActivity(i);
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putBoolean(welcomeScreenShownPref, true);
            editor.commit();
        }*/
        b1 = findViewById(R.id.b1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Splash.class);
                startActivity(i);
            }
        });
    }
}
