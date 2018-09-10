package com.vietdung.beautymusic.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vietdung.beautymusic.R;
import com.vietdung.beautymusic.activity.PlayMussic;
import com.vietdung.beautymusic.model.Albums;
import com.vietdung.beautymusic.model.Songs;
import com.vietdung.beautymusic.until.MusicService;

import java.util.List;

public class SongMusicAdapter extends RecyclerView.Adapter<SongMusicAdapter.RecyclerviewHolder> {
    private List<Songs> songsList;
    List<Albums>albumsList;
    Activity context;
    int idAlbums;
    //MusicService musicService;
    public static String rq_itent_album="hello";
//    public static String rq_itent_position="xyz";

    public SongMusicAdapter(List<Songs> songsList, Activity context,int idAlbums) {
        this.songsList = songsList;
        this.context = context;
        this.albumsList = albumsList;
        this.idAlbums = idAlbums;
       // this.musicService =musicService;
    }

    @NonNull
    @Override
    public SongMusicAdapter.RecyclerviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_songs, parent, false);
        return new SongMusicAdapter.RecyclerviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongMusicAdapter.RecyclerviewHolder holder, final int position) {
        holder.tv_NameSong.setText(songsList.get(position).getNameSong());
        holder.tv_NameAuthor.setText(songsList.get(position).getNameAuthor());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                musicService.setSong(position);
//                musicService.playSong();
                Intent i = new Intent(context,PlayMussic.class);
                i.putExtra(SongAdapter.rq_itent_id,songsList.get(position).getId());
                i.putExtra(SongAdapter.rq_itent_position,position);
                i.putExtra(rq_itent_album,idAlbums);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    public class RecyclerviewHolder extends RecyclerView.ViewHolder {
        ImageView iv_Beats;
        TextView tv_NameSong;
        TextView tv_NameAuthor;

        public RecyclerviewHolder(View itemView) {
            super(itemView);
            iv_Beats = itemView.findViewById(R.id.ivBeats);
            tv_NameAuthor = itemView.findViewById(R.id.tvAuthorName);
            tv_NameSong = itemView.findViewById(R.id.tvSongName);
        }
    }
}
