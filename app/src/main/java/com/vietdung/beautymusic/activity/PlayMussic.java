package com.vietdung.beautymusic.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.vietdung.beautymusic.R;
import com.vietdung.beautymusic.adapter.SongAdapter;
import com.vietdung.beautymusic.adapter.SongAlbumAdapter;
import com.vietdung.beautymusic.adapter.SongMusicAdapter;
import com.vietdung.beautymusic.model.Songs;
import com.vietdung.beautymusic.until.MusicService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlayMussic extends AppCompatActivity {
    CircleImageView iv_CircleMussic;
    ImageButton btn_Back;
    ImageButton btn_Next;
    ImageView iv_Pause;
    ImageView iv_Play;
    ObjectAnimator animator;
    TextView tv_Time, tv_TimeTotal;
    SeekBar seekBar;
    ImageView iv_Repeat, iv_Suffle;
    Toolbar tb_PlayMusic;
    List<Songs> songsList;
    SongAlbumAdapter songAdapter;
    ListView rv_Song;
    int id = 0;
    MusicService musicService;
    Intent playIntent;
    boolean musicBound = false;

    int position = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_mussic);

        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            //startService(playIntent);
        }
        initView();
        addEvents();

    }

    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            //get service
            musicService = binder.getService();
            //pass list
            musicService.setList(songsList);
            songPicked();
            musicBound = true;
            setToolbar();
            updateTimeSong();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

    }

    protected void onDestroy() {
        stopService(playIntent);
        musicService = null;
        super.onDestroy();
    }

    public void songPicked() {
        musicService.setSong(position);
        musicService.playSong();
    }

    private void addEvents() {
        getSonglist();
        getPosition();
        setButtonClick();
    }

    private void setButtonClick() {
        iv_Pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicService.pauseSong();
                iv_Pause.setVisibility(View.INVISIBLE);
                iv_Play.setVisibility(View.VISIBLE);
                setPlayingMusic();
            }
        });
        iv_Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicService.pauseSong();
                iv_Pause.setVisibility(View.VISIBLE);
                iv_Play.setVisibility(View.INVISIBLE);
                setPlayingMusic();
            }
        });
        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_Pause.setVisibility(View.VISIBLE);
                iv_Play.setVisibility(View.INVISIBLE);
                musicService.backSong();
                position = musicService.getPosition();
                setPlayingMusic();
            }
        });
        btn_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_Pause.setVisibility(View.VISIBLE);
                iv_Play.setVisibility(View.INVISIBLE);
                musicService.nextSong();
                position = musicService.getPosition();
                setPlayingMusic();
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                musicService.seekTo(seekBar.getProgress()


                );

            }
        });
        rv_Song.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                musicService.setSong(i);
                musicService.playSong();
            }
        });


    }

    private void getPosition() {
        if (position < songsList.size()) {
            position = getIntent().getIntExtra(SongAdapter.rq_itent_position, 0);
            id = getIntent().getIntExtra(SongAdapter.rq_itent_id, 0);
            Log.d("ID", " " + position + " " + id);
        }

    }

    private void getSonglist() {
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
            //add songs to list
            do {
                int thisId = musicCursor.getInt(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                songsList.add(new Songs(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());
        }
        songAdapter.notifyDataSetChanged();
    }

    private void setToolbar() {
        setSupportActionBar(tb_PlayMusic);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setPlayingMusic();
    }

    private void setPlayingMusic() {
        getSupportActionBar().setTitle(songsList.get(musicService.getPosition()).getNameSong());
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        if (musicService != null && musicBound && musicService.isPng()) {
            tv_TimeTotal.setText(dateFormat.format(musicService.getTimeTotal()));
            seekBar.setMax(musicService.getTimeTotal());
        }

    }

    private void updateTimeSong(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
                //tv_Time.setText(dateFormat.format(musicService.getCurrentPosition()));

                if (musicService != null && musicBound && musicService.isPng()) {
                    tv_Time.setText(dateFormat.format(musicService.getCurrentPosition()));
                    seekBar.setProgress(musicService.getCurrentPosition());
                    musicService.autoNextSong();
                    setPlayingMusic();
                }


                handler.postDelayed(this,500);
            }
        },100);
    }

    private void initView() {
        iv_CircleMussic = findViewById(R.id.ivCircleMusic);
        btn_Back = findViewById(R.id.ibBack);
        btn_Next = findViewById(R.id.ibNext);
        tv_Time = findViewById(R.id.tvTime);
        tv_TimeTotal = findViewById(R.id.tvTimeTotal);
        iv_Suffle = findViewById(R.id.ivSuffle);
        iv_Repeat = findViewById(R.id.ivRepeat);
        iv_Pause = findViewById(R.id.ivPause);
        iv_Play = findViewById(R.id.ivStart);
        iv_Play.setVisibility(View.INVISIBLE);
        tb_PlayMusic = findViewById(R.id.tbPlayMussic);
        rv_Song = findViewById(R.id.rvMusicSong);
        seekBar = findViewById(R.id.seekbar);
        musicService = new MusicService();
        songsList = new ArrayList<>();
        songAdapter = new SongAlbumAdapter(songsList, this);
        rv_Song.setAdapter(songAdapter);
        animator = ObjectAnimator.ofFloat(iv_CircleMussic, "rotation", 0f, 360f);
        animator.setDuration(10000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setInterpolator(new LinearInterpolator());
    }

}
