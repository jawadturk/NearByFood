package com.example.jawad.nearbyfood.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


import com.example.jawad.nearbyfood.R;
import com.example.jawad.nearbyfood.utils.ImageLoadedCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ImageViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<String> mResources;

    public ImageViewPagerAdapter(Context mContext, ArrayList<String> mResources) {
        this.mContext = mContext;
        this.mResources = mResources;
    }

    @Override
    public int getCount() {
        return mResources.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.pager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.img_pager_item);
        ProgressBar progress;
        progress = (ProgressBar) itemView.findViewById(R.id.progressBar_imageViewPager);
        progress.setVisibility(View.VISIBLE);


        //get your image view
        String imageUrl = mResources.get(position).trim();
        String temp = imageUrl.replaceAll(" ", "%20");
        Picasso.with(imageView.getContext())
                .load(temp)
                .into(imageView, new ImageLoadedCallback(progress) {
                    @Override
                    public void onSuccess() {
                        if (this.progressBar != null) {
                            this.progressBar.setVisibility(View.GONE);
                        }
                    }
                });

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
