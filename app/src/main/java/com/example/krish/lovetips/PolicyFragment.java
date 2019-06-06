package com.example.krish.lovetips;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import static android.content.Context.MODE_PRIVATE;
import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class PolicyFragment extends Fragment {
    private View view;
    private WebView webview;
    private SharedPreferences settings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_policy, container, false);

        webview = (WebView)view.findViewById(R.id.fpWebViewId);
        settings = getActivity().getSharedPreferences("SETTINGS",MODE_PRIVATE);
        String url  = settings.getString("PRIVACY_POLICY_LINK","");
        if(! url.equals("")){
            webview.loadUrl(url);
        }

        return view;
    }
}
