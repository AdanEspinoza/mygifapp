package com.code.challenge.mygifs.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.code.challenge.mygifs.R;
import com.code.challenge.mygifs.adapter.ListAdapter;
import com.code.challenge.mygifs.model.AddFavoritesInterface;
import com.code.challenge.mygifs.model.GiphyGifModel;
import com.code.challenge.mygifs.util.Constants;
import com.code.challenge.mygifs.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AddFavoritesInterface {

    private final String TAG = MainActivity.class.getSimpleName();
    private Button mSearchButton;
    private EditText mSearchEditText;
    private TextView mEmptyListTextView;
    ArrayList<GiphyGifModel> mGiphyGifModelArrayList;
    ArrayList<GiphyGifModel> mFavoriteGiphyGifModelArrayList;

    private RecyclerView mRecyclerView;
    private static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mEmptyListTextView = (TextView) findViewById(R.id.no_results_view);
        mSearchButton =(Button) findViewById(R.id.search_button);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmptyListTextView.setVisibility(View.VISIBLE);
                if(mGiphyGifModelArrayList != null && mAdapter!= null) {
                    mGiphyGifModelArrayList.clear();
                    mAdapter.notifyDataSetChanged();
                }
                searchGifList();
            }
        });
        mSearchEditText = (EditText) findViewById(R.id.search_edit_text);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        if(savedInstanceState == null) {
            mFavoriteGiphyGifModelArrayList = new ArrayList<>();
        }else {
            mFavoriteGiphyGifModelArrayList = (ArrayList) savedInstanceState.get(Constants.FAVORITE_LIST_KEY);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Constants.FAVORITE_LIST_KEY, mFavoriteGiphyGifModelArrayList);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mFavoriteGiphyGifModelArrayList = new ArrayList<>();
        mFavoriteGiphyGifModelArrayList = (ArrayList) savedInstanceState.get(Constants.FAVORITE_LIST_KEY);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_favorites) {
            Intent intent = new Intent(this, FavoritesActivity.class);
            intent.putParcelableArrayListExtra(Constants.FAVORITE_LIST_KEY, mFavoriteGiphyGifModelArrayList);
            startActivityForResult(intent,1 );
        }/* else if (id == R.id.nav_favorites) {


        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                mFavoriteGiphyGifModelArrayList = data.getParcelableArrayListExtra(Constants.FAVORITE_LIST_KEY);

            }
        }
    }

    private void searchGifList(){
        if(Util.isNetworkAvailable(this)){

            String searchText = mSearchEditText.getText().toString();
            if(searchText.isEmpty()){
                Toast.makeText(this, getString(R.string.search_write_something), Toast.LENGTH_SHORT).show();
                return;
            }
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(Util.generateSearchUrl(searchText))
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "Request Failure");
                    mEmptyListTextView.setText(getString(R.string.error_found));
                    mEmptyListTextView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            parseResponse(response);
                        }
                    });
                }
            });
        }
    }

    private void parseResponse(Response response) {
        try {
            String jsonData = response.body().string();
            if (response.isSuccessful()) {
                mGiphyGifModelArrayList = getGifList(jsonData);
                if(mGiphyGifModelArrayList.size() == 0) {
                    mEmptyListTextView.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                }else {
                    mEmptyListTextView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
                mAdapter = new ListAdapter(mGiphyGifModelArrayList, MainActivity.this, this::addFavorite );
                mRecyclerView.setAdapter(mAdapter);
                Log.d(TAG, "Giphy Gif Data from Response: " + mGiphyGifModelArrayList.toString());
            } else {
                Log.d(TAG, "Response Unsuccessful");
            }
        }
        catch (IOException e) {
            Log.e(TAG, "Exception Caught: ", e);
        }
        catch (JSONException e) {
            Log.e(TAG, "Exception Caught: ", e);
        }
    }

    private ArrayList<GiphyGifModel> getGifList(String jsonData) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonData);
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        ArrayList<GiphyGifModel> giphyGifModelArrayList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonGif = jsonArray.getJSONObject(i);
            JSONObject jsonImages = jsonGif.getJSONObject("images");
            JSONObject jsonUrl = jsonImages.getJSONObject("original");

            GiphyGifModel giphyGifModel = new GiphyGifModel();
            giphyGifModel.setUrl(jsonUrl.getString("url"));

            giphyGifModelArrayList.add(giphyGifModel);
            Log.d(TAG, "Gif JSON Data - GIF URL: " + giphyGifModel.getUrl());

        }
        return giphyGifModelArrayList;
    }



    @Override
    public void addFavorite(String url) {
        addGifToFavoriteList(url);
        Toast.makeText(this, getString(R.string.add_gif_favorites), Toast.LENGTH_SHORT).show();
    }

    private void addGifToFavoriteList(String url) {
        for(int position = 0 ; position < mGiphyGifModelArrayList.size(); position++){
            if(mGiphyGifModelArrayList.get(position).getUrl().contentEquals(url)){
                if(!Util.containsDuplicateValue(mFavoriteGiphyGifModelArrayList, url)){
                    mFavoriteGiphyGifModelArrayList.add(mGiphyGifModelArrayList.get(position));
                }
                break;
            }
        }
    }
}
