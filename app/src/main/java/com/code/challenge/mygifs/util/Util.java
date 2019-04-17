package com.code.challenge.mygifs.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.code.challenge.mygifs.model.GiphyGifModel;

import java.util.ArrayList;

public class Util {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    public static String generateSearchUrl(String searchableText){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Constants.BASE_URL);
        stringBuilder.append(Constants.SEARCH_URL);
        stringBuilder.append(Constants.START_PARAMETER);
        stringBuilder.append(Constants.API_KEY_PARAMETER);
        stringBuilder.append(Constants.apiKeyConstant);
        stringBuilder.append(Constants.ADD_PARAMETER);
        stringBuilder.append(Constants.SEARCH_PARAMETER);
        stringBuilder.append(searchableText);

        return stringBuilder.toString();
    }

    public static boolean containsDuplicateValue(ArrayList<GiphyGifModel> list, String duplicatedUrl){
        for(GiphyGifModel giphyGifModel : list) {
            if(giphyGifModel.getUrl().contentEquals(duplicatedUrl)){
                return true;
            }
        }

        return false;
    }
}
