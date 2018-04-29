package com.example.jawad.nearbyfood.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.jawad.nearbyfood.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DisplayImagesActivity extends AppCompatActivity {

    public final static String KEY_IMAGE_URL="image";

    private String image;

    @BindView(R.id.thumbnail)
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_images);
        ButterKnife.bind(this);
        if (getIntent()!=null)
        {
            image=getIntent().getStringExtra(KEY_IMAGE_URL);
            Picasso.with(imageView.getContext()).load(image).into(imageView);
        }
        setUpToolBar("Images");

    }

    private void setUpToolBar(String message) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(message);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }
}
