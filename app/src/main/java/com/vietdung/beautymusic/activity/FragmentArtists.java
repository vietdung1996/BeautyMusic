package com.vietdung.beautymusic.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.vietdung.beautymusic.R;
import com.vietdung.beautymusic.adapter.AlbumsAdapter;
import com.vietdung.beautymusic.adapter.AuthorAdapter;
import com.vietdung.beautymusic.model.Albums;
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
        View view = inflater.inflate(R.layout.fragments_artists,container,false);
        rv_Author = view.findViewById(R.id.rvAuthor);
        authorList = new ArrayList<>();
        authorAdapter = new AuthorAdapter(authorList,getActivity());
        getData();
        rv_Author.setAdapter(authorAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_Author.setLayoutManager(layoutManager);
        return view;
    }
    private void getData() {
        authorList.add(new Author(1, "Ngày mưa", "MTP"));
        authorList.add(new Author(1, "Ngày mưa", "MTP"));
        authorAdapter.notifyDataSetChanged();

    }
}
