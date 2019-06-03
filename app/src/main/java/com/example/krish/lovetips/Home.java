package com.example.krish.lovetips;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        registration = (TextView)findViewById(R.id.ahRegistrationId);
        forgetPassword = (TextView)findViewById(R.id.ahForgetPasswordId);
        SignInId = (TextView)findViewById(R.id.ahSignInId);
        registration.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
        SignInId.setOnClickListener(this);
        username = (EditText)findViewById(R.id.ahUsernameId);
        password = (EditText)findViewById(R.id.ahPasswordId);
        queue = Volley.newRequestQueue(Home.this);
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        session = getSharedPreferences("CUST_ID",MODE_PRIVATE);

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
