package com.example.jawad.nearbyfood.utils;

import android.widget.ProgressBar;

import com.squareup.picasso.Callback;

/**
 * Created by User on 12/18/2015.
 */
public class ImageLoadedCallback implements Callback {
    public ProgressBar progressBar;

    public ImageLoadedCallback(ProgressBar progBar) {
        progressBar = progBar;
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onError() {

    }
}
