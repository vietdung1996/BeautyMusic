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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vietdung.beautymusic.R;
import com.vietdung.beautymusic.adapter.FragmentArtirstsAdapter;
import com.vietdung.beautymusic.model.Author;

import java.util.ArrayList;
import java.util.List;

public class FragmentArtists extends Fragment {
    RecyclerView rv_Author;
    List<Author> authorList;
    FragmentArtirstsAdapter authorAdapter;
    //int numberofColumns = 2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragments_artists, container, false);
        rv_Author = view.findViewById(R.id.rvAuthor);
        authorList = new ArrayList<>();
        authorAdapter = new FragmentArtirstsAdapter(authorList, getActivity());
        getData();
        rv_Author.setAdapter(authorAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_Author.setLayoutManager(layoutManager);
        return view;
    }

    private void getData() {

        ContentResolver cr = getActivity().getContentResolver();
        Uri musicUri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = cr.query(musicUri, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Artists.ARTIST);
            int idColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Artists._ID);
//            int artistColumn = musicCursor.getColumnIndex
            //     (MediaStore.Audio.Artists.ARTIST);
            int numberSong = musicCursor.getColumnIndex
                    (MediaStore.Audio.Artists.NUMBER_OF_TRACKS);
            Log.d("numberSong", " " + numberSong);
            int numberAlbum = musicCursor.getColumnIndex
                    (MediaStore.Audio.Artists.NUMBER_OF_ALBUMS);
            //add songs to list
            do {
                int thisId = musicCursor.getInt(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisNumberSong = musicCursor.getString(numberSong);
                String thisNumberAlbum = musicCursor.getString(numberAlbum);
                authorList.add(new Author(thisId, thisTitle, thisNumberAlbum, thisNumberSong));
            }
            while (musicCursor.moveToNext());
        }
        authorAdapter.notifyDataSetChanged();

    }
}
