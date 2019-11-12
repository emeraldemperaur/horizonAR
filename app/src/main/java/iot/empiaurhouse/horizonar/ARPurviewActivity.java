package iot.empiaurhouse.horizonar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import static iot.empiaurhouse.horizonar.R.*;

public class ARPurviewActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,LocationProvider.LocationCallback  {

    private DrawerLayout purviewDrawer;
    private NavigationView navigationView;
    private FrameLayout horizonPurview;
    private SharedPreferences horizonPreferences;
    private SharedPreferences.Editor horizonAR_IO;
    private View profileView;
    private TextView purviewLocation;
    private TextView purviewGeoLocation;
    private ImageView purviewMenu;
    private ImageView crumbModeStatus;
    private FloatingActionButton arBreadcrumbs;
    private TypeWriterTextView arPurviewTitleTextView;
    private Animation fadeOut;
    private Animation fadeIn;
    private LocationProvider horizonLocationProvider;
    private ArFragment arFragment;
    private ViewRenderable seeRenderable;
    private ModelRenderable listenRenderable;
    private View viewRenderable;


    private String seeMaxim;
    private String seeContext;
    String listenMaxim;
    private int crumbMode;
    private String Name;
    private String Phone;
    private String Email;
    private String sender;
    private String locationCity;
    private String locationState;
    private String locationCountry;
    private Bitmap profileBitmap;

    public static final String TAG = ARPurviewActivity.class.getSimpleName();






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_arpurview);
        horizonLocationProvider = new LocationProvider(this, this);
        overridePendingTransition(anim.fadein, anim.fadeout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        purviewDrawer = findViewById(id.horizonDrawerLayout);
        navigationView = findViewById(id.nav_view);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(id.horizonux_fragment);
        Objects.requireNonNull(arFragment).getPlaneDiscoveryController().setInstructionView(null);
        navigationView.setNavigationItemSelectedListener(this);
        purviewMenu = findViewById(id.purviewMenu);
        horizonPurview = findViewById(id.horizonPurview);
        purviewLocation = findViewById(id.purviewLocation);
        arBreadcrumbs = findViewById(id.arBreadcrumbsFAB);
        crumbModeStatus = findViewById(id.crumbModeStatus);
        arPurviewTitleTextView = findViewById(id.purviewTitle);
        purviewGeoLocation = findViewById(id.purviewGeoLocation);
        purviewGeoLocation.setVisibility(View.INVISIBLE);
        horizonPreferences = getApplicationContext().getSharedPreferences("HORIZON_PREFERENCES",0);
        profileView = navigationView.getHeaderView(0);

        this.viewFX();
        this.profileRender();
        this.purviewEventHandler();
        this.buttonPrompt();









    }



    private ViewRenderable renderModel(){

        ViewRenderable.builder()
                .setView(this, renderView())
                .build()
                .thenAccept(renderable -> seeRenderable = renderable.makeCopy());

        MaterialFactory.makeOpaqueWithColor(this, new Color(android.graphics.Color.RED))
                .thenAccept(
                        material -> {
                            listenRenderable =
                                    ShapeFactory.makeSphere(0.1f, new Vector3(0.0f, 0.15f, 0.0f), material); });

        return seeRenderable;
    }


  private View renderView(){

      LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      viewRenderable = Objects.requireNonNull(inflater).inflate(layout.seebreadcrumb_cardview, null);
      TextView seeBreadcrumbsTitle = viewRenderable.findViewById(id.seeBreadcrumbsTitle);
      TextView seeBreadcrumbsContext = viewRenderable.findViewById(id.seeBreadcrumbsContext);
      TextView seeBreadcrumbsUser = viewRenderable.findViewById(id.seeBreadcrumbsUser);
      ImageView crumbProfileImage = viewRenderable.findViewById(id.crumbProfileImage);
      seeMaxim = horizonPreferences.getString("seeMaxim",getString(string.untitled_holdertext));
      seeContext = horizonPreferences.getString("seeContext",null);
      if(profileBitmap != null) {
          crumbProfileImage.setImageBitmap(profileBitmap);
      }
      seeBreadcrumbsTitle.setText(seeMaxim);
      seeBreadcrumbsContext.setText(seeContext);
      seeBreadcrumbsUser.setText(Name);



      return viewRenderable;
  }


    private ModelRenderable renderListenModel(){

        MaterialFactory.makeOpaqueWithColor(this, new Color(android.graphics.Color.BLUE))
                .thenAccept(
                        material -> {
                            listenRenderable =
                                    ShapeFactory.makeCube(new Vector3(1f, 1f, 1f), new Vector3(1f, 1f, 1f), material); });

        return listenRenderable;
    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {


            case id.nav_myprofile:
                startActivity(new Intent(this, ProfileActivity.class));

                break;
            case id.nav_arbreadcrumbs:
                startActivity(new Intent(this, ARBreadcrumbActivity.class));

                break;
            case id.nav_horizonstats:
                startActivity(new Intent(this, StatsActivity.class));

                break;
            case id.nav_appinfo:
                startActivity(new Intent(this, InfoActivity.class));

                break;





        }

        purviewDrawer.closeDrawers();
        return true;

    }



    @Override
    protected void onResume() {
        super.onResume();
        navigationView.setCheckedItem(id.menu_none);
        this.profileRender();
        this.receiveEditorData();
        horizonLocationProvider.connect();

    }


    @Override
    protected void onPause() {
        super.onPause();
        arBreadcrumbs.clearAnimation();
        this.profileRender();
        horizonLocationProvider.disconnect();
        viewRenderable = renderView();


    }




    private void profileRender(){

        Name = horizonPreferences.getString("displayName",null);
        Phone = horizonPreferences.getString("mobileNumber",null);
        Email = horizonPreferences.getString("email",null);
        String gps = horizonPreferences.getString("horizonLocation",null);
        String latLong = horizonPreferences.getString("horizonLatLong",null);
        seeMaxim = horizonPreferences.getString("seeMaxim",getString(string.untitled_holdertext));
        seeContext = horizonPreferences.getString("seeContext",null);
        listenMaxim = horizonPreferences.getString("listenMaxim",null);
        crumbMode = horizonPreferences.getInt("crumbMode",0);
        String base64String = horizonPreferences.getString("base64String",null);
        if (base64String != null) {
            profileBitmap = ImageUtil.convert(Objects.requireNonNull(base64String));

            if(profileBitmap != null) {
                ImageView profileImage = profileView.findViewById(id.profileImage);
                profileImage.setImageBitmap(profileBitmap);

        }

        }
        purviewLocation.setText(gps);
        purviewGeoLocation.setText(latLong);

        TextView profileName = profileView.findViewById(id.profileName);
        TextView profileEmail = profileView.findViewById(id.profileEmail);
        TextView profilePhone = profileView.findViewById(id.profilePhone);

        profileName.setText(Name);
        profilePhone.setText(Phone);
        profileEmail.setText(Email);
        if(crumbMode == 0){

            crumbModeStatus.setImageResource(drawable.ic_see_white_24dp);

        }else if (crumbMode == 1){

            crumbModeStatus.setImageResource(drawable.ic_listen_white_24dp);
            listenRenderable = renderListenModel();

        }



    }


    private void buttonPrompt(){

        final Animation floatingAnimation = AnimationUtils.loadAnimation(getApplicationContext(), anim.floating);
        arBreadcrumbs.startAnimation(floatingAnimation);

    }





    private void purviewEventHandler(){
        purviewMenu.setOnClickListener(view -> {

            purviewDrawer.openDrawer(GravityCompat.START);
            profileRender();



        });


        arBreadcrumbs.setOnClickListener(view -> {

            ARBreadcrumbsDialog.display(getSupportFragmentManager());



        });



        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    viewRenderable = renderView();
                    seeRenderable = renderModel();
                    if (seeRenderable == null) {
                        return;
                    }

                    // Create the Anchor.
                    Anchor horizonAR = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(horizonAR);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());


                    TransformableNode horizonARNode = new TransformableNode(arFragment.getTransformationSystem());
                    horizonARNode.setParent(anchorNode);
                    if(crumbMode == 0) {
                        horizonARNode.setRenderable(seeRenderable);
                    }
                    else if(crumbMode == 1){
                        horizonARNode.setRenderable(listenRenderable);

                    }
                    horizonARNode.select();
                });



    }











    private void receiveData()
    {
        //RECEIVE DATA VIA INTENT
        //Intent i = getIntent();
        //crumbMode = i.getIntExtra("MODE_KEY",0);
        horizonPreferences = getApplicationContext().getSharedPreferences("HORIZON_PREFERENCES", 0);
        horizonAR_IO = horizonPreferences.edit();
        crumbMode = horizonPreferences.getInt("crumbMode",0);
        //horizonAR_IO.putInt("crumbMode",crumbMode);


        if(crumbMode == 0){

            //seeMaxim = i.getStringExtra("MAXIM_KEY");
            //seeContext = i.getStringExtra("CONTEXT_KEY");
            horizonAR_IO.putString("seeMaxim", seeMaxim);
            horizonAR_IO.putString("seeContext", seeContext);
            crumbModeStatus.setImageResource(drawable.ic_see_white_24dp);
            horizonAR_IO.apply();



        }else if (crumbMode == 1){

           // listenMaxim = i.getStringExtra("MAXIM_KEY");
            horizonAR_IO.putString("listenMaxim", listenMaxim);
            crumbModeStatus.setImageResource(drawable.ic_listen_white_24dp);
            horizonAR_IO.apply();


        }

    }



    private void receiveEditorData(){

        try {
            sender = Objects.requireNonNull(this.getIntent().getExtras()).getString("SENDER_KEY");
        }catch(Exception e) {
            e.printStackTrace();
        }
        if(sender != null)
        {
            this.receiveData();

        }

    }




    private void typeWriterText(String arPurviewTitle, Integer charDelay, TypeWriterTextView arPurviewTitleTextView){
        arPurviewTitleTextView.setCharacterDelay(charDelay);
        arPurviewTitleTextView.displayTextWithAnimation(arPurviewTitle);

    }



    private void viewFX(){

        typeWriterText(getString(string.menu_purview),190,arPurviewTitleTextView);
        final Handler handler = new Handler();
        handler.postDelayed(() -> {

            fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), anim.fadein);
            fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), anim.fadeout);
            arPurviewTitleTextView.startAnimation(fadeOut);
            arPurviewTitleTextView.setVisibility(View.GONE);
            purviewGeoLocation.startAnimation(fadeIn);
            purviewGeoLocation.setVisibility(View.VISIBLE);

        }, 3600);




    }



    public void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        String Latitude = Double.toString(currentLatitude);
        String Longitude = Double.toString(currentLongitude);
        String LatLong = Latitude + "°" + " | " + Longitude + "°";




        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(currentLatitude, currentLongitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            locationCity = addresses.get(0).getLocality();
            locationState = addresses.get(0).getAdminArea();
            locationCountry = addresses.get(0).getCountryName();
            if(locationCity != null){
                purviewLocation.setText(locationCity);
                purviewGeoLocation.setText(LatLong);
                horizonPreferences = getApplicationContext().getSharedPreferences("HORIZON_PREFERENCES", 0);
                horizonAR_IO = horizonPreferences.edit();
                horizonAR_IO.putString("horizonLocation", locationCity);
                horizonAR_IO.putString("horizonCountry", locationCountry);
                horizonAR_IO.putString("horizonState", locationState);
                horizonAR_IO.putString("horizonLatLong", LatLong);
                horizonAR_IO.apply();
            }else{
                purviewLocation.setText(locationState);
                purviewGeoLocation.setText(LatLong);
                horizonPreferences = getApplicationContext().getSharedPreferences("HORIZON_PREFERENCES", 0);
                SharedPreferences.Editor horizonAR_IO = horizonPreferences.edit();
                horizonAR_IO.putString("horizonLocation", locationState);
                horizonAR_IO.putString("horizonCountry", locationCountry);
                horizonAR_IO.putString("horizonLatLong", LatLong);
                horizonAR_IO.apply();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }



    }








}
