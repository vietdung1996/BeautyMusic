package com.vietdung.beautymusic.until;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.RemoteViews;

import com.vietdung.beautymusic.R;
import com.vietdung.beautymusic.activity.MainActivity;
import com.vietdung.beautymusic.activity.PlayMussicActivity;
import com.vietdung.beautymusic.adapter.FragmentSongAdapter;
import com.vietdung.beautymusic.model.Songs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    MediaPlayer mediaPlayer;
    List<Songs> songsList;
    int position;
    int idSong;
    private final IBinder musicBind = new MusicBinder();
    //private String songTitle = "";
    private static final int NOTIFY_ID = 1;
    private boolean shuffle = false;
    private boolean repeat = false;
    private Random random;
    Notification notification;

    private boolean isRunBackground = false;
    private boolean isRestartNotifi = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        position = 0;
        idSong = 0;
        mediaPlayer = new MediaPlayer();
        random = new Random();
        // mediaPlayer.setOnCompletionListener(this);
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

//        mediaPlayer.stop();
//        mediaPlayer.release();
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
        idSong = playSong.getId();
        Uri trackUri = ContentUris.withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                idSong);
        //Log.d("Duong dan nhac", " " + trackUri);
        try {
            mediaPlayer.setDataSource(getApplicationContext(), trackUri);

        } catch (Exception e) {
            //Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        notification();
    }

    public void autoNextSong() {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                nextSong();
                if (isRunBackground){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    stopSelf();
                }

            }
        });
    }

    // V
    public void setSong(int songIndex) {
        position = songIndex;
    }

    public void nextSong() {
        if (repeat) {
            position = position;

        } else if (shuffle) {
            int newPosition = position;
            while (newPosition == position) {
                newPosition = random.nextInt(songsList.size());
            }
            position = newPosition;
        } else {
            position++;
            if (position > songsList.size() - 1) {
                position = 0;
            }
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
        }
    }

    public void pauseToPlaySong(){
        if(mediaPlayer.isPlaying()==false){
            mediaPlayer.start();
            if(isRestartNotifi){
                notification();
            }
        }
    }

    public boolean setShuffle() {
        if (shuffle) {
            shuffle = false;
        } else {
            shuffle = true;
        }
        return shuffle;
    }

    public boolean setRepeat() {
        if (repeat) {
            repeat = false;
        } else {
            repeat = true;
        }
        return repeat;
    }



    public int getPosition() {
        return position;
    }

    public int getId() {
        return idSong;
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
        i.putExtra(FragmentSongAdapter.rq_itent_position, position);
        i.putExtra(PlayMussicActivity.rq_notification,3000);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        RemoteViews notificationView = new RemoteViews(this.getPackageName(),R.layout.custom_notification);
        RemoteViews notificationViewSmall = new RemoteViews(this.getPackageName(),R.layout.custom_notification_lock_screen);
        notificationView.setTextViewText(R.id.noti_track_name,songsList.get(position).getNameSong());
        notificationView.setTextViewText(R.id.noti_artist_name,songsList.get(position).getNameAuthor());
        notificationViewSmall.setTextViewText(R.id.tv_track,songsList.get(position).getNameSong());
        notificationViewSmall.setTextViewText(R.id.tv_artist,songsList.get(position).getNameAuthor());

        //Button play
        Intent btnPlayIntent = new Intent(this, NotificationPlayHandler.class);
        PendingIntent btnPlayPendingIntent = PendingIntent.getBroadcast(this, 0, btnPlayIntent, 0);
        notificationView.setOnClickPendingIntent(R.id.noti_play,btnPlayPendingIntent);
        notificationViewSmall.setOnClickPendingIntent(R.id.iv_play,btnPlayPendingIntent);
//        // Back song
        Intent btnBackIntent = new Intent(this, NotificationPrevBroadcast.class);
        PendingIntent btnBackPendingIntent = PendingIntent.getBroadcast(this, 0, btnBackIntent, 0);
        notificationView.setOnClickPendingIntent(R.id.noti_prev,btnBackPendingIntent);
        notificationViewSmall.setOnClickPendingIntent(R.id.iv_back,btnBackPendingIntent);
//        //next song
        Intent btnNextIntent = new Intent(this,NotificationNextBroadcast.class);
        PendingIntent btnNextPendingIntent = PendingIntent.getBroadcast(this, 0, btnNextIntent, 0);
        notificationView.setOnClickPendingIntent(R.id.noti_next,btnNextPendingIntent);
        notificationViewSmall.setOnClickPendingIntent(R.id.iv_next,btnNextPendingIntent);
        //Close notification when music pause
        Intent btnCloseIntent= new Intent(this, NotificationCloseBroadcast.class);
        PendingIntent btnClose = PendingIntent.getBroadcast(this, 0, btnCloseIntent, 0);
        notificationView.setOnClickPendingIntent(R.id.noti_close,btnClose);
        //notificationViewSmall.setOnClickPendingIntent(R.id.iv_next,btnNextPendingIntent);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), (int) System.currentTimeMillis(), i, PendingIntent.FLAG_UPDATE_CURRENT);

        notification = new Notification.Builder(this).build();
        notification.bigContentView = notificationView;
        notification.contentView =  notificationViewSmall;
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        notification.icon = R.drawable.ic_launcher;
        notification.contentIntent = pendingIntent;

        startForeground(NOTIFY_ID, notification);
    }

    public void cancelNotification(){
        if(!isPng()){
            isRestartNotifi = true;
            stopForeground(true);
        }



    }





    public String getNameSong() {
        String nameSong = songsList.get(position).getNameSong();
        return nameSong;
    }

   public String getNameArtist() {
        String nameAritst = songsList.get(position).getNameAuthor();
        return nameAritst;
    }

    public boolean isRunBackground() {
        return isRunBackground;
    }

    public void setRunBackground(boolean runBackground) {
        isRunBackground = runBackground;
    }

    public static class NotificationClose1Broadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            MusicService musicService = MainActivity.musicService;
            if(musicService!=null) {
                musicService.cancelNotification();
            }
        }
    }
}
