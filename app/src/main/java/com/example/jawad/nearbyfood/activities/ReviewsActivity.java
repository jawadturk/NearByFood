package com.example.jawad.nearbyfood.activities;

import android.app.Dialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.example.jawad.nearbyfood.R;
import com.example.jawad.nearbyfood.adapters.ReviewsAdapter;
import com.example.jawad.nearbyfood.pojos.Resturant;
import com.example.jawad.nearbyfood.pojos.Review;
import com.example.jawad.nearbyfood.pojos.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsActivity extends AppCompatActivity {


    public static final String KEY_RESTURANT_ID="resturantId";
    public static final String KEY_RESTURANT_NAME="resturantName";
    private static final String TAG = ReviewsActivity.class.getSimpleName();

    private String resturantId;
    private String resturantName="";

    private DatabaseReference mReviewReference;
    private DatabaseReference mResturantReference;
    private ReviewsAdapter mReviewsAdapter;

    @BindView(R.id.fab_add_review)
    FloatingActionButton button_addReview;

    @BindView(R.id.recyclerView_reviews)
    RecyclerView recyclerView_reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        ButterKnife.bind(this);

        if (getIntent()!=null)
        {
            resturantId=getIntent().getStringExtra(KEY_RESTURANT_ID);
            resturantName=getIntent().getStringExtra(KEY_RESTURANT_NAME);

        }
        setUpToolBar(resturantName);
        setupDataBaseReference();
        setupReviewsRecyclerView();

        button_addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            showWriteReviewDialog();
            }
        });
    }

    private void setupReviewsRecyclerView() {
        recyclerView_reviews.setNestedScrollingEnabled(false);
        recyclerView_reviews.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mReviewsAdapter = new ReviewsAdapter(this, mReviewReference);
        recyclerView_reviews.setAdapter(mReviewsAdapter);
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
    private void setupDataBaseReference() {

        mReviewReference = FirebaseDatabase.getInstance().getReference()
                .child("resturant_reviews").child(resturantId);

        mResturantReference = FirebaseDatabase.getInstance().getReference()
                .child("resturants").child(resturantId);

    }

    @Override
    protected void onStop() {
        super.onStop();
//        mReviewsAdapter.cleanupListener();
    }
    private void showWriteReviewDialog() {
        final Dialog ReviewDialog = new Dialog(this);
        // Set GUI of login screen
        ReviewDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        ReviewDialog.setCancelable(true);
        ReviewDialog.setContentView(R.layout.layout_add_review);


        // Init button of login GUI

        final EditText editText_review = (EditText) ReviewDialog.findViewById(R.id.editText_review);
        final RatingBar ratingBar = (RatingBar) ReviewDialog.findViewById(R.id.ratingBar_vendorRating);
        Button buttonSave = (Button) ReviewDialog.findViewById(R.id.button_save);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postReview(editText_review.getText().toString(), ratingBar.getRating());
                ReviewDialog.dismiss();
            }
        });


        // Make dialog box visible.
        ReviewDialog.show();
    }

    private void postReview(final String reviewText, final float rating) {
        final String uid = getUid();
        FirebaseDatabase.getInstance().getReference().child("users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user information
                        User user = dataSnapshot.getValue(User.class);
                        String authorName = user.username;

                        // Create new reve=iew object

                        Review review = new Review(uid, authorName, reviewText, rating);

                        // Push the comment, it will appear in the list
                        mReviewReference.push().setValue(review);
                        onReviewAdded(rating);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void onReviewAdded(final float rating) {
        mResturantReference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Resturant resturant = mutableData.getValue(Resturant.class);
                if (resturant == null) {
                    return Transaction.success(mutableData);
                }


                // Star the post and add self to stars
                resturant.resturantNumberOfReviews = resturant.resturantNumberOfReviews + 1;
                resturant.resturantRating = resturant.resturantRating + rating;


                // Set value and report transaction success
                mutableData.setValue(resturant);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
