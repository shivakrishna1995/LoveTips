package com.example.krish.lovetips;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ss.com.bannerslider.Slider;

import static android.content.Context.MODE_PRIVATE;

public class homeFragment extends Fragment {

    private View view;
    private RequestQueue queue;
    private StringRequest stringRequest;
    private String url = "http://hkcodezone.com/love_tips/Api/HomePage";
    private SharedPreferences session;
    private RecyclerView recyclerView;
    private List<JSONObject> itemsList;

    private Slider imageSlider;
    private List<String> imagesList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        queue = Volley.newRequestQueue(getContext());
        session = getActivity().getSharedPreferences("CUST_ID",MODE_PRIVATE);

        itemsList = new ArrayList<>();
        recyclerView = (RecyclerView)view.findViewById(R.id.fhRecyclerViewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(true);

        Slider.init(new MyImageLoadingService(getContext()));
        imageSlider = (Slider)view.findViewById(R.id.ltImageSliderId);
        imagesList = new ArrayList<>();

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
                        imagesList.add(url);
                        imageSlider.setAdapter(new MySliderAdapter(imagesList));
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
}
