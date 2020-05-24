package com.na21k.audioplayer.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.na21k.audioplayer.MainActivity;
import com.na21k.audioplayer.R;
import com.na21k.audioplayer.Song;

import java.io.IOException;
import java.util.List;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.SongViewHolder> {

    private final LayoutInflater layoutInflater;
    private Context context;
    private List<Song> songs;

    public SongListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.list_item, parent, false);
        return new SongViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        if (songs != null) {
            Song song = songs.get(position);
            holder.setData(song.getTitle(), position);
            holder.setListener();
        } else {
            holder.itemView.setText("There is no items in this section");
        }
    }

    @Override
    public int getItemCount() {
        if (songs != null) {
            return songs.size();
        }
        return 0;
    }

    public void setItems(List<Song> songs) {
        this.songs = songs;
        notifyDataSetChanged();
    }

    public class SongViewHolder extends RecyclerView.ViewHolder {

        private TextView itemView;
        private int position;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView.findViewById(R.id.listItemText);
        }

        public void setData(String ietmText, int position) {
            itemView.setText(ietmText);
            this.position = position;
        }

        public void setListener() {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        MainActivity.queue = songs;
                        MainActivity.queueCurrent = position;
                        MainActivity.mediaPlayer.reset();
                        MainActivity.mediaPlayer.setDataSource(MainActivity.queue.get(MainActivity.queueCurrent).getPath());
                        MainActivity.mediaPlayer.prepare();
                        MainActivity.mediaPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
