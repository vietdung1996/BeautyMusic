package com.vietdung.beautymusic.database;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.vietdung.beautymusic.activity.PlayMussicActivity;
import com.vietdung.beautymusic.adapter.SongAlbum1Adapter;
import com.vietdung.beautymusic.adapter.SongArtistsAdapter;
import com.vietdung.beautymusic.model.Songs;

import java.util.ArrayList;
import java.util.List;

public class getDataSdCard {
    Activity activity;
    int screen = 0;


    public getDataSdCard(Activity activity, int screen) {
        this.activity = activity;
        this.screen = screen;
    }

    public List<Songs> getData() {
        List<Songs> songsList= new ArrayList<>();
        if (screen == 111) {
            ContentResolver cr = activity.getContentResolver();
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
                    songsList.add(new Songs(thisId, thisTitle, thisArtist, idALbums));
                }
                while (musicCursor.moveToNext());
            }
            // songAdapter.notifyDataSetChanged();
        }// getSong Album
        else if (screen == 123) {
            int idAlbums = activity.getIntent().getIntExtra(PlayMussicActivity.rq_screen_idalbums, -1);
            ContentResolver cr = activity.getContentResolver();
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
                        songsList.add(new Songs(thisId, thisTitle, thisArtist, idAlbums));
                    }
                }
                while (musicCursor.moveToNext());
            }


        } else if (screen == 321) {
            int idArtist = activity.getIntent().getIntExtra(PlayMussicActivity.rq_screen_idartist, -1);
            ContentResolver cr = activity.getContentResolver();
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
                        (MediaStore.Audio.Media.ARTIST_ID);
                //add songs to list
                do {
                    int thisArtistID = musicCursor.getInt(albumsColums);
                    if (thisArtistID == idArtist) {
                        int thisId = musicCursor.getInt(idColumn);
                        String thisTitle = musicCursor.getString(titleColumn);
                        String thisArtist = musicCursor.getString(artistColumn);
                        songsList.add(new Songs(thisId, thisTitle, thisArtist, idArtist));
                    }
                }
                while (musicCursor.moveToNext());
            }

        } else {
            ContentResolver cr = activity.getContentResolver();
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
                    songsList.add(new Songs(thisId, thisTitle, thisArtist, idALbums));
                }
                while (musicCursor.moveToNext());
            }
        }
        return songsList;
    }
}
