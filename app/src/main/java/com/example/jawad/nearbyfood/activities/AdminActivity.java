package com.example.jawad.nearbyfood.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.jawad.nearbyfood.R;
import com.example.jawad.nearbyfood.helper.IntentExtra;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdminActivity extends AppCompatActivity {

    @BindView(R.id.button_quickSearchCategory)
    Button button_addQuickSearchCategory;

    @BindView(R.id.button_addCuisineCategory)
    Button button_addCuisineCategory;

    @BindView(R.id.button_addNewResturant)
    Button button_addNewResturant;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ButterKnife.bind(this);

        button_addQuickSearchCategory.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                redirectToAddQuickSearch(IntentExtra.QuickSearchOrCuisine.typeQuickSearch);
            }
        });

        button_addCuisineCategory.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                redirectToAddQuickSearch(IntentExtra.QuickSearchOrCuisine.typeCuisine);
            }
        });

        button_addNewResturant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this,AddNewResturantActivity.class);
                startActivity(intent);
            }
        });

        setUpToolBar("Admin");

    }

    private void redirectToAddQuickSearch(String type) {
        Intent intent = new Intent(AdminActivity.this,AddQuickSearch_CuisineCategoryActivity.class);
        intent.putExtra(IntentExtra.QuickSearchOrCuisine.key,type);
        startActivity(intent);

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
