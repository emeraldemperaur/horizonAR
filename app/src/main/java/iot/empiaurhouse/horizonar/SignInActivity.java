package iot.empiaurhouse.horizonar;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignInActivity extends RuntimePermissionsActivity implements LocationProvider.LocationCallback {

    SharedPreferences horizonPreferences;
    private LocationProvider horizonLocationProvider;
    private VideoView signInBanner;
    private TextView bannerLocation;
    private EditText userName;
    private EditText phoneNumber;
    private EditText emailAddress;
    private Button discoverHorizon;

    private String mobileNumber;
    private String displayName;
    private String userEmail;
    private String locationCity;
    private String locationCountry;
    private String locationState;
    private static final int REQUEST_PERMISSIONS = 20;
    public static final String TAG = SignInActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_sign_in);
        horizonLocationProvider = new LocationProvider(this, this);
        signInBanner = findViewById(R.id.bannerVideo);
        bannerLocation = findViewById(R.id.bannerLocation);
        userName = findViewById(R.id.signInUsername);
        phoneNumber = findViewById(R.id.signInPhone);
        emailAddress = findViewById(R.id.signInEmail);
        discoverHorizon = findViewById(R.id.discoverButton);
        permitCheck();
        discoverHorizon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signInValidator();

            }
        });


        this.videoBannerConfig();
        //bannerLocation.setText("New York");
    }


    @Override
    protected void onPause() {
        super.onPause();
        horizonLocationProvider.disconnect();


    }


    @Override
    protected void onResume() {
        super.onResume();
        horizonLocationProvider.connect();


    }


    @Override
    public void onPermissionsGranted(final int requestCode) {
        Toast.makeText(this, "Permissions Received.", Toast.LENGTH_LONG).show();

    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            this.moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void videoBannerConfig() {

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.redbckgrnd);
        signInBanner.setVideoURI(uri);
        signInBanner.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
                signInBanner.start();
            }
        });

    }


    private void permitCheck() {

        SignInActivity.super.requestAppPermissions(new
                        String[]{Manifest.permission.ACCESS_FINE_LOCATION}, R.string
                        .runtime_permissions_txt
                , REQUEST_PERMISSIONS);


    }


    private void signInValidator() {

        displayName = userName.getText().toString();
        userEmail = emailAddress.getText().toString();
        mobileNumber = phoneNumber.getText().toString();


        if (!isValidName(displayName)) {
            userName.setError(getString(R.string.NameError));
        }
        if (!isValidEmail(userEmail)) {
            emailAddress.setError(getString(R.string.EmailError));

        }

        if (isValidName(displayName) && isValidEmail(userEmail)) {

            this.setHorizonPreferences();
            //this.profileLogger();
            startActivity(new Intent(this, OnboardingActivity.class));
            //finish();


        }


    }


    private void setHorizonPreferences() {

        horizonPreferences = getApplicationContext().getSharedPreferences("HORIZON_PREFERENCES", 0);
        SharedPreferences.Editor horizonAR_IO = horizonPreferences.edit();
        horizonAR_IO.putString("displayName", displayName);
        horizonAR_IO.putString("mobileNumber", mobileNumber);
        horizonAR_IO.putString("email", userEmail);



        horizonAR_IO.apply();


    }


    private void profileLogger() {

    }


    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    private boolean isValidName(String name) {
        if (name != null && !name.isEmpty()) {
            return true;
        }
        return false;
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
            locationCity = addresses.get(0).getLocality();
            locationState = addresses.get(0).getAdminArea();
            locationCountry = addresses.get(0).getCountryName();
            if(locationCity != null){
                bannerLocation.setText(locationCity);
                horizonPreferences = getApplicationContext().getSharedPreferences("HORIZON_PREFERENCES", 0);
                SharedPreferences.Editor horizonAR_IO = horizonPreferences.edit();
                horizonAR_IO.putString("horizonLocation", locationCity);
                horizonAR_IO.putString("horizonCountry", locationCountry);
                horizonAR_IO.putString("horizonState", locationState);
                horizonAR_IO.apply();
            }else{
                bannerLocation.setText(locationState);
                horizonPreferences = getApplicationContext().getSharedPreferences("HORIZON_PREFERENCES", 0);
                SharedPreferences.Editor horizonAR_IO = horizonPreferences.edit();
                horizonAR_IO.putString("horizonLocation", locationState);
                horizonAR_IO.putString("horizonCountry", locationCountry);
                horizonAR_IO.apply();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }



    }






}
