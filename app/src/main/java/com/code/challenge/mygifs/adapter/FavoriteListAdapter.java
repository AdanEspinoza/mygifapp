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
import com.code.challenge.mygifs.model.DeleteFavoritesInterface;
import com.code.challenge.mygifs.model.GiphyGifModel;

import java.util.ArrayList;

public class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.CustomViewHolder> {

    private DeleteFavoritesInterface deleteFavoritesInterface;
    private Context context;
    private ArrayList<GiphyGifModel> dataSet;

    public static class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageView favGifImageView;
        ImageButton deleteFavImageButton;
        DeleteFavoritesInterface deleteFavoritesInterface;


        public CustomViewHolder(View itemView) {
            super(itemView);
            this.favGifImageView = (ImageView) itemView.findViewById(R.id.favorites_image_view);
            this.deleteFavImageButton = (ImageButton) itemView.findViewById(R.id.favorites_delete_image_button);
        }
    }

    public FavoriteListAdapter(ArrayList<GiphyGifModel> data, Context context, DeleteFavoritesInterface deleteFavoritesInterface) {
        this.dataSet = data;
        this.context = context;
        this.deleteFavoritesInterface = deleteFavoritesInterface;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fav, parent, false);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int listPosition) {

        ImageView gifImageView = holder.favGifImageView;
        ImageButton favImageButton = holder.deleteFavImageButton;
        String url = dataSet.get(listPosition).getUrl();
        Glide.with(context)
                .load(url)
                .into(gifImageView);
        favImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFavoritesInterface.deleteFavorite(url);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
