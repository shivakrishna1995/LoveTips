package com.example.krish.lovetips;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;

public class LoveTips extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {

    //private ImageButton backButton;
    private TextView navBarUsername;
    private SharedPreferences session, settings;
    private DrawerLayout drawer;
    private ImageView navHeaderlogo;

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
            //backButton.setVisibility(View.INVISIBLE);
            fragmentHandler(new homeFragment(),"HOME_TAG");
            updateNavHeaderUsername();
        } else if (id == R.id.nav_tips) {
            //backButton.setVisibility(View.VISIBLE);
            fragmentHandler(new TIpsFragment(),"TIPS_TAG");
            updateNavHeaderUsername();
        }  else if (id == R.id.nav_privacy) {
            //backButton.setVisibility(View.VISIBLE);
            fragmentHandler(new PolicyFragment(),"PRIVACY_TAG");
            updateNavHeaderUsername();
        }else if (id == R.id.nav_profile) {
            //backButton.setVisibility(View.VISIBLE);
            fragmentHandler(new ProfileFragment(),"PROFILE_TAG");
            updateNavHeaderUsername();
        }else if(id == R.id.nav_moreApps){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(settings.getString("MORE_APP_LINK","")));
            intent.setPackage("com.android.vending");
            startActivity(intent);
            updateNavHeaderUsername();
        }else if(id == R.id.nac_logout){
            session.edit().clear();
            startActivity(new Intent(LoveTips.this,Home.class));
            finish();
        }else if(id == R.id.nav_rate){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(settings.getString("RATE_APP_LINK","")));
            intent.setPackage("com.android.vending");
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

}
