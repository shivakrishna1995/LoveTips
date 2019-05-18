package com.example.krish.lovetips;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerTips extends RecyclerView.Adapter<RecyclerTips.MyHolder> {

    private List<String> itemsList;
    private Context context;
    private FragmentManager fragmentManager;

    public RecyclerTips(Context context, List<String> itemsList, FragmentManager fragmentManager){
        this.itemsList = itemsList;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_tips_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, final int i) {
        myHolder.itemTextView.setText(itemsList.get(i));
        myHolder.itemTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new TipsDescriptionFragment();
                Bundle bundle = new Bundle();
                bundle.putString("Title",itemsList.get(i));
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().addToBackStack("TIPS_TAG").replace(R.id.cltFragmetnFrameId,fragment).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView itemTextView;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            itemTextView = (TextView)itemView.findViewById(R.id.rtiTextviewId);
        }
    }
}
