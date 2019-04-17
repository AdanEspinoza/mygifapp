package com.code.challenge.mygifs.model;

import android.os.Parcel;
import android.os.Parcelable;

public class GiphyGifModel implements Parcelable {

    public GiphyGifModel() {}

    protected GiphyGifModel(android.os.Parcel in) {
        url = in.readString();
    }

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
    }

    public static final Creator<GiphyGifModel> CREATOR = new Creator<GiphyGifModel>() {
        @Override
        public GiphyGifModel createFromParcel(android.os.Parcel in) {
            return new GiphyGifModel(in);
        }

        @Override
        public GiphyGifModel[] newArray(int size) {
            return new GiphyGifModel[size];
        }
    };
}
