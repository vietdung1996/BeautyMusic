package com.vietdung.beautymusic.until;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;

import com.vietdung.beautymusic.R;
import com.vietdung.beautymusic.activity.PlayMussicActivity;
import com.vietdung.beautymusic.adapter.FragmentSongAdapter;
import com.vietdung.beautymusic.model.Songs;

import java.io.IOException;
import java.util.List;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener
        , MediaPlayer.OnCompletionListener {
    MediaPlayer mediaPlayer;
    List<Songs> songsList;
    int position;
    private final IBinder musicBind = new MusicBinder();
    //private String songTitle = "";
    private static final int NOTIFY_ID = 1;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        position = 0;
        mediaPlayer = new MediaPlayer();
        //playSong();
        initMusicPlay();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {

        return false;
    }


    @Override
    public void onDestroy() {

        mediaPlayer.stop();
        mediaPlayer.release();
        super.onDestroy();
    }


    public void initMusicPlay() {
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    public void setList(List<Songs> songsList) {
        this.songsList = songsList;
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        nextSong();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }



    public void playSong() {
        mediaPlayer.reset();
        Songs playSong = songsList.get(position);
        int idSong = playSong.getId();
        Uri trackUri = ContentUris.withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                idSong);
        Log.d("Duong dan nhac", " " + trackUri);
        try {
            mediaPlayer.setDataSource(getApplicationContext(), trackUri);

        } catch (Exception e) {
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        notification();
        //autoNextSong();
    }

    public void autoNextSong() {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                nextSong();
            }
        });
    }

    // V
    public void setSong(int songIndex) {
        position = songIndex;
    }

    public void nextSong() {
        position++;
        if (position > songsList.size() - 1) {
            position = 0;
        }
        playSong();

    }

    public void backSong() {
        position--;
        if (position < 0) {
            position = songsList.size() - 1;
        }
        playSong();
    }

    public void pauseSong() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
        }
    }

    public int getPosition() {
        return position;

    }

    public int getTimeTotal() {
        return mediaPlayer.getDuration();
    }

    public boolean isPng() {
        return mediaPlayer.isPlaying();
    }

    public void seekTo(int pons) {
        mediaPlayer.seekTo(pons);
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public void notification() {

        Intent i = new Intent(getApplicationContext(), PlayMussicActivity.class);
        i.putExtra(FragmentSongAdapter.rq_itent_position,position);
        //i.putExtra()
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), (int) System.currentTimeMillis(), i, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setContentTitle(songsList.get(position).getNameSong())
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentText(songsList.get(position).getNameAuthor())
                    .setSmallIcon(R.drawable.music)
                    .setContentIntent(pendingIntent)
                    //.setAutoCancel(true)
                    .addAction(R.drawable.back, "Back",pendingIntent)
                    .addAction(R.drawable.pause, "Pause", pendingIntent)
                    .addAction(R.drawable.next, "Next", pendingIntent).build();
        }
        Notification not = builder.build();

        startForeground(NOTIFY_ID, not);
    }

}
