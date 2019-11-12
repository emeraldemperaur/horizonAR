package iot.empiaurhouse.horizonar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {


    private int BuildOS = Build.VERSION.SDK_INT;
    private String OEM = Build.MANUFACTURER;
    private String Model = Build.DISPLAY;
    private String Device = Build.DEVICE;
    private String deviceOS = "API Level: " + BuildOS;
    private String deviceName = "Model: " + Device;
    private String deviceBuild = "Build: " + Model;



    private TextView deviceOEM;
    private TextView deviceModel;
    private TextView deviceID;
    private TextView deviceBuildOS;
    private TextView deviceResolution;
    private TextView deviceDensity;


    private CardView BuildInfo;
    private CardView DeveloperInfo;
    private CardView DeviceInfo;
    private ImageView closeInfo;
    private TypeWriterTextView infoTypeWriterTextView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        infoTypeWriterTextView = findViewById(R.id.horizonInfo);
        typeWriterText(getString(R.string.about_horizon),190,infoTypeWriterTextView);
        statusBarColor(R.color.colorAccent);
        deviceOEM = findViewById(R.id.deviceOEM);
        deviceModel = findViewById(R.id.deviceModel);
        deviceID = findViewById(R.id.deviceID);
        deviceBuildOS = findViewById(R.id.deviceBuildOS);
        deviceResolution = findViewById(R.id.deviceResolution);
        deviceDensity = findViewById(R.id.deviceDPI);
        closeInfo = findViewById(R.id.closeInfo);
        BuildInfo = findViewById(R.id.cardViewBuildInfo);
        DeveloperInfo = findViewById(R.id.cardViewDeveloperInfo);
        DeviceInfo = findViewById(R.id.cardViewDeviceInfo);

        this.viewDirector();
        this.buildInfoSet();
        this.infoUIEventHandler();


    }


    private void buildInfoSet(){

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int heightPixels = metrics.heightPixels;
        int widthPixels = metrics.widthPixels;
        int densityDpi = metrics.densityDpi;
        String deviceResosultion = "Resolution: " + widthPixels + " x " + heightPixels;
        String deviceDPI = "Screen Density: " + densityDpi + " dpi";

        deviceOEM.setText(OEM);
        deviceModel.setText(deviceBuild);
        deviceID.setText(deviceName);
        deviceBuildOS.setText(deviceOS);
        deviceResolution.setText(deviceResosultion);
        deviceDensity.setText(deviceDPI);



    }



    private void viewDirector(){


        final Handler handler = new Handler();
        handler.postDelayed(() -> {

            final Animation loadAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_up_in);
            BuildInfo.startAnimation(loadAnimation);
            DeveloperInfo.startAnimation(loadAnimation);
            DeviceInfo.startAnimation(loadAnimation);
            BuildInfo.setVisibility(View.VISIBLE);
            DeveloperInfo.setVisibility(View.VISIBLE);
            DeviceInfo.setVisibility(View.VISIBLE);
        }, 100);





    }


    private void infoUIEventHandler(){


        DeveloperInfo.setOnClickListener(view -> {

            this.developerPortfolioIntent();
        });

        closeInfo.setOnClickListener(view -> {

            finish();

        });


    }



    private void developerPortfolioIntent(){

        String url = "https://www.emekaegwim.com";
        Intent eh = new Intent(Intent.ACTION_VIEW);
        eh.setData(Uri.parse(url));
        startActivity(eh);

    }



    private void typeWriterText(String horizonInfoText, Integer charDelay, TypeWriterTextView horizonInfoTextView){
        horizonInfoTextView.setCharacterDelay(charDelay);
        horizonInfoTextView.displayTextWithAnimation(horizonInfoText);

    }


    private void statusBarColor(int colorInt){


        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this,colorInt));

    }


    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onPause() {
        super.onPause();

        BuildInfo.clearAnimation();
        DeveloperInfo.clearAnimation();
        DeviceInfo.clearAnimation();


    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        BuildInfo.clearAnimation();
        DeveloperInfo.clearAnimation();
        DeviceInfo.clearAnimation();



    }



}
