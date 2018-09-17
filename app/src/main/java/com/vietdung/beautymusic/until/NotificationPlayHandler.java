package com.vietdung.beautymusic.until;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.vietdung.beautymusic.activity.MainActivity;
import com.vietdung.beautymusic.activity.PlayMussicActivity;

public class NotificationPlayHandler extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        MusicService musicService = MainActivity.musicService;
        musicService.pauseSong();

    }
}
