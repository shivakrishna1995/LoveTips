package com.example.krish.lovetips;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

import static android.content.Context.MODE_PRIVATE;

public class TipsDescriptionFragment extends Fragment {

    private TextView titleText, descriptionText;
    private ImageView logo;
    private StringRequest stringRequest;
    private RequestQueue queue;
    private String url = "http://hkcodezone.com/love_tips/Api/TipsDetails";
    private SharedPreferences session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tips_description, container, false);

        titleText = (TextView)view.findViewById(R.id.ftdTitleId);
        descriptionText = (TextView)view.findViewById(R.id.ftdDescriptionId);
        logo = (ImageView)view.findViewById(R.id.ftdLogoId);
        queue = Volley.newRequestQueue(getContext());
        session = getActivity().getSharedPreferences("CUST_ID",MODE_PRIVATE);


        loadTipsDescription();

        return view;
    }

    private void loadTipsDescription() {
        new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    if(new JSONObject(response).getInt("success") == 1){
                        JSONObject data = new JSONObject(response).getJSONObject("data");
                        titleText.setText(data.getString("Heading"));
                        descriptionText.setText(data.getString("Description"));
                        Glide.with(getContext()).load(data.getString("Image")).into(logo);
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
                params.put("TipsId",getArguments().get("TID").toString());

                return params;
            }
        };
    }

}
