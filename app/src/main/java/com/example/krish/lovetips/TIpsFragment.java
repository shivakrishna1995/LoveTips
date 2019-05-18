package com.example.krish.lovetips;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TIpsFragment extends Fragment {

    private View view;
    private RecyclerView tipsRecyclerView;

    private List<String> items = new ArrayList<String>(Arrays.asList("FirstItemTip","SecondItemTip","ThirdItemTip","FourthItemTip","FifthItemTip","SixthItemTip"));

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tips, container, false);

        tipsRecyclerView = (RecyclerView)view.findViewById(R.id.ftItemsRecyclerViewId);
        tipsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tipsRecyclerView.setAdapter(new RecyclerTips(getContext(),items,getFragmentManager()));

        return view;
    }

}
