package com.example.jawad.nearbyfood.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jawad.nearbyfood.R;
import com.joooonho.SelectableRoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImagesRecyclerViewAdapter extends RecyclerView.Adapter<ImagesRecyclerViewAdapter.MyViewHolder> {

public static MyClickListener myClickListener;

    public void setMyClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public  void setImagesList(List<String> imagesList) {
        ImagesRecyclerViewAdapter.imagesList = imagesList;
    }

    static   List<String> imagesList = new ArrayList<>();

    public ImagesRecyclerViewAdapter(List<String> images) {
        this.imagesList = images;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.images_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String p = imagesList.get(position);

        Picasso.with(holder.thumbnail.getContext()).load(p).into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        public SelectableRoundedImageView thumbnail;
        public CardView cardView;



        public MyViewHolder(View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.card_view);
            thumbnail =itemView.findViewById(R.id.thumbnail);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    myClickListener.onItemClicked(imagesList.get(getAdapterPosition()));
                }
            });

        }
    }

    public interface MyClickListener{
        void onItemClicked(String img);
    }
}
