package iot.empiaurhouse.horizonar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Objects;

public class LaunchActivity extends AppCompatActivity {

    private ImageView logoBW;
    private ImageView logoWB;
    private LinearLayout activityView;
    private Animation animationFadeIn;
    private Window window;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        window = this.getWindow();
        window.getDecorView().setBackgroundColor(Color.WHITE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);


        logoBW = findViewById(R.id.empiaurhouse_BWlogo);
        logoWB = findViewById(R.id.empiaurhouse_WBlogo);
        activityView = findViewById(R.id.launcher_activity);
        this.EHAnimator();






    }



    private void loaderIntent(){
        Intent loaderIntent = new Intent(this, LoaderActivity.class);
        startActivity(loaderIntent);
        finish();

    }


    private void permissionIntent(){
        Intent permissionIntent = new Intent(this, PermissionActivity.class);
        startActivity(permissionIntent);
        finish();


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



    @Override
    protected void onResume()
    {
        super.onResume();



    }



    @Override
    protected void onStop()
    {
        super.onStop();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

    }


    @Override
    protected void onPause()
    {
        super.onPause();




    }



    private void EHAnimator(){

        animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fader);




        Runnable _animationRunnable = new Runnable() {
            public void run() {
                //animate activity background and ui elements

                logoBW.setVisibility(View.INVISIBLE);

                int colorFrom = getResources().getColor(R.color.white);
                int colorTo = getResources().getColor(R.color.colorPrimary);
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                colorAnimation.setDuration(750);
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {


                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        activityView.setBackgroundColor((int) animator.getAnimatedValue());
                    }

                });
                colorAnimation.start();
                navShader();




                logoWB.setVisibility(View.VISIBLE);
                logoWB.startAnimation(animationFadeIn);

            }
        };
        Handler _animHandler = new Handler();
        _animHandler.postDelayed(_animationRunnable, 3215);


        Runnable _intentRunnable = new Runnable() {
            public void run() {


                servicesIntentChecker();

            }
        };
        Handler _LoaderIntentHandler = new Handler();
        _LoaderIntentHandler.postDelayed(_intentRunnable, 10440);




    }



    private void navShader(){

        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));


    }


    private void servicesIntentChecker(){

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(!(Objects.requireNonNull(locationManager)).isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //Google Location Services Disabled
            this.permissionIntent();

        }
        else {

            this.loaderIntent();
        }



    }





}
