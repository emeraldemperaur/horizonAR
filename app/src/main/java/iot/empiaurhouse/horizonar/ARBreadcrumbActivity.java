package iot.empiaurhouse.horizonar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;

public class ARBreadcrumbActivity extends AppCompatActivity {


    private CrumbsTabAdapter crumbsTabAdapter;
    private TabLayout crumbsTabLayout;
    private ViewPager crumbsViewPager;
    private ImageView closeCrumbs;
    private TypeWriterTextView crumbsTypeWriterTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arbreadcrumb);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        statusBarColor(R.color.colorAccent);
        closeCrumbs = findViewById(R.id.closeBreadcrumbs);
        crumbsTypeWriterTextView = findViewById(R.id.myBreadcrumbs);
        crumbsViewPager = findViewById(R.id.BreadcrumbsViewPager);
        crumbsTabLayout = findViewById(R.id.breadcrumbsTab);
        typeWriterText(getString(R.string.menu_breadcrumbs),190,crumbsTypeWriterTextView);
        this.breadcrumbsUIEventHandler();
        this.breadcrumbsTabConfig();


    }


    private void breadcrumbsTabConfig(){

        crumbsTabAdapter = new CrumbsTabAdapter(getSupportFragmentManager());
        crumbsTabAdapter.addFragment(new SeeBreadcrumbsFragment(),getString(R.string.see_text));
        crumbsTabAdapter.addFragment(new ListenBreadcrumbsFragment(),getString(R.string.listen_text));
        crumbsViewPager.setAdapter(crumbsTabAdapter);
        crumbsTabLayout.setupWithViewPager(crumbsViewPager);


    }



    private void breadcrumbsUIEventHandler(){



        closeCrumbs.setOnClickListener(view -> {

            finish();

        });


    }


    private void typeWriterText(String crumbssText, Integer charDelay, TypeWriterTextView crumbsTextView){
       crumbsTextView.setCharacterDelay(charDelay);
       crumbsTextView.displayTextWithAnimation(crumbssText);

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
    protected void onDestroy() {
        super.onDestroy();


    }









}
