package com.example.jawad.nearbyfood.viewholders;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jawad.nearbyfood.R;
import com.example.jawad.nearbyfood.pojos.CuisineCategories;
import com.example.jawad.nearbyfood.pojos.QuickSearchCategories;
import com.joooonho.SelectableRoundedImageView;
import com.squareup.picasso.Picasso;


public class CuisineCategoriesViewHolder extends RecyclerView.ViewHolder {

    public TextView textView_categoryTitle;
    public SelectableRoundedImageView imageView_categoryImage;


    public CuisineCategoriesViewHolder(View itemView) {
        super(itemView);


        textView_categoryTitle = (TextView) itemView.findViewById(R.id.textView_title);
        imageView_categoryImage = (SelectableRoundedImageView) itemView.findViewById(R.id.thumbnail);

    }

    public void bindToPost(CuisineCategories category, View.OnClickListener starClickListener) {
        textView_categoryTitle.setText(category.cuisineCategoryName);


        if (!TextUtils.isEmpty(category.categoryImage))
        {
            Picasso.with(imageView_categoryImage.getContext()).load(category.categoryImage).into(imageView_categoryImage);

        }

    }
}
