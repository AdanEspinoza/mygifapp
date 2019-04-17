package com.code.challenge.mygifs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.code.challenge.mygifs.R;
import com.code.challenge.mygifs.model.AddFavoritesInterface;
import com.code.challenge.mygifs.model.GiphyGifModel;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.CustomViewHolder> {

    private Context context;
    private ArrayList<GiphyGifModel> dataSet;
    private AddFavoritesInterface addFavoritesInterface;

    public static class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageView gifImageView;
        ImageButton favImageButton;


        public CustomViewHolder(View itemView) {
            super(itemView);
            this.gifImageView = (ImageView) itemView.findViewById(R.id.gif_image_view);
            this.favImageButton = (ImageButton) itemView.findViewById(R.id.favorites_image_button);
        }
    }

    public ListAdapter(ArrayList<GiphyGifModel> data, Context context, AddFavoritesInterface addFavoritesInterface) {
        this.dataSet = data;
        this.context = context;
        this.addFavoritesInterface = addFavoritesInterface;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gif, parent, false);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int listPosition) {

        ImageView gifImageView = holder.gifImageView;
        ImageButton favImageButton = holder.favImageButton;
        String url = dataSet.get(listPosition).getUrl();
        Glide.with(context)
                .load(url)
                .into(gifImageView);
        favImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFavoritesInterface.addFavorite(url);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
