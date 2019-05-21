package com.example.krish.lovetips;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class LoveTips extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {

    //private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_tips);

        //backButton = (ImageButton)findViewById(R.id.topBarLeftIconId);
        //backButton.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentHandler(new homeFragment(),"");
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
        } else if (id == R.id.nav_tips) {
            //backButton.setVisibility(View.VISIBLE);
            fragmentHandler(new TIpsFragment(),"TIPS_TAG");
        }  else if (id == R.id.nav_privacy) {
            //backButton.setVisibility(View.VISIBLE);
            fragmentHandler(new PolicyFragment(),"PRIVACY_TAG");
        }else if (id == R.id.nav_profile) {
            //backButton.setVisibility(View.VISIBLE);
            fragmentHandler(new ProfileFragment(),"PROFILE_TAG");
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
}