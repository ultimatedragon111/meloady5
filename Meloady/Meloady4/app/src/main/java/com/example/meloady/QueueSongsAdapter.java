package com.example.meloady;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QueueSongsAdapter extends RecyclerView.Adapter<QueueSongsAdapter.MyViewHolder>{
    private Context context;
    private List<Song> songs;

    public QueueSongsAdapter(Context context, List<Song> songs) {
        this.context = context;
        this.songs = songs;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.songs_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.songName.setText(songs.get(position).getName());
        holder.artistName.setText(songs.get(position).getArtist());


    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView songName;
        TextView artistName;
        CardView songCard;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            songCard = itemView.findViewById(R.id.songCard);
            artistName = itemView.findViewById(R.id.artistNameRow);
            songName = itemView.findViewById(R.id.songNameRow);
        }
    }
}