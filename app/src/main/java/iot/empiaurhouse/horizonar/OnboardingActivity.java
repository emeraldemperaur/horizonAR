package iot.empiaurhouse.horizonar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

import static iot.empiaurhouse.horizonar.R.*;

public class OnboardingActivity extends AppIntro {

    SharedPreferences horizonPreferences;
    String bgColorWhite = "#FFFFFF";
    String bgColorAccent = "#A90003";
    String bgColorBlack = "#000000";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(anim.fadein, anim.fadeout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        horizonPreferences = getApplicationContext().getSharedPreferences("HORIZON_PREFERENCES",0);
        String horizonLocation = horizonPreferences.getString("horizonLocation","town");
        String displayName = horizonPreferences.getString("displayName",null);
        String exploreDesc = getString(string.explore_desc, horizonLocation);
        String eventHorizonDesc = getString(string.eventhorizon_desc,displayName);

        SliderPage horizonOverview = new SliderPage();
        horizonOverview.setTitle(getString(string.discover_title));
        horizonOverview.setDescription(getString(string.discover_desc));
        horizonOverview.setTitleColor(Color.parseColor(bgColorAccent));
        horizonOverview.setDescColor(Color.parseColor(bgColorAccent));
        horizonOverview.setImageDrawable(drawable.discoveraricon);
        horizonOverview.setBgColor(Color.parseColor(bgColorWhite));
        horizonOverview.setTitleTypefaceFontRes(font.montserratregular);
        horizonOverview.setDescTypefaceFontRes(font.montserratsemibold);
        addSlide(AppIntroFragment.newInstance(horizonOverview));



        SliderPage Explore = new SliderPage();
        Explore.setTitle(getString(string.explore_title));
        Explore.setTitleColor(Color.parseColor(bgColorAccent));
        Explore.setDescColor(Color.parseColor(bgColorAccent));
        Explore.setDescription(exploreDesc);
        Explore.setImageDrawable(drawable.globepng);
        Explore.setBgColor(Color.parseColor(bgColorWhite));
        Explore.setTitleTypefaceFontRes(font.montserratregular);
        Explore.setDescTypefaceFontRes(font.montserratsemibold);
        addSlide(AppIntroFragment.newInstance(Explore));


        SliderPage See = new SliderPage();
        See.setTitle(getString(string.see_title));
        See.setTitleColor(Color.parseColor(bgColorAccent));
        See.setDescColor(Color.parseColor(bgColorAccent));
        See.setDescription(getString(string.see_desc));
        See.setImageDrawable(drawable.seearicon);
        See.setBgColor(Color.parseColor(bgColorWhite));
        See.setTitleTypefaceFontRes(font.montserratregular);
        See.setDescTypefaceFontRes(font.montserratsemibold);
        addSlide(AppIntroFragment.newInstance(See));


        SliderPage Hear = new SliderPage();
        Hear.setTitle(getString(string.hear_title));
        Hear.setTitleColor(Color.parseColor(bgColorAccent));
        Hear.setDescColor(Color.parseColor(bgColorAccent));
        Hear.setDescription(getString(string.hear_desc));
        Hear.setImageDrawable(drawable.soundaricon);
        Hear.setBgColor(Color.parseColor(bgColorWhite));
        Hear.setTitleTypefaceFontRes(font.montserratregular);
        Hear.setDescTypefaceFontRes(font.montserratsemibold);
        addSlide(AppIntroFragment.newInstance(Hear));


        SliderPage eventHorizon = new SliderPage();
        eventHorizon.setTitle(getString(string.eventhorizon_title));
        eventHorizon.setTitleColor(Color.parseColor(bgColorAccent));
        eventHorizon.setDescColor(Color.parseColor(bgColorAccent));
        eventHorizon.setDescription(eventHorizonDesc);
        eventHorizon.setImageDrawable(drawable.eharicon);
        eventHorizon.setBgColor(Color.parseColor(bgColorWhite));
        eventHorizon.setTitleTypefaceFontRes(font.montserratregular);
        eventHorizon.setDescTypefaceFontRes(font.montserratsemibold);
        addSlide(AppIntroFragment.newInstance(eventHorizon));

        setVibrate(true);
        setSeparatorColor(Color.parseColor(bgColorBlack));
        showStatusBar(false);
        setVibrateIntensity(30);
        setFlowAnimation();
        setIndicatorColor(Color.parseColor(bgColorAccent),Color.parseColor(bgColorBlack));
        setColorSkipButton(Color.parseColor(bgColorAccent));
        setColorDoneText(Color.parseColor(bgColorAccent));
        setNextArrowColor(Color.parseColor(bgColorAccent));

    }







    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        startActivity(new Intent(this, ARPurviewActivity.class));
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(this, ARPurviewActivity.class));
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




}
