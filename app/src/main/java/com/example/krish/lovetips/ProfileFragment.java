package com.example.krish.lovetips;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    private SharedPreferences session;
    private EditText fullname, mobile, email, password, newPassword;
    private Button updateProfile, changePassword;
    private StringRequest stringRequest1,stringRequest2;
    private RequestQueue queue;
    private String url1 = "http://hkcodezone.com/love_tips/Api/UpdateProfile";
    private String url2 = "http://hkcodezone.com/love_tips/Api/ChangePassword";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        session = getActivity().getSharedPreferences("CUST_ID", MODE_PRIVATE);
        fullname = (EditText)view.findViewById(R.id.fpFullNameId);
        mobile = (EditText)view.findViewById(R.id.fpMobileId);
        email = (EditText)view.findViewById(R.id.fpEmailId);
        password = (EditText)view.findViewById(R.id.fpOldPasswordId);
        newPassword = (EditText)view.findViewById(R.id.fpNewPasswordId);
        updateProfile = (Button)view.findViewById(R.id.fpUpdateProfileButtonId);
        changePassword = (Button)view.findViewById(R.id.fpChangePasswordId);
        updateProfile.setOnClickListener(this);
        changePassword.setOnClickListener(this);
        queue = Volley.newRequestQueue(getContext());

        loadFields();

        return view;
    }

    private void loadFields() {
        fullname.setText(session.getString("FullName",""));
        mobile.setText(session.getString("MobileNo",""));
        email.setText(session.getString("Email",""));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fpUpdateProfileButtonId:
                stringRequest1 = new StringRequest(Request.Method.POST, url1, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject data = new JSONObject(response);
                            if(data.getInt("success") == 1){
                                Toast.makeText(getContext(),data.getString("message"),Toast.LENGTH_LONG).show();
                                session.edit().putString("Id",data.getJSONObject("data").getString("Id"))
                                        .putString("FullName",data.getJSONObject("data").getString("FullName"))
                                        .putString("MobileNo",data.getJSONObject("data").getString("MobileNo"))
                                        .putString("Email",data.getJSONObject("data").getString("Email")).commit();
                            }
                        }catch (Exception e){

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Id",session.getString("Id",""));
                        params.put("FullName",fullname.getText().toString());
                        params.put("MobileNo",mobile.getText().toString());
                        params.put("Email",email.getText().toString());
                        return params;
                    }
                };
                queue.add(stringRequest1);
                break;
            case R.id.fpChangePasswordId:
                stringRequest2 = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject data = new JSONObject(response);
                            if(data.getInt("success") == 1){
                                Toast.makeText(getContext(),data.getString("message"),Toast.LENGTH_LONG).show();
                            }
                        }catch (Exception e){

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Id",session.getString("Id",""));
                        params.put("OldPassword",password.getText().toString());
                        params.put("NewPassword",newPassword.getText().toString());
                        return params;
                    }
                };
                queue.add(stringRequest2);
                break;
        }
    }
}
