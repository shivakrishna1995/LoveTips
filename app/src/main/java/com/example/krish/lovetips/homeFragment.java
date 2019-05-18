package com.example.krish.lovetips;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class homeFragment extends Fragment {

    private ViewFlipper imageSlider;
    private int images[] = {R.drawable.images_2,R.drawable.images_3};
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        imageSlider = (ViewFlipper)view.findViewById(R.id.ltImageSliderId);

        for(int image : images){
            initializeViewFlipper(image);
        }
        return view;
    }

    public void initializeViewFlipper(int image){
        ImageView imageView = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageResource(image);

        imageSlider.addView(imageView);
        imageSlider.setFlipInterval(3000);
        imageSlider.setAutoStart(true);
        imageSlider.setInAnimation(getContext(),android.R.anim.slide_in_left);
        imageSlider.setOutAnimation(getContext(),android.R.anim.slide_out_right);
    }
}
