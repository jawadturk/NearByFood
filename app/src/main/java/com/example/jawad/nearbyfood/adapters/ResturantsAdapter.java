package com.example.jawad.nearbyfood.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.jawad.nearbyfood.R;
import com.example.jawad.nearbyfood.activities.FetchResturantsActivity;
import com.example.jawad.nearbyfood.activities.ResturantDetailsActivity;
import com.example.jawad.nearbyfood.pojos.Resturant;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.joooonho.SelectableRoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;




public class ResturantsAdapter extends RecyclerView.Adapter<ResturantsAdapter.ResturantViewHolder> {
    private static final String TAG = ResturantsAdapter.class.getSimpleName();
    private Context mContext;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;
    private List<String> mResturantsIds = new ArrayList<>();
    private String categoryId;
    private String categoryType;
    private List<Resturant> mResturants = new ArrayList<>();

    public ResturantsAdapter(final Context context, DatabaseReference ref,String acategoryId,String acategoryType) {
        mContext = context;
        mDatabaseReference = ref;
        this.categoryId=acategoryId;
        this.categoryType=acategoryType;

        // Create child event listener
        // [START child_event_listener_recycler]
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                Resturant resturant = dataSnapshot.getValue(Resturant.class);

                if (categoryType.equals(FetchResturantsActivity.KEY_CUISINE))
                {
                    if (resturant.resturantCuisineCategories.contains(categoryId))
                    {
                        mResturants.add(resturant);
                        mResturantsIds.add(dataSnapshot.getKey());
                    }
                }else if (categoryType.equals(FetchResturantsActivity.KEY_QUICK_SEARCH))
                {
                    if (resturant.resturantQuickSearchCategories.contains(categoryId))
                    {
                        mResturants.add(resturant);
                        mResturantsIds.add(dataSnapshot.getKey());
                    }
                }


                notifyItemInserted(mResturants.size() - 1);
                // [END_EXCLUDE]
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());


                Resturant newResturant = dataSnapshot.getValue(Resturant.class);
                String resturantId = dataSnapshot.getKey();

                // [START_EXCLUDE]
                int commentIndex = mResturantsIds.indexOf(resturantId);
                if (commentIndex > -1) {

                    if (categoryType.equals(FetchResturantsActivity.KEY_CUISINE))
                    {
                        if (newResturant.resturantCuisineCategories.contains(categoryId))
                        {
                            // Replace with the new data
                            mResturants.set(commentIndex, newResturant);
                            // Update the RecyclerView
                            notifyItemChanged(commentIndex);
                        }
                    }else if (categoryType.equals(FetchResturantsActivity.KEY_QUICK_SEARCH))
                    {
                        if (newResturant.resturantQuickSearchCategories.contains(categoryId))
                        {
                            // Replace with the new data
                            mResturants.set(commentIndex, newResturant);
                            // Update the RecyclerView
                            notifyItemChanged(commentIndex);
                        }
                    }



                } else {
                    Log.w(TAG, "onChildChanged:unknown_child:" + resturantId);
                }
                // [END_EXCLUDE]
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());


                String resturantId = dataSnapshot.getKey();

                // [START_EXCLUDE]
                int commentIndex = mResturantsIds.indexOf(resturantId);
                if (commentIndex > -1) {
                    // Remove data from the list
                    mResturantsIds.remove(commentIndex);
                    mResturants.remove(commentIndex);

                    // Update the RecyclerView
                    notifyItemRemoved(commentIndex);
                } else {
                    Log.w(TAG, "onChildRemoved:unknown_child:" + resturantId);
                }
                // [END_EXCLUDE]
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Toast.makeText(mContext, "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        ref.addChildEventListener(childEventListener);
        // [END child_event_listener_recycler]

        // Store reference to listener so it can be removed on app stop
        mChildEventListener = childEventListener;
    }

    @Override
    public ResturantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.resturant_list_item, parent, false);
        ResturantViewHolder dataObjectHolder = new ResturantViewHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final ResturantViewHolder holder, int position) {

        final Resturant resturant = mResturants.get(position);
        holder.resturantName.setText(resturant.resturantName);
        holder. resturantAddress.setText(resturant.resturantAddress);

        if(!resturant.cuisineTypes.isEmpty())
        {
            holder.   resturantCuisineType.setText(resturant.cuisineTypes);
        }
        holder. resturantRating.setText(Double.toString(resturant.resturantRating));


        if (resturant.resturantPhotos.size() > 0) {
            Picasso.with( holder.thumbnail.getContext()).load(resturant.resturantPhotos.get(0)).into( holder.thumbnail);

        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(mContext, ResturantDetailsActivity.class);
                intent.putExtra(ResturantDetailsActivity.KEY_RESTURANT_KEY,resturant.resturantId);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mResturants.size();
    }

    public static class ResturantViewHolder extends RecyclerView.ViewHolder {

        public SelectableRoundedImageView thumbnail;
        public TextView resturantName;
        public TextView resturantAddress;
        public TextView resturantCuisineType;
        public TextView resturantRating;
        public CardView cardView;

        public ResturantViewHolder(View itemView) {
            super(itemView);

            thumbnail = (SelectableRoundedImageView) itemView.findViewById(R.id.imageView_thumbnail);
            resturantName = (TextView) itemView.findViewById(R.id.textView_resturantName);
            resturantAddress = (TextView) itemView.findViewById(R.id.textView_resturantAddress);
            resturantRating = (TextView) itemView.findViewById(R.id.textView_rating);
            resturantCuisineType = (TextView) itemView.findViewById(R.id.textView_resturantCuisines);
            cardView = (CardView) itemView.findViewById(R.id.vendor_cardview);

        }
    }

    public void cleanupListener() {
        if (mChildEventListener != null) {
            mDatabaseReference.removeEventListener(mChildEventListener);
        }
    }
}
