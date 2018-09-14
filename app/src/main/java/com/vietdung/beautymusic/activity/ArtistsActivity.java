package com.vietdung.beautymusic.activity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.vietdung.beautymusic.R;
import com.vietdung.beautymusic.adapter.AlbumsAdapter;
import com.vietdung.beautymusic.adapter.SongAlbum1Adapter;
import com.vietdung.beautymusic.adapter.SongArtistsAdapter;
import com.vietdung.beautymusic.model.Albums;
import com.vietdung.beautymusic.model.Songs;
import com.vietdung.beautymusic.until.MusicService;

import java.util.ArrayList;
import java.util.List;

public class ArtistsActivity extends AppCompatActivity {
    Toolbar tb_Albums;
    ImageView iv_Albums;
    CollapsingToolbarLayout collapsingToolbarLayout;
    RecyclerView rv_Albums;
    List<Songs> songsList;
    List<Albums> albumsList;
    SongArtistsAdapter songArtistsAdapter;
    MusicService musicService;

    int idAlbums = 0;
    String thisTitle = "";
    String thisArtist = "";
    String thisArt = "";
    String thisIdAlbums = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);

        initView();
        addEvents();
    }

    private void addEvents() {

        getAlbums();
        getSongAlbums();
        setToolbar();
    }

    // getAlbum from sdcard
    private void getAlbums() {
        idAlbums = getIntent().getIntExtra(AlbumsAdapter.idALbums, 0);
        ContentResolver cr = getApplication().getContentResolver();
        Uri musicUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = cr.query(musicUri, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Albums.ALBUM);
            int idColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Albums._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Albums.ARTIST);
            int artMusic = musicCursor.getColumnIndex
                    (MediaStore.Audio.Albums.ALBUM_ART);
            //add songs to list
            do {

                int thisId = musicCursor.getInt(idColumn);
                if (idAlbums == thisId) {
                    thisTitle = musicCursor.getString(titleColumn);
                    thisArtist = musicCursor.getString(artistColumn);
                    thisArt = musicCursor.getString(artMusic);
                }

                //albumsList.add(new Albums(thisId, thisTitle, thisArtist,thisArt));
            }
            while (musicCursor.moveToNext());
        }
    }

    // getSong on Album
    private void getSongAlbums() {
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
                int thisAlbums = musicCursor.getInt(albumsColums);
                if (thisAlbums == idAlbums) {
                    int thisId = musicCursor.getInt(idColumn);
                    String thisTitle = musicCursor.getString(titleColumn);
                    String thisArtist = musicCursor.getString(artistColumn);
                    songsList.add(new Songs(thisId, thisTitle, thisArtist,idAlbums));
                }
            }
            while (musicCursor.moveToNext());
        }

        songArtistsAdapter.notifyDataSetChanged();

    }

    private void setToolbar() {
        setSupportActionBar(tb_Albums);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        collapsingToolbarLayout.setTitle(thisTitle);
        Bitmap bitmap = BitmapFactory.decodeFile(thisArt);
        iv_Albums.setImageBitmap(bitmap);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void initView() {
        tb_Albums = findViewById(R.id.tbAlbums);
        iv_Albums = findViewById(R.id.ivActivityAlbums);
        collapsingToolbarLayout = findViewById(R.id.collToolBar);
        rv_Albums = findViewById(R.id.rvAlbums);
        songsList = new ArrayList<>();
        albumsList = new ArrayList<>();
        songArtistsAdapter = new SongArtistsAdapter(songsList, this, idAlbums);
        songArtistsAdapter.notifyDataSetChanged();
        rv_Albums.setAdapter(songArtistsAdapter);
        rv_Albums.setLayoutManager(new LinearLayoutManager(this));
    }

}
