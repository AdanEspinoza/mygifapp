package com.code.challenge.mygifs.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.code.challenge.mygifs.R;
import com.code.challenge.mygifs.adapter.FavoriteListAdapter;
import com.code.challenge.mygifs.model.DeleteFavoritesInterface;
import com.code.challenge.mygifs.model.GiphyGifModel;
import com.code.challenge.mygifs.util.Constants;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity implements DeleteFavoritesInterface {

    ArrayList<GiphyGifModel> mGiphyGifModelArrayList;
    private RecyclerView mRecyclerView;
    private static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mEmptyListTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        mGiphyGifModelArrayList = intent.getParcelableArrayListExtra(Constants.FAVORITE_LIST_KEY);

        mEmptyListTextView = (TextView) findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.favorite_recycler_view);

        if(mGiphyGifModelArrayList == null || mGiphyGifModelArrayList.size() == 0) {
            mEmptyListTextView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }else {
            mEmptyListTextView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }


        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new FavoriteListAdapter(mGiphyGifModelArrayList, this, this::deleteFavorite);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Constants.FAVORITE_LIST_KEY, mGiphyGifModelArrayList);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mGiphyGifModelArrayList = new ArrayList<>();
        mGiphyGifModelArrayList = (ArrayList) savedInstanceState.get(Constants.FAVORITE_LIST_KEY);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(Constants.FAVORITE_LIST_KEY, mGiphyGifModelArrayList);
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
    }

    @Override
    public void deleteFavorite(String url) {
        deleteGifFromFavoriteList(url);
        mAdapter.notifyDataSetChanged();
        Toast.makeText(this, getString(R.string.delete_gif_favorites), Toast.LENGTH_SHORT).show();
        if(mGiphyGifModelArrayList.size() == 0) {
            mEmptyListTextView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    private void deleteGifFromFavoriteList(String url) {
        for(int position = 0 ; position < mGiphyGifModelArrayList.size(); position++){
            if(mGiphyGifModelArrayList.get(position).getUrl().contentEquals(url)){
                mGiphyGifModelArrayList.remove(position);
                break;
            }
        }
    }
}
