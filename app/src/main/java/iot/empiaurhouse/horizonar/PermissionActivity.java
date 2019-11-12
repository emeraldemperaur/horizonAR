package iot.empiaurhouse.horizonar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Objects;

public class PermissionActivity extends AppCompatActivity {

    private Button locationServices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_permission);

        locationServices = findViewById(R.id.enable_location_button);
        locationServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });

    }





    @Override
    protected void onPause()
    {
        super.onPause();


    }


    @Override
    protected void onResume() {
        super.onResume();
        this.locationServiceCheck();


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



    private void locationServiceCheck(){

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if((Objects.requireNonNull(locationManager)).isProviderEnabled(LocationManager.GPS_PROVIDER) && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //Google Location Services enabled

            startActivity(new Intent(this, LoaderActivity.class));


        }
        else{

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
