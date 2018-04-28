package com.example.jawad.nearbyfood.adapters;


import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.jawad.nearbyfood.R;

import java.util.ArrayList;
import java.util.List;

public class HorizontalImageListSelectorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    public List<String> getImageURLList() {
        return imageURLList;
    }

    List<String> imageURLList = new ArrayList<>();

    Context context;

    public HorizontalImageListSelectorAdapter() {
    }


    public void setImageURLList(List<String> imageURLList, Context context) {
        this.imageURLList = imageURLList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_FOOTER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_icon_captruing_footer_item, parent, false);
            return new FooterViewHolder(v);
        } else if (viewType == TYPE_ITEM) {
            View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.horizontal_images_item_layout, parent, false);

            return new GalleryImageListViewHolder(view);
        }
        return null;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point mSize = new Point();
        display.getSize(mSize);

        if (holder instanceof FooterViewHolder) {

            FooterViewHolder footerHolder = (FooterViewHolder) holder;
            footerHolder.imageCaptureIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Clicked Footer", Toast.LENGTH_LONG).show();
                }
            });
        } else {


            GalleryImageListViewHolder galleryImageListViewHolder = (GalleryImageListViewHolder) holder;
            if (!imageURLList.isEmpty() && imageURLList.size() > position) {
                String photoUri = imageURLList.get(position);

                Glide.with(galleryImageListViewHolder.imageView_carImage.getContext())
                        .load(photoUri)
                        .into(galleryImageListViewHolder.imageView_carImage);
            }


        }
    }


    @Override
    public int getItemCount() {
        return imageURLList.size() + 1;
    }

    //    need to override this method
    @Override
    public int getItemViewType(int position) {
        if (isPositionFooter(position)) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionFooter(int position) {
        return position == imageURLList.size();
    }

    private class GalleryImageListViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView_carImage;
        ImageButton imageButton_deleteImage;

        public GalleryImageListViewHolder(View itemView) {
            super(itemView);

            imageView_carImage = (ImageView) itemView.findViewById(R.id.imageView_carImage);
            imageButton_deleteImage = (ImageButton) itemView.findViewById(R.id.deleteImage);

            imageButton_deleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItem(getAdapterPosition());
                }
            });
        }


    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        LinearLayout imageCaptureIcon;

        public FooterViewHolder(View itemView) {
            super(itemView);
            this.imageCaptureIcon = (LinearLayout) itemView.findViewById(R.id.image_capture_button_layout);
        }
    }

    // This removes the data from our Dataset and Updates the Recycler View.
    public void removeItem(int index) {

        //  int currPosition = data.indexOf(infoData);
        imageURLList.remove(index);
        notifyItemRemoved(index);
    }

    // This method adds(duplicates) a Object (item ) to our Data set as well as Recycler View.
    public void addItem(String imageuri) {

        imageURLList.add(imageURLList.size(), imageuri);
        notifyItemInserted(imageURLList.size());
    }


}
