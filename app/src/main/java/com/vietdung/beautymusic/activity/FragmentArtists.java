package com.vietdung.beautymusic.activity;


import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vietdung.beautymusic.R;
import com.vietdung.beautymusic.adapter.AuthorAdapter;
import com.vietdung.beautymusic.model.Author;

import java.util.ArrayList;
import java.util.List;

public class FragmentArtists extends Fragment {
    RecyclerView rv_Author;
    List<Author> authorList;
    AuthorAdapter authorAdapter;
    int numberofColumns = 2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragments_artists, container, false);
        rv_Author = view.findViewById(R.id.rvAuthor);
        authorList = new ArrayList<>();
        authorAdapter = new AuthorAdapter(authorList, getActivity());
        getData();
        rv_Author.setAdapter(authorAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_Author.setLayoutManager(layoutManager);
        return view;
    }

    private void getData() {

        ContentResolver cr = getActivity().getContentResolver();
        Uri musicUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = cr.query(musicUri, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Artists.ARTIST);
            int idColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Artists._ID);
//            int artistColumn = musicCursor.getColumnIndex
//                    (MediaStore.Audio.Artists.ARTIST);
            int artMusic = musicCursor.getColumnIndex
                    (MediaStore.Audio.Albums.ALBUM_ART);
            //add songs to list
            do {
                int thisId = musicCursor.getInt(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                //  String idArtist = musicCursor.getString(artistColumn);
                String thisArt = musicCursor.getString(artMusic);
                authorList.add(new Author(thisId, thisTitle, thisArt));
            }
            while (musicCursor.moveToNext());
        }
        authorAdapter.notifyDataSetChanged();

    }
}
