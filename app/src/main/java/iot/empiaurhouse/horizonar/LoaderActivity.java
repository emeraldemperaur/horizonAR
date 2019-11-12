package iot.empiaurhouse.horizonar;


import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


public class LoaderActivity extends RuntimePermissionsActivity {

    SharedPreferences horizonPreferences;
    private Window window;
    private TextView horizonLogoText;
    private TextView horizonLogoSubText;
    private ImageView horizonLogo;
    private Animation fadeIn;
    private Animation slideDown;
    private static final int REQUEST_PERMISSIONS = 20;

    private String displayName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_loader);
        window = this.getWindow();
        navShader();
        horizonLogoText = findViewById(R.id.horizonLogoText);
        horizonLogoSubText = findViewById(R.id.horizonLogoSubText);
        horizonLogo = findViewById(R.id.horizonLogo);


        this.titleIntroFX();
        this.permitCheck();

    }



    @Override
    public void onPermissionsGranted(final int requestCode) {
        //Toast.makeText(this, "Permissions Received.", Toast.LENGTH_LONG).show();
        userValidator();

    }



    @Override
    protected void onPause()
    {
        super.onPause();
        horizonLogoText.clearAnimation();
        horizonLogoSubText.clearAnimation();



    }


    @Override
    protected void onResume() {
        super.onResume();

    }



    private void navShader(){


        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));


    }



    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            this.moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }




    private void titleIntroFX(){

        slideDown = AnimationUtils.loadAnimation(this, R.anim.slidedown);
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fadefx);

        horizonLogoText.startAnimation(fadeIn);
        horizonLogoText.setVisibility(View.VISIBLE);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


                horizonLogoSubText.startAnimation(slideDown);
                horizonLogoSubText.setVisibility(View.VISIBLE);

            }
        }, 3000);


    }



    private void userValidator(){

        horizonPreferences = getApplicationContext().getSharedPreferences("HORIZON_PREFERENCES",0);
        displayName = horizonPreferences.getString("displayName",null);

        if(displayName == null){

            startActivity(new Intent(this, SignInActivity.class));
            finish();


        }else  {



                startActivity(new Intent(this, PrologueActivity.class));
                finish();


            }


    }



    private void permitCheck(){

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


                LoaderActivity.super.requestAppPermissions(new
                                String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, R.string
                                .runtime_permissions_txt
                        , REQUEST_PERMISSIONS);

            }
        }, 4000);


    }





}
