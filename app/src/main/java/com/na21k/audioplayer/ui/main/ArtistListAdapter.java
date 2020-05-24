package com.na21k.audioplayer.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.na21k.audioplayer.Artist;
import com.na21k.audioplayer.R;

import java.util.List;

public class ArtistListAdapter extends RecyclerView.Adapter<ArtistListAdapter.ArtistViewHolder> {

    private final LayoutInflater layoutInflater;
    private Context context;
    private List<Artist> artists;

    public ArtistListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.list_item, parent, false);
        return new ArtistViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        if (artists != null) {
            Artist artist = artists.get(position);
            holder.setData(artist.getName(), position);
            holder.setListener();
        } else {
            holder.itemView.setText("There is no items in this section");
        }
    }

    @Override
    public int getItemCount() {
        if (artists != null) {
            return artists.size();
        }
        return 0;
    }

    public void setItems(List<Artist> artists) {
        this.artists = artists;
        notifyDataSetChanged();
    }

    public class ArtistViewHolder extends RecyclerView.ViewHolder {

        private TextView itemView;
        private int position;

        public ArtistViewHolder(@NonNull View itemView) {
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
                            .replace(R.id.ArtistFragmentContainer, new SongFragment(artists.get(position)), null)
                            .addToBackStack(null).commit();
                }
            });
        }
    }
}
