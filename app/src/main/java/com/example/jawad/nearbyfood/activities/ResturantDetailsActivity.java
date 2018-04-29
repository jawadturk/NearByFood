package com.example.jawad.nearbyfood.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.jawad.nearbyfood.R;
import com.example.jawad.nearbyfood.adapters.ImageViewPagerAdapter;
import com.example.jawad.nearbyfood.adapters.ImagesRecyclerViewAdapter;
import com.example.jawad.nearbyfood.customviews.MyViewPager;
import com.example.jawad.nearbyfood.database.DatabaseOps;
import com.example.jawad.nearbyfood.pojos.Resturant;
import com.example.jawad.nearbyfood.utils.GridSpacingItemDecoration;
import com.example.jawad.nearbyfood.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResturantDetailsActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, ImagesRecyclerViewAdapter.MyClickListener {

    private static final String TAG = ResturantDetailsActivity.class.getSimpleName() ;
    @BindView(R.id.textView_resturantName)
    TextView textView_resturantName;

    @BindView(R.id.textView_phoneNumber)
    TextView textView_resturantPhoneNumber;

    @BindView(R.id.textView_resturantAddress)
    TextView textView_resturantAddress;

    @BindView(R.id.textView_cuisineTypes)
    TextView textView_cuisineTypes;

    @BindView(R.id.textView_resturantType)
    TextView textView_resturantType;

    @BindView(R.id.textView_rating)
    TextView textView_rating;

    @BindView(R.id.textView_numberOfReviews)
    TextView textView_numberOfReviews;

    @BindView(R.id.textView_numberOfPhotos)
    TextView textView_numberOfPhotos;

    @BindView(R.id.recyclerView_menuImages)
    RecyclerView recyclerView_menuImages;

    @BindView(R.id.viewPagerCountDots)
    LinearLayout pager_indicator;

    @BindView(R.id.viewPager_eventImages)
    MyViewPager intro_images;

    @BindView(R.id.toggleButton_like)
    ToggleButton toggleButton;


    private int dotsCount;
    private ImageView[] dots;
    private ImageViewPagerAdapter mAdapter;
    private ArrayList<String> mImageResources = new ArrayList<String>();

    Handler handler;
    Runnable update;
    private Timer timer;

    private DatabaseReference mDataBaseReference;
    private ValueEventListener mResturantListener;

    public static final String KEY_RESTURANT_KEY="resturantKey";

    private String resturantId;

    private ImagesRecyclerViewAdapter mMenuImagesAdapter;

    private String phoneNumber;
    private String resturantName;
    private String latit;
    private String longtit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resturant_details);
        ButterKnife.bind(this);
        if (getIntent()!=null)
        {
            resturantId=getIntent().getStringExtra(KEY_RESTURANT_KEY);
        }
        setupToolbar();
        setUpRecyclerView();
        setupDataBaseReference();

        textView_resturantPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCallBtnClick();
            }
        });

        textView_resturantAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOnMap(latit,longtit);
            }
        });

        textView_numberOfReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResturantDetailsActivity.this, ReviewsActivity.class);
                intent.putExtra(ReviewsActivity.KEY_RESTURANT_ID, resturantId);
                intent.putExtra(ReviewsActivity.KEY_RESTURANT_NAME, resturantName);

                startActivity(intent);
            }
        });


        if (DatabaseOps.getCurrentInstance().isFavorite(resturantId)
                ) {

            toggleButton.setChecked(true);
        }
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    DatabaseOps.getCurrentInstance().insertResturantId(resturantId);
                }else {
                    DatabaseOps.getCurrentInstance().unFavorite(resturantId);
                }
            }
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_activity_eventDetails);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void setUpRecyclerView()
    {
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        recyclerView_menuImages.setLayoutManager(mLayoutManager);
        recyclerView_menuImages.addItemDecoration(new GridSpacingItemDecoration(2,(int) Utils.convertDpToPixel(8,this), true));
        recyclerView_menuImages.setItemAnimator(new DefaultItemAnimator());

        mMenuImagesAdapter=new ImagesRecyclerViewAdapter(new ArrayList<String>());
        mMenuImagesAdapter.setMyClickListener(this);
        recyclerView_menuImages.setAdapter(mMenuImagesAdapter);
    }
    private void setupDataBaseReference() {
      mDataBaseReference   = FirebaseDatabase.getInstance().getReference().child("resturants").child(resturantId);

    }

    @Override
    public void onStart() {
        super.onStart();


        ValueEventListener resturantListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Resturant resturant = dataSnapshot.getValue(Resturant.class);
                latit=resturant.resturantLocationLat;
                longtit=resturant.resturantLocationLong;
                textView_resturantName.setText(resturant.resturantName);
                textView_numberOfReviews.setText(Integer.toString(resturant.resturantNumberOfReviews)+" Reviews");
                textView_numberOfReviews.setPaintFlags(textView_numberOfPhotos.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                float rating = (float) resturant.resturantRating / resturant.resturantNumberOfReviews;
                textView_rating.setText(Double.toString(rating));
                textView_numberOfPhotos.setText(resturant.resturantPhotos.size()+" Photos");
                textView_numberOfPhotos.setPaintFlags(textView_numberOfPhotos.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                textView_resturantAddress.setText(resturant.resturantAddress);
                textView_resturantPhoneNumber.setText(resturant.phoneNumber);
                String display_selected_cuisine_categories = "";
                phoneNumber=resturant.phoneNumber;
                resturantName=resturant.resturantName;
                for (int i=0;i<resturant.resturantCuisineCategoriesNamesList.size();i++)
                {
                    display_selected_cuisine_categories+=resturant.resturantCuisineCategoriesNamesList.get(i);
                    if (i<resturant.resturantCuisineCategoriesNamesList.size()-1)
                    {
                        display_selected_cuisine_categories+=",";
                    }
                }

                textView_cuisineTypes.setText(display_selected_cuisine_categories);
                String display_selected_type_categories = "";
                for (int i=0;i<resturant.resturantQuickSearchCategoriesNamesList.size();i++)
                {
                    display_selected_type_categories+=resturant.resturantQuickSearchCategoriesNamesList.get(i);
                    if (i<resturant.resturantQuickSearchCategoriesNamesList.size()-1)
                    {
                        display_selected_type_categories+=",";
                    }
                }

                textView_resturantType.setText(display_selected_type_categories);
                setupPagerImages(resturant.resturantPhotos);

               mMenuImagesAdapter.setImagesList(resturant.resturantMenuPhotos);
               mMenuImagesAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.w(TAG, "ResturantLoad:onCancelled", databaseError.toException());

                Toast.makeText(ResturantDetailsActivity.this, "Failed to load Resturant",
                        Toast.LENGTH_SHORT).show();

            }
        };
        mDataBaseReference.addValueEventListener(resturantListener);
        // [END post_value_event_listener]

        // Keep copy of resturant listener so we can remove it when app stops
        mResturantListener = resturantListener;


    }
    private void setupPagerImages(ArrayList<String> images) {

        mImageResources.clear();
        mImageResources = images;
        mAdapter = new ImageViewPagerAdapter(ResturantDetailsActivity.this, mImageResources);
        intro_images.setAdapter(mAdapter);
        intro_images.setCurrentItem(0);
        intro_images.addOnPageChangeListener(this);

        handler = new Handler();
        update = new Runnable() {
            public void run() {
                if (intro_images.getCurrentItem() == mAdapter.getCount()-1) {
                    intro_images.setCurrentItem(0, true);
                } else {
                    intro_images.setCurrentItem(intro_images.getCurrentItem() + 1, true);
                }


            }
        };


        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 3000, 3000);


        setUiPageViewController();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 3000, 3000);
    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonselecteditem_dot));
        }

        dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.selecteditem_dot));

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(update);
        timer.cancel();
    }

    private void setUiPageViewController() {

        dotsCount = mAdapter.getCount();
        dots = new ImageView[dotsCount];
        pager_indicator.removeAllViews();
        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            if (i == 0) {
                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.selecteditem_dot));
            }
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);


            pager_indicator.addView(dots[i], params);
        }

        if (dots.length>0)
        {
            dots[0].setImageDrawable(ContextCompat.getDrawable(ResturantDetailsActivity.this, R.drawable.selecteditem_dot));

        }
    }
    @Override
    public void onStop() {
        super.onStop();

        // Remove vendor value event listener
        if (mResturantListener != null) {
            mDataBaseReference.removeEventListener(mResturantListener);
        }
    }

    @Override
    public void onItemClicked(String img) {
        Intent intent = new Intent(ResturantDetailsActivity.this,DisplayImagesActivity.class);
        intent.putExtra(DisplayImagesActivity.KEY_IMAGE_URL,img);
        startActivity(intent);
    }

    private void onCallBtnClick(){
        if (Build.VERSION.SDK_INT < 23) {
            phoneCall();
        }else {

            if (ActivityCompat.checkSelfPermission(ResturantDetailsActivity.this,
                    Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                phoneCall();
            }else {
                final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                //Asking request Permissions
                ActivityCompat.requestPermissions(ResturantDetailsActivity.this, PERMISSIONS_STORAGE, 9);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        boolean permissionGranted = false;
        switch(requestCode){
            case 9:
                permissionGranted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                break;
        }
        if(permissionGranted){
            phoneCall();
        }else {
            Toast.makeText(ResturantDetailsActivity.this, "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }

    private void phoneCall(){
        if (ActivityCompat.checkSelfPermission(ResturantDetailsActivity.this,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+phoneNumber));
           startActivity(callIntent);
        }else{
            Toast.makeText(ResturantDetailsActivity.this, "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showOnMap(String lat,String longit)
    {
        Uri gmmIntentUri = Uri.parse("geo:"+lat+","+longit);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

}
