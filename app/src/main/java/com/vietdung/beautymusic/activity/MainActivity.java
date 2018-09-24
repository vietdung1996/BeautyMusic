package com.vietdung.beautymusic.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.vietdung.beautymusic.R;
import com.vietdung.beautymusic.model.Songs;
import com.vietdung.beautymusic.until.MusicService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    ViewPager viewPager;
    TabLayout tabLayout;
    Toolbar tb_main;
    SeekBar seekBarBottom;
    TextView tv_SongBottom, tv_ArtistBottom;
    ImageView iv_Pause;
    public static MusicService musicService;
    List<Songs> songsList;
    public static Intent playIntent;
    boolean musicBound = false;
    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            //get service
            musicService = binder.getService();
            //pass list
            musicService.setList(songsList);

            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }

        } else {
            initView();
        }
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
        addEvents();


    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void addEvents() {
        getData();
        setToolbar();
        updateTimeSong();

    }

    private void setToolbar() {
        setSupportActionBar(tb_main);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    {
                        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        } else {
                            initView();
                        }
                    }
                    return;
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.lvsongs:
                break;
            case R.id.gvsongs:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        tb_main = findViewById(R.id.tbMain);
        seekBarBottom = findViewById(R.id.seekbarBottom);
        tv_SongBottom = findViewById(R.id.tvSongs);
        tv_ArtistBottom = findViewById(R.id.tvArtist);
        iv_Pause = findViewById(R.id.ivPauseBottom);
        songsList = new ArrayList<>();


        FragmentManager manager = getSupportFragmentManager();
        PagerAdapter adapter = new com.vietdung.beautymusic.adapter.PagerAdapter(manager);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabsFromPagerAdapter(adapter);//deprecated
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(musicConnection);
//        Intent intent = new Intent();
//        intent.setAction("CancelNotifi");
//                sendBroadcast(intent);
        // Log.d("destroy"," true");
    }

    @Override
    protected void onResume() {
        //super.onStop();
        super.onResume();
        // Log.d("stop", "onStop: ");
        if (musicService != null) {
            setDisplayMusicBottom();
        }
    }

    public void setDisplayMusicBottom() {
        //int position = musicService.getPosition();
        //Log.d("Namesong", " "+songsList.get(position).getNameSong());
        // Log.d("NameAritis", " "+musicService.getNameArtist());
        if (musicService.isPng()) {
            tv_SongBottom.setText(musicService.getNameSong());
            tv_ArtistBottom.setText(musicService.getNameArtist());
            seekBarBottom.setMax(musicService.getTimeTotal());
            seekBarBottom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    musicService.seekTo(seekBar.getProgress());

                }

            });

        }
    }

    private void updateTimeSong() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (musicService != null && musicBound && musicService.isPng()) {
                    //tv_Time.setText(dateFormat.format(musicService.getCurrentPosition()));
                    seekBarBottom.setProgress(musicService.getCurrentPosition());
                    tv_SongBottom.setText(musicService.getNameSong());
                    tv_ArtistBottom.setText(musicService.getNameArtist());
                    musicService.autoNextSong();
                }
                handler.postDelayed(this, 500);
            }
        }, 100);
    }

    public void getData() {
        //songAdapter.notifyDataSetChanged();
        ContentResolver cr = getApplicationContext().getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = cr.query(musicUri, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ARTIST);
            int albumsColums = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM_ID);

            //add songs to list
            do {
                int thisId = musicCursor.getInt(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                int idALbums = musicCursor.getInt(albumsColums);
                songsList.add(new Songs(thisId, thisTitle, thisArtist, idALbums));

            }
            while (musicCursor.moveToNext());
        }


    }
}
