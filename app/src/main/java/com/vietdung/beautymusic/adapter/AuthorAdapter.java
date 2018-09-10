package com.vietdung.beautymusic.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vietdung.beautymusic.R;
import com.vietdung.beautymusic.activity.ActivityAlbums;
import com.vietdung.beautymusic.model.Author;

import java.util.List;

public class AuthorAdapter extends RecyclerView.Adapter<AuthorAdapter.RecyclerviewHolder> {
    List<Author> authorList;
    Activity context;

    public AuthorAdapter(List<Author> authorList, Activity context) {
        this.authorList = authorList;
        this.context = context;
    }

    @NonNull
    @Override
    public AuthorAdapter.RecyclerviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_albums,parent,false);
        return new RecyclerviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorAdapter.RecyclerviewHolder holder, int position) {
        holder.tvAlbumsSong.setText(authorList.get(position).getNameAuthor());
        holder.tvAlbumsAuthorName.setText(authorList.get(position).getNameAuthor());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ActivityAlbums.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return authorList.size();
    }

    public class RecyclerviewHolder extends RecyclerView.ViewHolder{
        ImageView iv_Albums;
        TextView tvAlbumsAuthorName;
        TextView tvAlbumsSong;
        public RecyclerviewHolder(View itemView) {
            super(itemView);
            iv_Albums = itemView.findViewById(R.id.ivAlbums);
            tvAlbumsAuthorName = itemView.findViewById(R.id.tvAlbumsAuthorName);
            tvAlbumsSong = itemView.findViewById(R.id.tvAlbumsSong);
        }
    }
}
