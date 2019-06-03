package com.example.krish.lovetips;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class homeFragment extends Fragment {

    private ViewFlipper imageSlider;
    private int images[] = {R.drawable.images_2,R.drawable.images_3};
    private View view;
    private RequestQueue queue;
    private StringRequest stringRequest;
    private String url = "http://hkcodezone.com/love_tips/Api/HomePage";
    private SharedPreferences session;
    private RecyclerView recyclerView;
    private List<JSONObject> itemsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        imageSlider = (ViewFlipper)view.findViewById(R.id.ltImageSliderId);
        queue = Volley.newRequestQueue(getContext());
        session = getActivity().getSharedPreferences("CUST_ID",MODE_PRIVATE);

        itemsList = new ArrayList<>();
        recyclerView = (RecyclerView)view.findViewById(R.id.fhRecyclerViewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(true);

        loadImages();
        return view;
    }

    private void loadImages()
    {
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {
                    JSONObject data = new JSONObject(response);
                    JSONArray dataArray = data.getJSONObject("data").getJSONArray("Slider");
                    for(int i=0; i< dataArray.length(); i++){
                        String url = dataArray.getJSONObject(i).getString("Image");
                        //Toast.makeText(getContext(),url,Toast.LENGTH_LONG).show();
                        initializeViewFlipper(url);
                    }

                    JSONArray dataArray1 = data.getJSONObject("data").getJSONArray("Tips");
                    for(int i=0; i< dataArray1.length(); i++){
                        itemsList.add(dataArray1.getJSONObject(i));
                        recyclerView.setAdapter(new RecyclerTips(getContext(),itemsList,getFragmentManager()));
                    }
                }
                catch(Exception e)
                {

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

    public void initializeViewFlipper(String url){
        ImageView imageView = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(getContext()).load(url).into(imageView);


        imageSlider.addView(imageView);
        imageSlider.setFlipInterval(3000);
        imageSlider.setAutoStart(true);
        imageSlider.setInAnimation(getContext(),android.R.anim.slide_in_left);
        imageSlider.setOutAnimation(getContext(),android.R.anim.slide_out_right);
    }
}
