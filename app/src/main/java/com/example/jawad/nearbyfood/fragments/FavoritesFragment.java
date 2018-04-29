package com.example.jawad.nearbyfood.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jawad.nearbyfood.R;
import com.example.jawad.nearbyfood.activities.ResturantDetailsActivity;
import com.example.jawad.nearbyfood.adapters.FavoriteResturantsAdapter;
import com.example.jawad.nearbyfood.adapters.ResturantsAdapter;
import com.example.jawad.nearbyfood.pojos.Resturant;
import com.example.jawad.nearbyfood.viewholders.ResturantsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class FavoritesFragment extends Fragment {
    private DatabaseReference mDatabase;
    private DatabaseReference mResturantReference;
    private RecyclerView mRecycler;

    private View rootview;

    private FavoriteResturantsAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_quick_search, container, false);
        return rootview;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]
        mResturantReference = mDatabase.child("resturants");
        mRecycler = (RecyclerView) rootview.findViewById(R.id.recyclerView_quickSearchCategory);
        mRecycler.setHasFixedSize(true);

        setupRecyclerView();
    }


    private void setupRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecycler.setLayoutManager(mLayoutManager);
        mRecycler.setItemAnimator(new DefaultItemAnimator());


    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter= new FavoriteResturantsAdapter(getContext(), mResturantReference);

        mRecycler.setAdapter(mAdapter);
    }
}
