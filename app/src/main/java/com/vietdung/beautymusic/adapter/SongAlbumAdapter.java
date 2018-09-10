package com.vietdung.beautymusic.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vietdung.beautymusic.R;
import com.vietdung.beautymusic.model.Songs;

import java.util.List;

public class SongAlbumAdapter extends BaseAdapter {
    List<Songs>songsList;
    Context context;

    public SongAlbumAdapter(List<Songs> songsList,  Context context) {
        this.songsList = songsList;
        this.context =context;
    }

    @Override
    public int getCount() {
        return songsList.size();
    }

    @Override
    public Object getItem(int i) {
        return songsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return songsList.get(i).getId();
    }

    public class ViewHolder{
        ImageView iv_Beats;
        TextView tv_NameSong;
        TextView tv_NameAuthor;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view==null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.custom_songs,null);
            viewHolder.iv_Beats = view.findViewById(R.id.ivBeats);
            viewHolder.tv_NameSong = view.findViewById(R.id.tvSongName);
            viewHolder.tv_NameAuthor = view.findViewById(R.id.tvAuthorName);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv_NameSong.setText(songsList.get(i).getNameSong());
        viewHolder.tv_NameAuthor.setText(songsList.get(i).getNameAuthor());
        return view;
    }
}
