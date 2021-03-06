package com.lovetips.krish.lovetips;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Home extends Activity implements View.OnClickListener {

    private TextView registration, forgetPassword, SignInId;
    private EditText username, password;
    private RequestQueue queue;
    private StringRequest loginRequest;
    private String loginUrl = "http://hkcodezone.com/love_tips/Api/SignIn";
    private String android_id = "";
    private SharedPreferences session;

    int main = R.layout.activity_home;
    private SharedPreferences settings, remember;

    private ImageView logo;

    private CheckBox rememberCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = LayoutInflater.from(Home.this).inflate(main,null);

        rememberCheckBox = (CheckBox)view.findViewById(R.id.checkBox);
        settings = getSharedPreferences("SETTINGS",MODE_PRIVATE);
        int color  = settings.getInt("APP_COLOR",Color.parseColor("#E82433"));
        SignInId = (TextView)view.findViewById(R.id.ahSignInId);
        SignInId.setBackgroundColor(color);

        logo = (ImageView)view.findViewById(R.id.ahlogoIcon);
        String img = settings.getString("APP_RLF_LOGO","");
        if(!img.equals("")){
            Glide.with(Home.this).load(img).into(logo);
        }

        setContentView(view);

        registration = (TextView)findViewById(R.id.ahRegistrationId);
        forgetPassword = (TextView)findViewById(R.id.ahForgetPasswordId);
        registration.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
        SignInId.setOnClickListener(this);
        username = (EditText)findViewById(R.id.ahUsernameId);
        password = (EditText)findViewById(R.id.ahPasswordId);
        queue = Volley.newRequestQueue(Home.this);
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        session = getSharedPreferences("CUST_ID",MODE_PRIVATE);
        remember = getSharedPreferences("REMEMBER",MODE_PRIVATE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                rememberCheckBox.setOutlineSpotShadowColor(Color.GRAY);
            }
            rememberCheckBox.setButtonTintList(getColorStateList(R.color.mainColor));
        }

        if(remember.getString("Email","").length() > 0){
            rememberCheckBox.setChecked(true);
            username.setText(remember.getString("Email",""));
            password.setText(remember.getString("Password",""));
        }

        if(!session.getString("Email","").equals("")){
            startActivity(new Intent(Home.this, LoveTips.class));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ahRegistrationId:
                startActivity(new Intent(Home.this,Registration.class));
                finish();
                break;
            case R.id.ahForgetPasswordId:
                startActivity(new Intent(Home.this,ForgetActivity.class));
                finish();
                break;
            case R.id.ahSignInId:
                    if(username.getText().toString().length() > 0 && password.getText().toString().length() > 0){
                        loginRequest = new StringRequest(Request.Method.POST, loginUrl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject data = new JSONObject(response);
                                    if(data.getInt("success") == 1){
                                        if(rememberCheckBox.isChecked()){
                                            remember.edit().putString("Email", username.getText().toString())
                                            .putString("Password",password.getText().toString()).commit();
                                        }else{
                                            remember.edit().clear().commit();
                                        }
                                        session.edit().putString("Id",data.getJSONObject("data").getString("Id"))
                                                .putString("FullName",data.getJSONObject("data").getString("FullName"))
                                                .putString("MobileNo",data.getJSONObject("data").getString("MobileNo"))
                                                .putString("Email",username.getText().toString())
                                                .putString("Password",password.getText().toString()).commit();
                                        Toast.makeText(Home.this,"User login successfully",Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(Home.this,LoveTips.class));
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(Home.this,"Invalid credentials.",Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Home.this, "Error",Toast.LENGTH_LONG).show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> params = new HashMap<String, String>();
                                params.put("Email",username.getText().toString());
                                params.put("Password", password.getText().toString());
                                params.put("DeviceType", "Android");
                                params.put("DeviceToken", android_id);
                                return params;
                            }
                        };
                        queue.add(loginRequest);
                    }
                    else{
                        Toast.makeText(Home.this, "Fields must not be Empty.",Toast.LENGTH_LONG).show();
                    }
                break;
        }
    }
}
