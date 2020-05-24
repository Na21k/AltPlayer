package com.na21k.audioplayer.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.na21k.audioplayer.Album;
import com.na21k.audioplayer.R;

import java.util.List;

public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.AlbumViewHolder> {

    private final LayoutInflater layoutInflater;
    private Context context;
    private List<Album> albums;

    public AlbumListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.list_item, parent, false);
        return new AlbumViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumListAdapter.AlbumViewHolder holder, int position) {
        if (albums != null) {
            Album album = albums.get(position);
            holder.setData(album.getName(), position);
            holder.setListener();
        } else {
            holder.itemView.setText("There is no items in this section");
        }
    }

    @Override
    public int getItemCount() {
        if (albums != null) {
            return albums.size();
        }
        return 0;
    }

    public void setItems(List<Album> albums) {
        this.albums = albums;
        notifyDataSetChanged();
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder {

        private TextView itemView;
        private int position;

        public AlbumViewHolder(@NonNull View itemView) {
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
                    ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.fragment_open_enter, R.anim.fragment_open_exit)
                            .replace(R.id.AlbumFragmentContainer, new SongFragment(albums.get(position)), null)
                            .addToBackStack(null).commit();
                }
            });
        }
    }
}
