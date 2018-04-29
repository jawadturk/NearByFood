package com.example.jawad.nearbyfood.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.jawad.nearbyfood.R;
import com.example.jawad.nearbyfood.pojos.Comment;
import com.example.jawad.nearbyfood.pojos.Review;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import at.blogc.android.views.ExpandableTextView;


public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {
    private static final String TAG = ReviewsAdapter.class.getSimpleName();
    private Context mContext;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;

    private List<String> mCommentIds = new ArrayList<>();
    private List<Review> mReviews = new ArrayList<>();

    public ReviewsAdapter(final Context context, DatabaseReference ref) {
        mContext = context;
        mDatabaseReference = ref;

        // Create child event listener
        // [START child_event_listener_recycler]
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new comment has been added, add it to the displayed list
                Review comment = dataSnapshot.getValue(Review.class);

                // [START_EXCLUDE]
                // Update RecyclerView
                mCommentIds.add(dataSnapshot.getKey());
                mReviews.add(comment);
                notifyItemInserted(mReviews.size() - 1);
                // [END_EXCLUDE]
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                Review newReview = dataSnapshot.getValue(Review.class);
                String commentKey = dataSnapshot.getKey();

                // [START_EXCLUDE]
                int commentIndex = mCommentIds.indexOf(commentKey);
                if (commentIndex > -1) {
                    // Replace with the new data
                    mReviews.set(commentIndex, newReview);

                    // Update the RecyclerView
                    notifyItemChanged(commentIndex);
                } else {
                    Log.w(TAG, "onChildChanged:unknown_child:" + commentKey);
                }
                // [END_EXCLUDE]
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                // A review has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                String commentKey = dataSnapshot.getKey();

                // [START_EXCLUDE]
                int commentIndex = mCommentIds.indexOf(commentKey);
                if (commentIndex > -1) {
                    // Remove data from the list
                    mCommentIds.remove(commentIndex);
                    mReviews.remove(commentIndex);

                    // Update the RecyclerView
                    notifyItemRemoved(commentIndex);
                } else {
                    Log.w(TAG, "onChildRemoved:unknown_child:" + commentKey);
                }
                // [END_EXCLUDE]
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
                Comment movedComment = dataSnapshot.getValue(Comment.class);
                String commentKey = dataSnapshot.getKey();

                // ...
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
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_review, parent, false);
        ReviewViewHolder dataObjectHolder = new ReviewViewHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final ReviewViewHolder holder, int position) {

        Review review = mReviews.get(position);
        holder.authorName.setText(review.author);
        holder.reviewText.setText(review.review);
        holder.ratingBar.setRating(review.rating);
        // toggle the ExpandableTextView
        holder.buttonToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                holder.reviewText.toggle();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {

        public TextView authorName;
        public ExpandableTextView reviewText;
        public RatingBar ratingBar;
        public Button buttonToggle;

        public ReviewViewHolder(View itemView) {
            super(itemView);

            authorName = (TextView) itemView.findViewById(R.id.textView_reviewAuthor);
            reviewText = (ExpandableTextView) itemView.findViewById(R.id.textView_reviewBody);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar_vendorRating);
            buttonToggle = (Button) itemView.findViewById(R.id.button_toggle);

            // set interpolators for both expanding and collapsing animations
            reviewText.setInterpolator(new OvershootInterpolator());


            // listen for expand / collapse events
            reviewText.setOnExpandListener(new ExpandableTextView.OnExpandListener() {
                @Override
                public void onExpand(final ExpandableTextView view) {
                    buttonToggle.setText(R.string.readLess);
                }

                @Override
                public void onCollapse(final ExpandableTextView view) {
                    buttonToggle.setText(R.string.read_more);
                }
            });

        }
    }

    public void cleanupListener() {
        if (mChildEventListener != null) {
            mDatabaseReference.removeEventListener(mChildEventListener);
        }
    }
}
