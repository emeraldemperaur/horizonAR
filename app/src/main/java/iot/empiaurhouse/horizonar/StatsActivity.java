package iot.empiaurhouse.horizonar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

public class StatsActivity extends AppCompatActivity {


    SharedPreferences horizonPreferences;
    private ImageView closeStats;
    private ImageView profileImg;
    private TypeWriterTextView statsTypeWriterTextView;
    private TextView statsProfileName;
    private TextView statsProfileRank;
    private TextView statsProfileLastAdventure;
    private TextView statsPreviousVantagePoint;
    private TextView statsRubiconCrossDate;
    private CardView statsProfile;
    private CardView statsCrumbsCount;
    private CardView statsCitiesExplored;
    private CardView statsCountriesVisited;
    private CardView statsFavouriteLocations;
    private String base64String;
    private TextView errorMsg;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        statusBarColor(R.color.colorAccent);
        closeStats = findViewById(R.id.closeStats);
        statsTypeWriterTextView = findViewById(R.id.myStats);
        typeWriterText(getString(R.string.menu_horizonStats),190,statsTypeWriterTextView);
        horizonPreferences = getApplicationContext().getSharedPreferences("HORIZON_PREFERENCES",0);
        statsProfileName = findViewById(R.id.statsProfileName);
        statsProfileRank = findViewById(R.id.statsProfileRank);
        profileImg = findViewById(R.id.statsProfileImage);
        statsProfileLastAdventure = findViewById(R.id.statsProfileLastAdventure);
        statsProfile = findViewById(R.id.cardviewStatsUserProfile);
        statsCrumbsCount = findViewById(R.id.cardCrumbsCount);
        statsCitiesExplored = findViewById(R.id.cardViewCitiesExplored);
        statsCountriesVisited = findViewById(R.id.cardViewCountriesVisited);
        statsFavouriteLocations = findViewById(R.id.cardViewFavouriteLocations);
        errorMsg = findViewById(R.id.noDataError);
        statsRubiconCrossDate = findViewById(R.id.statsRubiconCrossDate);
        statsPreviousVantagePoint = findViewById(R.id.statsPreviousVantagePoint);

        this.statsUIEventHandler();
        this.statsProfileRender();
        this.viewLoadDirector();


    }




    private void statsProfileRender(){

        statsProfileName.setText(horizonPreferences.getString("displayName",null));
        base64String = horizonPreferences.getString("base64String",null);
        if (base64String != null) {
            Bitmap profileBitmap = ImageUtil.convert(Objects.requireNonNull(base64String));

            if(profileBitmap != null) {
                profileImg.setImageBitmap(profileBitmap);

            }

        }
        //include CIRCE web service info fetch method here


    }



    private void viewLoadDirector(){


        final Handler handler = new Handler();
        handler.postDelayed(() -> {

            final Animation fadeInAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);

            statsProfile.startAnimation(fadeInAnimation);
            statsProfileLastAdventure.startAnimation(fadeInAnimation);
            statsProfile.setVisibility(View.VISIBLE);
            statsProfileLastAdventure.setVisibility(View.VISIBLE);


        }, 500);

        final Handler slideUpHandler = new Handler();
        slideUpHandler.postDelayed(() -> {

            final Animation loadAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_up_in);

            //statsCrumbsCount.startAnimation(loadAnimation);
            //statsCitiesExplored.startAnimation(loadAnimation);
            //statsCountriesVisited.startAnimation(loadAnimation);
            //statsFavouriteLocations.startAnimation(loadAnimation);
            statsCrumbsCount.setVisibility(View.GONE);
            statsCitiesExplored.setVisibility(View.GONE);
            statsCountriesVisited.setVisibility(View.GONE);
            statsFavouriteLocations.setVisibility(View.GONE);
            errorMsg.startAnimation(loadAnimation);
            errorMsg.setVisibility(View.VISIBLE);

        }, 1000);





    }




    private void statsUIEventHandler(){


        closeStats.setOnClickListener(view -> {

            finish();

        });


    }


    private void typeWriterText(String statsText, Integer charDelay, TypeWriterTextView statsTextView){
        statsTextView.setCharacterDelay(charDelay);
        statsTextView.displayTextWithAnimation(statsText);

    }


    private void statusBarColor(int colorInt){


        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this,colorInt));

    }




}
