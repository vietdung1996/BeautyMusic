package com.vietdung.beautymusic.until;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.vietdung.beautymusic.activity.MainActivity;
import com.vietdung.beautymusic.activity.PlayMussicActivity;

public class NotificationPrevBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        MusicService musicService = MainActivity.musicService;
        if(musicService!=null){
            musicService.backSong();
        }else{
        }

    }
}
