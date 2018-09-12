package com.vietdung.beautymusic.presenter;

import com.vietdung.beautymusic.model.Songs;

import java.util.List;

public class PresenterLogicFragmentSong implements PresenterImpFragmentSong {
    UserInterator userInterator;
    ViewFragmentSong viewFragmentSong;

    public PresenterLogicFragmentSong(ViewFragmentSong viewFragmentSong) {
        this.viewFragmentSong = viewFragmentSong;

    }

    @Override
    public void getData(List<Songs> songsList) {
       // viewFragmentSong.displayList(songsList);

    }
}
