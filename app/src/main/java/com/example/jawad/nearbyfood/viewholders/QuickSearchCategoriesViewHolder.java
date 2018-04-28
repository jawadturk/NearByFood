package com.example.jawad.nearbyfood.viewholders;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.jawad.nearbyfood.R;
import com.example.jawad.nearbyfood.pojos.QuickSearchCategories;
import com.squareup.picasso.Picasso;


public class QuickSearchCategoriesViewHolder extends RecyclerView.ViewHolder {

    public TextView textView_categoryTitle;
    public ImageView imageView_categoryImage;
    public CardView cardView;
    public LinearLayout container;

    public QuickSearchCategoriesViewHolder(View itemView) {
        super(itemView);


        textView_categoryTitle = (TextView) itemView.findViewById(R.id.textView_title);
        imageView_categoryImage = (ImageView) itemView.findViewById(R.id.thumbnail);
        cardView = (CardView) itemView.findViewById(R.id.card_view);
        container = (LinearLayout) itemView.findViewById(R.id.container);
    }

    public void bindToPost(QuickSearchCategories category, View.OnClickListener starClickListener) {
        textView_categoryTitle.setText(category.quickSearchCategoryName);


        if (!TextUtils.isEmpty(category.categoryImage))
        {
            Picasso.with(imageView_categoryImage.getContext()).load(category.categoryImage).into(imageView_categoryImage);

        }

    }
}
