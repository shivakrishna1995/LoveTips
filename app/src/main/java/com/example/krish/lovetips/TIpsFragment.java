package com.example.krish.lovetips;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class TIpsFragment extends Fragment {

    private View view;
    private RecyclerView tipsRecyclerView;
    private RequestQueue queue;
    private StringRequest stringRequest;
    private String url = "http://hkcodezone.com/love_tips/Api/TipsLists";
    private SharedPreferences session;
    private List<JSONObject> itemsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tips, container, false);

        queue = Volley.newRequestQueue(getContext());
        tipsRecyclerView = (RecyclerView)view.findViewById(R.id.ftItemsRecyclerViewId);
        tipsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemsList = new ArrayList<>();
        session = getActivity().getSharedPreferences("CUST_ID",MODE_PRIVATE);
        gettignTips();



        return view;
    }

    private void gettignTips() {
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("Tips");
                    for(int i=0; i< jsonArray.length(); i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        itemsList.add(jsonObject1);
                        tipsRecyclerView.setAdapter(new RecyclerTips(getContext(),itemsList,getFragmentManager()));
                    }
                }
                catch(Exception e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Error",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Id",session.getString("Id",""));
                return params;
            }
        };
        queue.add(stringRequest);
    }

}
