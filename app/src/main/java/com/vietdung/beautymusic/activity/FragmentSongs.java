package com.vietdung.beautymusic.activity;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vietdung.beautymusic.R;
import com.vietdung.beautymusic.adapter.SongAdapter;
import com.vietdung.beautymusic.model.Songs;
import com.vietdung.beautymusic.presenter.PresenterImpFragmentSong;
import com.vietdung.beautymusic.presenter.PresenterLogicFragmentSong;
import com.vietdung.beautymusic.presenter.ViewFragmentSong;
import com.vietdung.beautymusic.until.MusicService;

import java.util.ArrayList;
import java.util.List;

public class FragmentSongs extends Fragment implements ViewFragmentSong {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    // private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = ;
    RecyclerView rv_Songs;
    List<Songs> songsList;
    SongAdapter songAdapter;
    int numberofColumns = 1;
    Intent playIntent;
    boolean musicBound = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_songs, container, false);
        rv_Songs = view.findViewById(R.id.rvSongs);
        //PresenterLogicFragmentSong presenterLogicFragmentSong = new PresenterLogicFragmentSong((PresenterImpFragmentSong) this,getActivity());
        songsList = new ArrayList<>();
        songAdapter = new SongAdapter(songsList, getActivity());
        getData();


//        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
//
//            } else {
//                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
//            }
//
//        } else {
//            getData();
//        }
        rv_Songs.setAdapter(songAdapter);
        rv_Songs.setLayoutManager(new GridLayoutManager(getActivity(), numberofColumns));

        return view;

    }



    public void getData() {
        //songAdapter.notifyDataSetChanged();
        ContentResolver cr = getActivity().getContentResolver();
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


    }

    @Override
    public void displayList() {

    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    {
//                        if (ContextCompat.checkSelfPermission(getActivity(),
//                                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                            getData();
//                            // doStuff();
//                        } else {
//                        }
//                    }
//                    return;
//                }
//        }
//    }
}
