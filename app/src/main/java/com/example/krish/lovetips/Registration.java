package com.example.krish.lovetips;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import static android.accounts.AccountManager.KEY_PASSWORD;

public class Registration extends Activity implements View.OnClickListener {


    private TextView backToLogin;
    private RequestQueue queue;
    private StringRequest registerRequest;
    private String registerUrl = "http://hkcodezone.com/love_tips/Api/SignUp";
    private EditText fullname, password, mobile, email;
    private Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        backToLogin = (TextView)findViewById(R.id.ahBackToLoginId);
        backToLogin.setOnClickListener(this);
        queue = Volley.newRequestQueue(Registration.this);
        fullname = (EditText)findViewById(R.id.arFullNameId);
        password = (EditText)findViewById(R.id.arPasswordId);
        mobile = (EditText)findViewById(R.id.arMobileId);
        email = (EditText)findViewById(R.id.arEmailId);
        signUp = (Button)findViewById(R.id.arSignUp);
        signUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ahBackToLoginId:
                startActivity(new Intent(Registration.this,Home.class));
                finish();
                break;
            case R.id.arSignUp:
                if(fullname.getText().toString().length() > 0 && password.getText().toString().length() > 0 && mobile.getText().toString().length() > 0 && email.getText().toString().length() > 0){
                    registerRequest = new StringRequest(Request.Method.POST, registerUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject data = new JSONObject(response);
                                if(data.getInt("success") == 1){
                                    Toast.makeText(Registration.this,"User registration successfully",Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(Registration.this,Home.class));
                                }
                                else{
                                    Toast.makeText(Registration.this,"This email is already exist.",Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Registration.this, "Error",Toast.LENGTH_LONG).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("FullName",fullname.getText().toString());
                            params.put("Password", password.getText().toString());
                            params.put("MobileNo",mobile.getText().toString());
                            params.put("Email", email.getText().toString());
                            return params;
                        }
                    };
                    queue.add(registerRequest);
                }
                else{
                    Toast.makeText(Registration.this, "Field's must not be empty.",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
