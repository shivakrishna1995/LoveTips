package com.example.krish.lovetips;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Console;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    Timer timer = new Timer();
    private JsonObjectRequest stringRequest;
    private RequestQueue queue;
    private String url = "https://hkcodezone.com/love_tips/Api/Settings";
    private SharedPreferences settings;
    private ConstraintLayout mainLayout;
    private TextView appName;
    private int color;
    private View view;
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main,null);

        getSettingData();
    }

    private void getSettingData(){
        mainLayout = (ConstraintLayout)view.findViewById(R.id.activityMainId);
        queue = Volley.newRequestQueue(MainActivity.this);
        settings = getSharedPreferences("SETTINGS",MODE_PRIVATE);
        appName = (TextView)view.findViewById(R.id.textAppNameId);
        logo = (ImageView)view.findViewById(R.id.imageView);

        stringRequest = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    System.out.println("-------------success"+response);
                    if(response.getInt("success") == 1){

                        final JSONArray array = response.getJSONArray("data");
                        if(array.getJSONObject(0).getString("Key").equals("APP_SWITCH") && array.getJSONObject(0).getString("Value").equals("1")){
                            if(array.getJSONObject(7).getString("Value").equals("")){
                                color = Color.parseColor("#E82433");
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    getWindow().setStatusBarColor(color);
                                }
                            }else{
                                color = Color.parseColor(array.getJSONObject(7).getString("Value"));
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    getWindow().setStatusBarColor(color);
                                }
                            }
                            appName.setText(array.getJSONObject(1).getString("Value"));
                            mainLayout.setBackgroundColor(color);
                            Glide.with(MainActivity.this).load(array.getJSONObject(2).getString("Value")).into(logo);
                            setContentView(view);
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try{
                                                settings.edit().putString("APP_NAME",array.getJSONObject(1).getString("Value"))
                                                        .putString("APP_FLASH_LOGO",array.getJSONObject(2).getString("Value"))
                                                        .putString("APP_RLF_LOGO",array.getJSONObject(3).getString("Value"))
                                                        .putString("APP_MAIN_LOGO",array.getJSONObject(4).getString("Value"))
                                                        .putString("PROFILE_BACKGROUND_IMAGE",array.getJSONObject(5).getString("Value"))
                                                        .putString("PROFILE_BACKGROUND_LOGO",array.getJSONObject(6).getString("Value"))
                                                        .putInt("APP_COLOR",color)
                                                        .putString("MORE_APP_LINK",array.getJSONObject(8).getString("Value"))
                                                        .putString("PRIVACY_POLICY_LINK",array.getJSONObject(9).getString("Value"))
                                                        .putString("RATE_APP_LINK",array.getJSONObject(10).getString("Value"))
                                                        .putString("ADMOB_SWITCH",array.getJSONObject(12).getString("Value"))
                                                        .putString("BANER_ADD_LINK",array.getJSONObject(12).getString("Value"))
                                                        .putString("REWAD_VIDEO_LINK",array.getJSONObject(13).getString("Value"))
                                                        .commit();
                                                startActivity(new Intent(MainActivity.this,Home.class));
                                                finish();
                                            }catch (Exception e){
                                                Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }
                            },4000);
                        }
                        else{
                            setContentView(view);

                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("Maintenance");
                            builder.setMessage("Server under maintenance!");
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            finish();
                                        }
                                    });
                                }
                            },4000);
                        }
                    }
                }catch(Exception e){
                    System.out.println("---------"+e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("---------"+error);

            }
        });
        queue.add(stringRequest);
    }
}
