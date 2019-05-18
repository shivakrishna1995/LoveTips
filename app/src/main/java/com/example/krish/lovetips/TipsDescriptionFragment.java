package com.example.krish.lovetips;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TipsDescriptionFragment extends Fragment {

    private TextView titleText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tips_description, container, false);

        titleText = (TextView)view.findViewById(R.id.ftdTitleId);
        titleText.setText(getArguments().get("Title").toString());

        return view;
    }

}
