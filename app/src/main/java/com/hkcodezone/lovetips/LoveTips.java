package com.lovetips.krish.lovetips;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;


public class LoveTips extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener, RewardedVideoAdListener {

    //private ImageButton backButton;
    private TextView navBarUsername;
    private SharedPreferences session, settings, remember;
    private DrawerLayout drawer;
    private ImageView navHeaderlogo;
    private RewardedVideoAd mRewardedVideoAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(LoveTips.this).inflate(R.layout.activity_love_tips,null);

        settings = getSharedPreferences("SETTINGS",MODE_PRIVATE);
        int color  = settings.getInt("APP_COLOR",Color.parseColor("#E82433"));

        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        NavigationView navigationView = (NavigationView)view.findViewById(R.id.nav_view);
        toolbar.setBackgroundColor(color);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }
        navigationView.getHeaderView(0).setBackgroundColor(color);

        navHeaderlogo = (ImageView)navigationView.getHeaderView(0).findViewById(R.id.imageView);
        String url = settings.getString("APP_MAIN_LOGO","");
        if(!url.equals("")){
            Glide.with(LoveTips.this).load(url).into(navHeaderlogo);
        }

        setContentView(view);

        //backButton = (ImageButton)findViewById(R.id.topBarLeftIconId);
        //backButton.setOnClickListener(this);

        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        fragmentHandler(new homeFragment(),"");
        session = getSharedPreferences("CUST_ID",MODE_PRIVATE);
        navBarUsername = (TextView)navigationView.getHeaderView(0).findViewById(R.id.nhlpUsernameId);
        updateNavHeaderUsername();

        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
                updateNavHeaderUsername();
            }
        });

        remember = getSharedPreferences("REMEMBER",MODE_PRIVATE);

        RelativeLayout adContainer = (RelativeLayout) findViewById(R.id.adView);
        if(settings.getString("ADMOB_SWITCH","").equals("ON")){
            MobileAds.initialize(LoveTips.this, "ca-app-pub-3940256099942544~3347511713");

            AdView mAdView = new AdView(LoveTips.this);
            mAdView.setAdSize(AdSize.SMART_BANNER);
            mAdView.setAdUnitId(settings.getString("BANER_ADD_LINK",""));
            adContainer.addView(mAdView);

            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

            //MobileAds.initialize(LoveTips.this, "ca-app-pub-3940256099942544~3347511713");
            //adView = (AdView)findViewById(R.id.adView);
            //adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
            //adRequest = new AdRequest.Builder().build();
            //if(adView.getAdSize() != null || adView.getAdUnitId() != null){
            //    adView.loadAd(adRequest);
            //}

            mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(LoveTips.this);
            mRewardedVideoAd.setRewardedVideoAdListener(this);

            loadRewardedVideoAd();
        }else{
            adContainer.setVisibility(View.INVISIBLE);
        }
    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd(settings.getString("REWAD_VIDEO_LINK",""),
                new AdRequest.Builder().build());
    }

    private void fragmentHandler(Fragment fragment, String TAG) {
        if(TAG.length() > 0){
            if(TAG == "HOME_TAG"){
                getSupportFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction().replace(R.id.cltFragmetnFrameId,fragment,TAG).commit();
            }
            else{
                getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.cltFragmetnFrameId,fragment,TAG).commit();
            }
        }
        else{
            getSupportFragmentManager().beginTransaction().replace(R.id.cltFragmetnFrameId,fragment,"HOME_TAG").commit();
        }
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            if(settings.getString("ADMOB_SWITCH","").equals("ON")) {
                loadRewardedVideoAd();
            }
            //backButton.setVisibility(View.INVISIBLE);
            fragmentHandler(new homeFragment(),"HOME_TAG");
            updateNavHeaderUsername();
        } else if (id == R.id.nav_tips) {
            if(settings.getString("ADMOB_SWITCH","").equals("ON")) {
                loadRewardedVideoAd();
            }
            //backButton.setVisibility(View.VISIBLE);
            fragmentHandler(new TIpsFragment(),"TIPS_TAG");
            updateNavHeaderUsername();
        }  else if (id == R.id.nav_privacy) {
            if(settings.getString("ADMOB_SWITCH","").equals("ON")) {
                loadRewardedVideoAd();
            }
            //backButton.setVisibility(View.VISIBLE);
            fragmentHandler(new PolicyFragment(),"PRIVACY_TAG");
            updateNavHeaderUsername();
        }else if (id == R.id.nav_profile) {
            if(settings.getString("ADMOB_SWITCH","").equals("ON")) {
                loadRewardedVideoAd();
            }
            //backButton.setVisibility(View.VISIBLE);
            fragmentHandler(new ProfileFragment(),"PROFILE_TAG");
            updateNavHeaderUsername();
        }else if(id == R.id.nav_moreApps){
            if(settings.getString("ADMOB_SWITCH","").equals("ON")) {
                loadRewardedVideoAd();
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(settings.getString("MORE_APP_LINK","")));
            intent.setPackage("com.android.vending");
            startActivity(intent);
            updateNavHeaderUsername();
        }else if(id == R.id.nac_logout){
            session.edit().clear().commit();
            startActivity(new Intent(LoveTips.this,Home.class));
            finish();
        }else if(id == R.id.nav_rate){
            if(settings.getString("ADMOB_SWITCH","").equals("ON")) {
                loadRewardedVideoAd();
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(settings.getString("RATE_APP_LINK","")));
            intent.setPackage("com.android.vending");
            startActivity(intent);
        }else if(id == R.id.nav_share){
            if(settings.getString("ADMOB_SWITCH","").equals("ON")) {
                loadRewardedVideoAd();
            }
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT,"I suggest this app for you : https://play.google.com/store/apps/details?id=com.android.chrome");
            intent.setType("text/plain");
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        /*switch (v.getId()) {
            case R.id.topBarLeftIconId:
                getSupportFragmentManager().popBackStackImmediate();
                if(getSupportFragmentManager().findFragmentByTag("HOME_TAG") != null){
                    if(getSupportFragmentManager().findFragmentByTag("HOME_TAG").isVisible()){
                        backButton.setVisibility(View.INVISIBLE);
                    }
                }
                else{
                    backButton.setVisibility(View.VISIBLE);
                }

        }*/
    }

    public void updateNavHeaderUsername(){
        navBarUsername.setText(session.getString("FullName",""));
    }

    @Override
    public void onRewarded(RewardItem reward) {
        //Toast.makeText(this, "onRewarded! currency: " + reward.getType() + "  amount: " +
          //      reward.getAmount(), Toast.LENGTH_SHORT).show();
        // Reward the user.
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        //Toast.makeText(this, "onRewardedVideoAdLeftApplication",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        //Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        //Toast.makeText(this, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        //Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
        mRewardedVideoAd.show();
    }

    @Override
    public void onRewardedVideoAdOpened() {
        //Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoStarted() {
        //Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoCompleted() {

        //mRewardedVideoAd.destroy(LoveTips.this);
        //Toast.makeText(this, "onRewardedVideoCompleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        //mRewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        //mRewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        //mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
