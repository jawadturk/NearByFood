package com.example.jawad.nearbyfood.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.jawad.nearbyfood.R;
import com.example.jawad.nearbyfood.adapters.HorizontalImageListSelectorAdapter;
import com.example.jawad.nearbyfood.pojos.CuisineCategories;
import com.example.jawad.nearbyfood.pojos.QuickSearchCategories;
import com.example.jawad.nearbyfood.pojos.Resturant;
import com.example.jawad.nearbyfood.uploadimageservice.MyUploadService;
import com.example.jawad.nearbyfood.utils.RecyclerTouchListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.liuguangqiang.ipicker.IPicker;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddNewResturantActivity extends AppCompatActivity {


    private static final String TAG = AddNewResturantActivity.class.getSimpleName() ;
    @BindView(R.id.recyclerView_foodImages)
    RecyclerView recyclerView_foodImages;

    @BindView(R.id.recyclerView_menuImages)
    RecyclerView recyclerView_menuImages;

    @BindView(R.id.editText_resturantName)
    EditText editText_resturantName;

    @BindView(R.id.editText_resturantAddress)
    EditText editText_resturantAddress;

    @BindView(R.id.editText_resturantLocation)
    EditText editText_resturantLocation;

    @BindView(R.id.editText_resturantPhoneNumber)
    EditText editText_resturantPhoneNumber;

    @BindView(R.id.editText_resturantCuisineCategories)
    EditText editText_resturantCuisineChoose;

    @BindView(R.id.editText_resturantQuickSearchCategories)
    EditText editText_resturantQuickSearchChoose;

    @BindView(R.id.button_save)
    Button buttonAddNewResturant;


    String [] quickSearchCategories;
    boolean[] quickSearchCheckedState;

    String [] cuisineCategories;
    boolean[] cuisineCheckedState;

    private List<CuisineCategories> cuisineCategoriesList= new ArrayList<>();
    private List<QuickSearchCategories> quickSearchCategoriesList = new ArrayList<>();

    private HorizontalImageListSelectorAdapter mPhotosAdapter;
    private HorizontalImageListSelectorAdapter mMenuPhotosAdapter;

    ArrayList<String> uploadedImagesUrls = new ArrayList<>();
    ArrayList<String> uploadedMenuImagesUrls = new ArrayList<>();

    ArrayList<String> selectedCuisinesCategories = new ArrayList<>();
    ArrayList<String> selectedQuickSearchCategories = new ArrayList<>();

    private int currentIndex = 0;
    private int numberOfRetriedLeft = 3;

    private ProgressDialog mProgressDialog;
    private BroadcastReceiver mBroadcastReceiver;

    private DatabaseReference mDatabase;

    private ArrayList<String> imagesToUpload = new ArrayList<>();

    private boolean ipickerChoosingPhotos=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_resturant);
        ButterKnife.bind(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        setUpToolBar();
        retrieveQuickSearchCategories();
        retrieveCuisineCategories();
        setupBroadCastReceiver();

        setupResturantImagesRecyclerView();
        setupMenuImagesRecyclerView();

        setupIPicker();
        buttonAddNewResturant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    //start uploading images first
                    if (mPhotosAdapter.getImageURLList().size() > 0||mMenuPhotosAdapter.getImageURLList().size()>0) {
                        imagesToUpload.addAll(mPhotosAdapter.getImageURLList());
                        imagesToUpload.addAll(mMenuPhotosAdapter.getImageURLList());
                        uploadFromUri();
                    } else {
                        Resturant resturant = new Resturant();
                        resturant.resturantName = editText_resturantName.getText().toString();
                        resturant.resturantAddress = editText_resturantAddress.getText().toString();
                        resturant.phoneNumber = editText_resturantPhoneNumber.getText().toString();
                        resturant.resturantMenuPhotos=uploadedMenuImagesUrls;
                        resturant.resturantPhotos=uploadedImagesUrls;
                        resturant.resturantCuisineCategories=selectedCuisinesCategories;
                        resturant.resturantQuickSearchCategories=selectedQuickSearchCategories;

                        writeResturantToDataBase(resturant);
                    }


                } else {

                }
            }
        });

        editText_resturantCuisineChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder1 = new AlertDialog.Builder(AddNewResturantActivity.this)
                        .setTitle("Choose a Cuisine Category")
                        .setMultiChoiceItems(cuisineCategories, cuisineCheckedState, new DialogInterface.OnMultiChoiceClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
// TODO Auto-generated method stub

//storing the checked state of the items in an array
//                                checked_state[which] = isChecked;
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
// TODO Auto-generated method stub
                                String display_selected_cuisine_categories = "";
                                selectedCuisinesCategories.clear();
                                for (int i = 0; i < cuisineCheckedState.length; i++) {
                                    if (cuisineCheckedState[i] == true) {
                                        selectedCuisinesCategories.add(cuisineCategoriesList.get(i).cuisineCategoryId);
                                        display_selected_cuisine_categories = display_selected_cuisine_categories + " " + cuisineCategories[i];
                                    }
                                }

                                editText_resturantCuisineChoose.setText(display_selected_cuisine_categories);

//clears the String used to store the displayed text
                                display_selected_cuisine_categories = null;


//used to dismiss the dialog upon user selection.
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertdialog1 = builder1.create();
                alertdialog1.show();
            }
        });

        editText_resturantQuickSearchChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder1 = new AlertDialog.Builder(AddNewResturantActivity.this)
                        .setTitle("Choose a Quick Search Category")
                        .setMultiChoiceItems(quickSearchCategories, quickSearchCheckedState, new DialogInterface.OnMultiChoiceClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
// TODO Auto-generated method stub

//storing the checked state of the items in an array
//                                checked_state[which] = isChecked;
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
// TODO Auto-generated method stub
                                String categoriesSelected = "";
                                selectedQuickSearchCategories.clear();
                                for (int i = 0; i < quickSearchCheckedState.length; i++) {
                                    if (quickSearchCheckedState[i] == true) {
                                        selectedQuickSearchCategories.add(quickSearchCategoriesList.get(i).quickSearchCategoryId);
                                        categoriesSelected = categoriesSelected + " " + quickSearchCategories[i];
                                    }
                                }
editText_resturantQuickSearchChoose.setText(categoriesSelected);
//clears the String used to store the displayed text
                                categoriesSelected = null;

                                dialog.dismiss();
                            }
                        });
                AlertDialog alertdialog1 = builder1.create();
                alertdialog1.show();
            }
        });
    }

    private void setupIPicker() {
        IPicker.setLimit(10);
        IPicker.setCropEnable(false);
        IPicker.setOnSelectedListener(new IPicker.OnSelectedListener() {
            @Override
            public void onSelected(final List<String> paths) {

                if (ipickerChoosingPhotos==true)
                {
                    mPhotosAdapter.setImageURLList(paths, AddNewResturantActivity.this);
                    mPhotosAdapter.notifyDataSetChanged();
                }
                else
                {
                    mMenuPhotosAdapter.setImageURLList(paths, AddNewResturantActivity.this);
                    mMenuPhotosAdapter.notifyDataSetChanged();
                }
                }


        });
    }

    public Query getSearchCategories(DatabaseReference databaseReference) {
        Query query = databaseReference.child("quick-search-categories").orderByKey();
        return query;
    }

    public Query getCuisineCategory(DatabaseReference databaseReference) {
        Query query = databaseReference.child("cuisine_categories").orderByKey();
        return query;
    }


    private void retrieveCuisineCategories() {
        Query postsQuery = getCuisineCategory(mDatabase);

        postsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clear old data
                cuisineCategoriesList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    CuisineCategories categories = postSnapshot.getValue(CuisineCategories.class);


                    if (!TextUtils.isEmpty(categories.cuisineCategoryName) && !TextUtils.isEmpty(categories.cuisineCategoryId)) {
                        cuisineCategoriesList.add(categories);
                    }
                    cuisineCategories = new String[cuisineCategoriesList.size()];
                    cuisineCheckedState = new boolean[cuisineCategoriesList.size()];
                    for (int i = 0; i < cuisineCategoriesList.size(); i++) {
                        cuisineCategories[i] = cuisineCategoriesList.get(i).cuisineCategoryName;
                        cuisineCheckedState[i]=false;
                    }



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void retrieveQuickSearchCategories() {
        Query postsQuery = getSearchCategories(mDatabase);

        postsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clear old data
                quickSearchCategoriesList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    QuickSearchCategories categories = postSnapshot.getValue(QuickSearchCategories.class);


                    if (!TextUtils.isEmpty(categories.quickSearchCategoryName) && !TextUtils.isEmpty(categories.quickSearchCategoryId)) {
                        quickSearchCategoriesList.add(categories);
                    }
                    quickSearchCategories = new String[quickSearchCategoriesList.size()];
                    quickSearchCheckedState = new boolean[quickSearchCategoriesList.size()];
                    for (int i = 0; i < quickSearchCategoriesList.size(); i++) {
                        quickSearchCategories[i] = quickSearchCategoriesList.get(i).quickSearchCategoryName;
                        quickSearchCheckedState[i]=false;
                    }



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setupBroadCastReceiver() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "onReceive:" + intent);
                hideDialog();

                switch (intent.getAction()) {

                    case MyUploadService.UPLOAD_COMPLETED:
                        Log.d(TAG, "images size" +imagesToUpload.size());
                        onUploadResultIntent(intent);


                        break;
                    case MyUploadService.UPLOAD_ERROR:
                        numberOfRetriedLeft--;
                        if (numberOfRetriedLeft == 0) {
                            currentIndex++;
                            numberOfRetriedLeft = 3;
                        }
                        uploadFromUri();
                        break;
                }
            }
        };
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
        Uri imageUrl = intent.getParcelableExtra(MyUploadService.EXTRA_DOWNLOAD_URL);
        if (currentIndex<=mPhotosAdapter.getImageURLList().size()-1)
        {
            uploadedImagesUrls.add(imageUrl.toString());
        }else
        {
            uploadedMenuImagesUrls.add(imageUrl.toString());
        }

        Log.d(TAG, "onUploadResultIntent: " + imageUrl);
        currentIndex++;
        numberOfRetriedLeft = 3;
        if (currentIndex <= imagesToUpload.size() - 1) {
            uploadFromUri();
        } else {

            Resturant resturant = new Resturant();
            resturant.resturantName = editText_resturantName.getText().toString();
            resturant.resturantAddress = editText_resturantAddress.getText().toString();
            resturant.phoneNumber = editText_resturantPhoneNumber.getText().toString();
            resturant.resturantMenuPhotos=uploadedMenuImagesUrls;
            resturant.resturantPhotos=uploadedImagesUrls;
            resturant.resturantCuisineCategories=selectedCuisinesCategories;
            resturant.resturantQuickSearchCategories=selectedQuickSearchCategories;

            writeResturantToDataBase(resturant);
        }


    }
    private void writeResturantToDataBase(Resturant resturant) {
        String key = mDatabase.child("resturants").push().getKey();
        resturant.resturantId = key;

        Map<String, Object> vendorValues = resturant.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/resturants/" + key, vendorValues);


        mDatabase.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(AddNewResturantActivity.this, "Insert resturant Completed", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
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

    private void uploadFromUri() {
        File file = new File(imagesToUpload.get(currentIndex));
        Uri uri = Uri.fromFile(file);
        Log.d(TAG, "uploadFromUri:src:" + uri.toString());
        // Start MyUploadService to upload the file, so that the file is uploaded
        // even if this Activity is killed or put in the background
        startService(new Intent(this, MyUploadService.class)
                .putExtra(MyUploadService.EXTRA_FILE_URI, uri)
                .putExtra(MyUploadService.EXTRA_FILE_DIRECTOY_NAME, "resturant_photos")
                .setAction(MyUploadService.ACTION_UPLOAD));

        // Show loading spinner
        showProgressDialog(getString(R.string.progress_uploading));
    }

    private void setUpToolBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add New Resturant");
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

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(editText_resturantName.getText().toString())) {
            editText_resturantName.setError("Required");
            result = false;
        } else {
            editText_resturantName.setError(null);
        }

        if (TextUtils.isEmpty(editText_resturantAddress.getText().toString())) {
            editText_resturantAddress.setError("Required");
            result = false;
        } else {
            editText_resturantAddress.setError(null);
        }
        if (TextUtils.isEmpty(editText_resturantPhoneNumber.getText().toString())) {
            editText_resturantPhoneNumber.setError("Required");
            result = false;
        } else {
            editText_resturantPhoneNumber.setError(null);
        }

        if (TextUtils.isEmpty(editText_resturantCuisineChoose.getText().toString())) {
            editText_resturantCuisineChoose.setError("Required");
            result = false;
        } else {
            editText_resturantCuisineChoose.setError(null);
        }

        if (TextUtils.isEmpty(editText_resturantQuickSearchChoose.getText().toString())) {
            editText_resturantQuickSearchChoose.setError("Required");
            result = false;
        } else {
            editText_resturantQuickSearchChoose.setError(null);
        }



        return result;
    }
    private void setupResturantImagesRecyclerView() {



        recyclerView_foodImages.setNestedScrollingEnabled(false);
        recyclerView_foodImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mPhotosAdapter = new HorizontalImageListSelectorAdapter();
        mPhotosAdapter.setImageURLList(new ArrayList<String>(), this);
        recyclerView_foodImages.setAdapter(mPhotosAdapter);
        recyclerView_foodImages
                .addOnItemTouchListener(new RecyclerTouchListener(this,
                        recyclerView_foodImages,
                        new RecyclerTouchListener.ClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                if (position == mPhotosAdapter.getItemCount() - 1) {
                                    ipickerChoosingPhotos=true;
                                    IPicker.open(AddNewResturantActivity.this, (ArrayList<String>) mPhotosAdapter.getImageURLList());
                                }


                            }

                            @Override
                            public void onLongClick(View view, int position) {
                                if (position == mPhotosAdapter.getItemCount() - 1) {
                                    //do nothing thats image icon
                                } else {


                                }


                            }
                        }));
    }


    private void setupMenuImagesRecyclerView() {


        recyclerView_menuImages.setNestedScrollingEnabled(false);
        recyclerView_menuImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mMenuPhotosAdapter = new HorizontalImageListSelectorAdapter();
        mMenuPhotosAdapter.setImageURLList(new ArrayList<String>(), this);
        recyclerView_menuImages.setAdapter(mMenuPhotosAdapter);
        recyclerView_menuImages
                .addOnItemTouchListener(new RecyclerTouchListener(this,
                        recyclerView_menuImages,
                        new RecyclerTouchListener.ClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                if (position == mMenuPhotosAdapter.getItemCount() - 1) {
                                    ipickerChoosingPhotos=false;
                                    IPicker.open(AddNewResturantActivity.this, (ArrayList<String>) mMenuPhotosAdapter.getImageURLList());
                                }


                            }

                            @Override
                            public void onLongClick(View view, int position) {
                                if (position == mMenuPhotosAdapter.getItemCount() - 1) {
                                    //do nothing thats image icon
                                } else {


                                }


                            }
                        }));
    }

}
