package com.vietdung.beautymusic.activity;


import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vietdung.beautymusic.R;
import com.vietdung.beautymusic.adapter.AlbumsAdapter;
import com.vietdung.beautymusic.model.Albums;

import java.util.ArrayList;
import java.util.List;

public class FragmentAlbums extends Fragment {
    RecyclerView rv_Albums;
    List<Albums> albumsList;
    AlbumsAdapter albumsAdapter;
    int numberofColumns = 2;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums, container, false);
        rv_Albums = view.findViewById(R.id.rvAlbums);
        albumsList = new ArrayList<>();
        albumsAdapter = new AlbumsAdapter(albumsList, getActivity());
        getData();
        rv_Albums.setAdapter(albumsAdapter);
        rv_Albums.setLayoutManager(new GridLayoutManager(getActivity(), numberofColumns));
        return view;
    }

    private void getData() {


        ContentResolver cr = getActivity().getContentResolver();
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
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                String thisArt = musicCursor.getString(artMusic);
                albumsList.add(new Albums(thisId, thisTitle, thisArtist, thisArt));
            }
            while (musicCursor.moveToNext());
        }
        albumsAdapter.notifyDataSetChanged();

    }
}
