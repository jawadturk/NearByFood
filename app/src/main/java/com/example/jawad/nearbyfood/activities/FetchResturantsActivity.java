package com.example.jawad.nearbyfood.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.example.jawad.nearbyfood.R;
import com.example.jawad.nearbyfood.adapters.ResturantsAdapter;
import com.example.jawad.nearbyfood.pojos.Resturant;
import com.example.jawad.nearbyfood.viewholders.ResturantsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FetchResturantsActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;

    private RecyclerView mRecycler;
    private DatabaseReference mResturantReference;
    public static final String KEY_CATEGORY_ID="categoryId";
    public static final String KEY_CATEGORY_TYPE="cuisineOrQuickSearch";
    public static final String KEY_CUISINE="cuisine";
    public static final String KEY_QUICK_SEARCH="quickSearch";
    private ResturantsAdapter mAdapter;
    private String categoryId;
    private String categoryType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cuisine);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]
        mResturantReference = mDatabase.child("resturants");

        mRecycler = (RecyclerView) findViewById(R.id.recyclerView_resturants);
        mRecycler.setHasFixedSize(true);

        if (getIntent()!=null)
        {
            categoryId=getIntent().getStringExtra(KEY_CATEGORY_ID);
            categoryType=getIntent().getStringExtra(KEY_CATEGORY_TYPE);
        }
        setupRecyclerView();
        setUpToolBar("Resturants");
    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(FetchResturantsActivity.this);
        mRecycler.setLayoutManager(mLayoutManager);
        mRecycler.setItemAnimator(new DefaultItemAnimator());


        mAdapter= new ResturantsAdapter(this, mResturantReference,categoryId,categoryType);
        mRecycler.setAdapter(mAdapter);
    }
    @Override
    public void onStop() {
        super.onStop();


        mAdapter.cleanupListener();
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
