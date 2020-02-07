package com.lovetips.krish.lovetips;

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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

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
    private SharedPreferences session, settings;
    private RewardedVideoAd mRewardedVideoAd;

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

        settings = getContext().getSharedPreferences("SETTINGS",MODE_PRIVATE);
        if(settings.getString("ADMOB_SWITCH","").equals("ON")) {
            MobileAds.initialize(getContext(), "ca-app-pub-3940256099942544~3347511713");
            mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getContext());
            mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                    new AdRequest.Builder().build());

            RewardedVideoAdListener rewardedVideoAdListener = new RewardedVideoAdListener() {
                @Override
                public void onRewardedVideoAdLoaded() {
                    mRewardedVideoAd.show();
                    //Toast.makeText(getActivity(), "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onRewardedVideoAdOpened() {
                    //Toast.makeText(getActivity(), "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onRewardedVideoStarted() {
                    //Toast.makeText(getActivity(), "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onRewardedVideoAdClosed() {
                    //Toast.makeText(getActivity(), "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
                    ////// UpdateDataBase
                }

                @Override
                public void onRewarded(RewardItem reward) {
                    //Toast.makeText(getActivity(), getString(R.string.on_rewarded_video) + " " +  reward.getAmount() + " " + reward.getType(), Toast.LENGTH_LONG).show();
                    // Reward the user.
                }

                @Override
                public void onRewardedVideoAdLeftApplication() {
                    //Toast.makeText(getActivity(), "onRewardedVideoAdLeftApplication", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onRewardedVideoAdFailedToLoad(int i) {
                    //Toast.makeText(getActivity(), "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onRewardedVideoCompleted() {

                }
            };
            mRewardedVideoAd.setRewardedVideoAdListener(rewardedVideoAdListener);
        }

        return view;
    }

    private void loadTipsDescription() {
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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
                    Toast.makeText(getContext(),"Error",Toast.LENGTH_LONG).show();
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
                params.put("TipsId",getArguments().get("TID").toString());

                return params;
            }
        };
        queue.add(stringRequest);
    }

}
