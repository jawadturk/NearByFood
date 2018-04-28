package com.example.jawad.nearbyfood.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jawad.nearbyfood.R;
import com.example.jawad.nearbyfood.helper.IntentExtra;
import com.example.jawad.nearbyfood.pojos.CuisineCategories;
import com.example.jawad.nearbyfood.pojos.User;
import com.example.jawad.nearbyfood.pojos.QuickSearchCategories;
import com.example.jawad.nearbyfood.uploadimageservice.MyUploadService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.liuguangqiang.ipicker.IPicker;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddQuickSearch_CuisineCategoryActivity  extends BaseActivity  {
    private ProgressDialog mProgressDialog;

    private static final String TAG = AddQuickSearch_CuisineCategoryActivity.class.getSimpleName();
    private static final String REQUIRED = "Required";


    private DatabaseReference mDatabase;


    private EditText mTitleField;
    private FloatingActionButton mSubmitButton;
    private FloatingActionButton mCaptureImage;
    private ImageView mImageView;
    private Uri mFileUri = null;
    private Uri mDownloadUrl = null;

    private BroadcastReceiver mBroadcastReceiver;
    private static final String KEY_FILE_URI = "key_file_uri";
    String addingType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_quick_search__cuisine_category);

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        mTitleField = (EditText) findViewById(R.id.field_title);
        mCaptureImage = (FloatingActionButton) findViewById(R.id.fab_chooseImage);
        mSubmitButton = (FloatingActionButton) findViewById(R.id.fab_submit_post);
        mImageView=(ImageView)findViewById(R.id.imageView_vendorCategoryImage) ;

        IPicker.setLimit(1);
        IPicker.setCropEnable(false);
        IPicker.setOnSelectedListener(new IPicker.OnSelectedListener() {
            @Override
            public void onSelected(final List<String> paths) {

                File file = new File(paths.get(0));
                Uri uri = Uri.fromFile(file);

                uploadFromUri(uri);
                Glide.with(mImageView.getContext()).load(paths.get(0)).into(mImageView);
            }
        });
        mCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IPicker.open(getApplicationContext());
            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "onReceive:" + intent);
                hideDialog();

                switch (intent.getAction()) {

                    case MyUploadService.UPLOAD_COMPLETED:
                    case MyUploadService.UPLOAD_ERROR:
                        onUploadResultIntent(intent);
                        break;
                }
            }
        };

        if (getIntent()!=null)
        {
            addingType=getIntent().getStringExtra(IntentExtra.QuickSearchOrCuisine.key);
        }

        if (addingType.equals(IntentExtra.QuickSearchOrCuisine.typeCuisine))
        {
            setUpToolBar("New Cuisine Category");
        }else {
            setUpToolBar("New Quick Search Category");
        }

    }

    private void submitPost() {
        final String title = mTitleField.getText().toString();


        // Title is required
        if (TextUtils.isEmpty(title)) {
            mTitleField.setError(REQUIRED);
            return;
        }


        // Disable button so there are no multi-posts
        setEditingEnabled(false);
        Toast.makeText(this, "Posting...", Toast.LENGTH_SHORT).show();

        // [START single_value_read]
        final String userId = getUid();
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        // [START_EXCLUDE]
                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(AddQuickSearch_CuisineCategoryActivity.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Write new post
                            writeNewQuickSearchCategory(title, mDownloadUrl);
                        }

                        // Finish this Activity, back to the stream
                        setEditingEnabled(true);
                        finish();
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // [START_EXCLUDE]
                        setEditingEnabled(true);
                        // [END_EXCLUDE]
                    }
                });
        // [END single_value_read]
    }

    private void setEditingEnabled(boolean enabled) {
        mTitleField.setEnabled(enabled);

        if (enabled) {
            mSubmitButton.setVisibility(View.VISIBLE);
        } else {
            mSubmitButton.setVisibility(View.GONE);
        }
    }

    private void writeNewQuickSearchCategory(String title, Uri imageUrl) {


        if (addingType.equals(IntentExtra.QuickSearchOrCuisine.typeCuisine))
        {
            String key = mDatabase.child("cuisine_categories").push().getKey();
            CuisineCategories post = new CuisineCategories(title, imageUrl.toString(), key);
            Map<String, Object> postValues = post.toMap();

            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/cuisine_categories/" + key, postValues);


            mDatabase.updateChildren(childUpdates);
        }else if (addingType.equals(IntentExtra.QuickSearchOrCuisine.typeQuickSearch))
        {
            String key = mDatabase.child("quick-search-categories").push().getKey();
            QuickSearchCategories post = new QuickSearchCategories(title, imageUrl.toString(), key);
            Map<String, Object> postValues = post.toMap();

            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/quick-search-categories/" + key, postValues);


            mDatabase.updateChildren(childUpdates);
        }

    }

    private void uploadFromUri(Uri fileUri) {
        Log.d(TAG, "uploadFromUri:src:" + fileUri.toString());

        // Save the File URI
        mFileUri = fileUri;

        // Start MyUploadService to upload the file, so that the file is uploaded
        // even if this Activity is killed or put in the background
        startService(new Intent(this, MyUploadService.class)
                .putExtra(MyUploadService.EXTRA_FILE_URI, fileUri)
                .setAction(MyUploadService.ACTION_UPLOAD));

        // Show loading spinner
        showProgressDialog(getString(R.string.progress_uploading));
    }
    private void showProgressDialog(String caption) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.setMessage(caption);
        mProgressDialog.show();
    }
    private void hideDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
    private void onUploadResultIntent(Intent intent) {
        // Got a new intent from MyUploadService with a success or failure
        mDownloadUrl = intent.getParcelableExtra(MyUploadService.EXTRA_DOWNLOAD_URL);
        mFileUri = intent.getParcelableExtra(MyUploadService.EXTRA_FILE_URI);
        Log.d(TAG, "onUploadResultIntent: "+mDownloadUrl);

    }
    @Override
    public void onStop() {
        super.onStop();

        // Unregister download receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Register receiver for uploads and downloads
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.registerReceiver(mBroadcastReceiver, MyUploadService.getIntentFilter());
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
