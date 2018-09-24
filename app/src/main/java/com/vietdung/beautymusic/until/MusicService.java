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

import com.vietdung.beautymusic.R;
import com.vietdung.beautymusic.activity.PlayMussicActivity;
import com.vietdung.beautymusic.adapter.FragmentSongAdapter;
import com.vietdung.beautymusic.model.Songs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener
        , MediaPlayer.OnCompletionListener {
    MediaPlayer mediaPlayer;
    List<Songs> songsList;
    List<Songs> songsListAll;
    int position;
    int idSong;
    private final IBinder musicBind = new MusicBinder();
    //private String songTitle = "";
    private static final int NOTIFY_ID = 1;
    private boolean shuffle = false;
    private boolean repeat = false;
    private Random random;

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
        } else {
            mediaPlayer.start();
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

        //Button play
        Intent btnPlayIntent = new Intent(this, NotificationPlayHandler.class);
        PendingIntent btnPlayPendingIntent = PendingIntent.getBroadcast(this, 0, btnPlayIntent, 0);
        // Back song
        Intent btnBackIntent = new Intent(this, NotificationPrevBroadcast.class);
        PendingIntent btnBackPendingIntent = PendingIntent.getBroadcast(this, 0, btnBackIntent, 0);
        //
        Intent btnNextIntent = new Intent(this, NotificationNextBroadcast.class);
        PendingIntent btnNextPendingIntent = PendingIntent.getBroadcast(this, 0, btnNextIntent, 0);
        //i.putExtra()
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), (int) System.currentTimeMillis(), i, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setContentTitle(songsList.get(position).getNameSong())
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentText(songsList.get(position).getNameAuthor())
                    .setSmallIcon(R.drawable.music)
                    .setContentIntent(pendingIntent)
                    //.setLargeIcon()
                    .setAutoCancel(true)
                    .addAction(R.drawable.back, "Back", btnBackPendingIntent)
                    .addAction(R.drawable.pause, "Pause", btnPlayPendingIntent)
                    .addAction(R.drawable.next, "Next", btnNextPendingIntent).build();
        }
        Notification not = builder.build();

        startForeground(NOTIFY_ID, not);
    }

    public void cancelNotifi() {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Log.d("Cancelnoti", "true");
                mediaPlayer.release();

                stopSelf();
                //stopForeground(true);
            }
        });
    }

    public class CancelNotificationBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
//            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mediaPlayer) {
//                    Log.d("Cancelnoti", "true");
//                    mediaPlayer.release();
//
//                }
//            });
        }
    }


    public String getNameSong() {
        String nameSong = "";
        getSongListAll();
        if (songsList.size() > 0) {
            for (int i = 0; i < songsListAll.size(); i++) {
                if (idSong == songsListAll.get(i).getId()) {
                    nameSong = songsListAll.get(i).getNameSong();
                }
            }
            //nameSong = songsList.get(position).getNameSong();
            //nameAritst = songsList.get(position).getNameAuthor();
        }

        return nameSong;

    }

    public String getNameArtist() {
        String nameAritst = "";
        getSongListAll();
        if (songsList.size() > 0) {
            for (int i = 0; i < songsListAll.size(); i++) {
                if (idSong == songsListAll.get(i).getId()) {
                    nameAritst = songsListAll.get(i).getNameAuthor();
                }
            }

        }

        return nameAritst;

    }

    public void getSongListAll() {
        songsListAll = new ArrayList<>();
        ContentResolver cr = getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = cr.query(musicUri, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            int albumsColums = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM_ID);
            //add songs to list
            do {
                int thisId = musicCursor.getInt(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                int idALbums = musicCursor.getInt(albumsColums);
                songsListAll.add(new Songs(thisId, thisTitle, thisArtist, idALbums));
            }
            while (musicCursor.moveToNext());
        }
    }



}
