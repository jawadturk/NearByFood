package com.example.jawad.nearbyfood.viewholders;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import android.widget.TextView;

import com.example.jawad.nearbyfood.R;

import com.example.jawad.nearbyfood.pojos.Resturant;
import com.joooonho.SelectableRoundedImageView;
import com.squareup.picasso.Picasso;


public class ResturantsViewHolder extends RecyclerView.ViewHolder {

    public SelectableRoundedImageView thumbnail;
    public TextView resturantName;
    public TextView resturantAddress;
    public TextView resturantCuisineType;
    public TextView resturantRating;
    public CardView cardView;

    public ResturantsViewHolder(View itemView) {
        super(itemView);


        thumbnail = (SelectableRoundedImageView) itemView.findViewById(R.id.imageView_thumbnail);
        resturantName = (TextView) itemView.findViewById(R.id.textView_resturantName);
        resturantAddress = (TextView) itemView.findViewById(R.id.textView_resturantAddress);
        resturantRating = (TextView) itemView.findViewById(R.id.textView_rating);
        resturantCuisineType = (TextView) itemView.findViewById(R.id.textView_resturantCuisines);
        cardView = (CardView) itemView.findViewById(R.id.vendor_cardview);

    }

    public void bindToPost(Resturant resturant, View.OnClickListener starClickListener) {
        resturantName.setText(resturant.resturantName);
        resturantAddress.setText(resturant.resturantAddress);

        if(!resturant.cuisineTypes.isEmpty())
        {
            resturantCuisineType.setText(resturant.cuisineTypes);
        }
        float rating = (float) resturant.resturantRating / resturant.resturantNumberOfReviews;
        resturantRating.setText(Double.toString(rating));


        if (resturant.resturantPhotos.size() > 0) {
            Picasso.with(thumbnail.getContext()).load(resturant.resturantPhotos.get(0)).into(thumbnail);

        }


    }
}
