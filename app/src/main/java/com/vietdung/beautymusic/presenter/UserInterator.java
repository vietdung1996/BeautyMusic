package com.vietdung.beautymusic.presenter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.vietdung.beautymusic.model.Songs;

import java.util.ArrayList;
import java.util.List;

public class UserInterator {
    private PresenterImpFragmentSong impFragmentSong;
    Context context;

    public UserInterator( PresenterImpFragmentSong impFragmentSong,Context context){
        this.impFragmentSong = impFragmentSong;
        this.context =context;
    }

    public void getListData(){
        List<Songs> songsList = new ArrayList<>();
        // Context context;
        ContentResolver cr = context.getContentResolver();
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
                int idALbums= musicCursor.getInt(albumsColums);
                songsList.add(new Songs(thisId, thisTitle, thisArtist,idALbums));

            }
            while (musicCursor.moveToNext());
        }
        impFragmentSong.getData(songsList);

    }

}
