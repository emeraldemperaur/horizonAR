package iot.empiaurhouse.horizonar;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class PrologueActivity extends RuntimePermissionsActivity implements LocationProvider.LocationCallback{

    public static final String TAG = PrologueActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS = 20;
    SharedPreferences horizonPreferences;
    private LocationProvider horizonLocationProvider;

    private InterstitialAd prologueInterstitialAd;

    private TextView horizonLocation;
    private TextView prologueGreeting;
    private TextView prologueUserName;
    private TypeWriterTextView realityTextA;
    private TextView realityTextB;
    private TextView loadingText;
    private Animation fadeIn;
    private Animation slideDown;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_prologue);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        horizonLocationProvider = new LocationProvider(this, this);
        horizonLocation = findViewById(R.id.loadingCity);
        loadingText = findViewById(R.id.loadingText);
        prologueGreeting = findViewById(R.id.prologueGreeting);
        prologueUserName = findViewById(R.id.prologueUserName);
        realityTextA = findViewById(R.id.realityTextA);
        realityTextB = findViewById(R.id.realityTextB);
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade);
        slideDown = AnimationUtils.loadAnimation(this, R.anim.slidedown);
        this.permitCheck();
        this.loadingDirector();

    }



    @Override
    protected void onPause() {
        super.onPause();

        horizonLocationProvider.disconnect();
        loadingText.clearAnimation();
        realityTextA.clearAnimation();
        realityTextB.clearAnimation();
        horizonLocation.clearAnimation();


    }


    @Override
    protected void onResume() {
        super.onResume();
        horizonLocationProvider.connect();


    }



    private void typeWriterText(String realityText, Integer charDelay, TypeWriterTextView realityTextView){
       realityTextView.setCharacterDelay(charDelay);
       realityTextView.displayTextWithAnimation(realityText);

    }




    private void permitCheck() {

        PrologueActivity.super.requestAppPermissions(new
                        String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CAMERA}, R.string
                        .runtime_permissions_txt
                , REQUEST_PERMISSIONS);


    }






    private void loadingDirector(){

        this.loadingTextFX();
        this.realityTextFX();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                adExec(getString(R.string.google_live_adMob_id),getString(R.string.live_ad_unit_id));
            }
        }, 3300);



    }



    private void loadingTextFX(){

        loadingText.startAnimation(fadeIn);
        loadingText.setVisibility(View.VISIBLE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                horizonLocation.startAnimation(slideDown);
                horizonLocation.setVisibility(View.VISIBLE);

            }
        }, 1800);

    }



    private void realityTextFX(){

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                realityTextA.startAnimation(fadeIn);
                realityTextB.startAnimation(fadeIn);
                realityTextA.setVisibility(View.VISIBLE);
                realityTextB.setVisibility(View.VISIBLE);
                typeWriterText(getString(R.string.augmented_reality),190,realityTextA);

            }
        }, 2500);




    }






    @Override
    public void onPermissionsGranted(final int requestCode) {
        //Toast.makeText(this, "Permissions Received.", Toast.LENGTH_LONG).show();

    }



    public void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(currentLatitude, currentLongitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String City = addresses.get(0).getLocality();
            String State = addresses.get(0).getAdminArea();
            String Country = addresses.get(0).getCountryName();
            if(City != null){
                horizonLocation.setText(City);
                horizonPreferences = getApplicationContext().getSharedPreferences("HORIZON_PREFERENCES", 0);
                SharedPreferences.Editor horizonAR_IO = horizonPreferences.edit();
                horizonAR_IO.putString("horizonLocation", City);
                horizonAR_IO.putString("horizonCountry", Country);
                horizonAR_IO.putString("horizonState", State);
                horizonAR_IO.apply();
            }else{
                horizonLocation.setText(State);
                horizonPreferences = getApplicationContext().getSharedPreferences("HORIZON_PREFERENCES", 0);
                SharedPreferences.Editor horizonAR_IO = horizonPreferences.edit();
                horizonAR_IO.putString("horizonLocation", State);
                horizonAR_IO.putString("horizonCountry", Country);
                horizonAR_IO.apply();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }



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




    private void adExec(String AdMobID, String AdUnitId){


        MobileAds.initialize(this,
                AdMobID);

        prologueInterstitialAd = new InterstitialAd(this);
        prologueInterstitialAd.setAdUnitId(AdUnitId);
        prologueInterstitialAd.loadAd(new AdRequest.Builder().build());




        prologueInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                if (prologueInterstitialAd.isLoaded()) {
                    prologueInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                purviewIntent();
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                purviewIntent();
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                purviewIntent();
            }
        });



    }




    private void purviewIntent(){

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if((Objects.requireNonNull(locationManager)).isProviderEnabled(LocationManager.GPS_PROVIDER) && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            startActivity(new Intent(this, ARPurviewActivity.class));
            finish();

        }
        else {

            this.locationToast();


        }



    }




    private void locationToast(){

        View horizontoastView = getLayoutInflater().inflate(R.layout.horizon_toast_view, null);

        // Initiate the Toast instance.
        Toast toast = new Toast(getApplicationContext());
        // Set custom view in toast.
        toast.setView(horizontoastView);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0,0);
        toast.show();


    }








}
