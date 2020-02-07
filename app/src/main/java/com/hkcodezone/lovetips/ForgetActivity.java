package com.lovetips.krish.lovetips;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgetActivity extends Activity implements View.OnClickListener {

    private TextView backToLogin;
    private EditText email;
    private Button submit;
    private StringRequest stringRequest;
    private RequestQueue queue;
    private String Url = "http://hkcodezone.com/love_tips/Api/ForgotPassword";

    private SharedPreferences settings;
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = LayoutInflater.from(ForgetActivity.this).inflate(R.layout.activity_forget,null);

        settings = getSharedPreferences("SETTINGS",MODE_PRIVATE);
        int color  = settings.getInt("APP_COLOR",Color.parseColor("#E82433"));
        submit = (Button)view.findViewById(R.id.afResetPasswordId);
        submit.setBackgroundColor(color);

        logo = (ImageView)view.findViewById(R.id.imageView3);
        String img = settings.getString("APP_RLF_LOGO","");
        if(!img.equals("")){
            Glide.with(ForgetActivity.this).load(img).into(logo);
        }

        setContentView(view);

        backToLogin = (TextView)findViewById(R.id.afBackToSignInId);
        backToLogin.setOnClickListener(this);
        email = (EditText) findViewById(R.id.arFullNameId);
        submit.setOnClickListener(this);
        queue = Volley.newRequestQueue(ForgetActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.afBackToSignInId:
                startActivity(new Intent(ForgetActivity.this, Home.class));
                finish();
                break;
            case R.id.afResetPasswordId:

                stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject data = new JSONObject(response);
                            if(data.getInt("success") == 0)
                            {
                                Toast.makeText(getApplicationContext(),data.getString("message"),Toast.LENGTH_LONG).show();
                            }
                            Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(),"Please check your mail.",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(ForgetActivity.this, Home.class));
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ForgetActivity.this, "Error",Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("Email", email.getText().toString());
                        return params;
                    }
                };
                queue.add(stringRequest);
                break;
        }
    }
}
