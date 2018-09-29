package com.vietdung.beautymusic.until;

import android.app.Activity;
import android.app.Application;
import android.app.Service;

public class AppController extends Application {
    private static AppController mInstance;
    private Service musicService;
    private Activity mainActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public Service getMusicService() {
        return musicService;
    }

    public void setMusicService(Service musicService) {
        this.musicService = musicService;
    }

    public static AppController getInstance() {
        return mInstance;
    }

    public Activity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(Activity mainActivity) {
        this.mainActivity = mainActivity;
    }
}
