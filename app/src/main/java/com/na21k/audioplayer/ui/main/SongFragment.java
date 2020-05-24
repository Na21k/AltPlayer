package com.na21k.audioplayer.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.na21k.audioplayer.Album;
import com.na21k.audioplayer.Artist;
import com.na21k.audioplayer.R;
import com.na21k.audioplayer.Song;
import com.na21k.audioplayer.SongViewModel;

import java.util.List;

public class SongFragment extends Fragment {

    private SongListAdapter adapter;
    private SongViewModel viewModel;

    //if is album details (appears whe user selects an item in the album tab)
    private final boolean isAlbumDetails;
    private final Album album;

    //if is artist details (appears whe user selects an item in the artist tab)
    private final boolean isArtistDetails;
    private final Artist artist;

    public SongFragment() {
        isAlbumDetails = false;
        isArtistDetails = false;
        album = null;
        artist = null;
    }

    public SongFragment(Album album) {
        isAlbumDetails = true;
        isArtistDetails = false;
        this.album = album;
        artist = null;
    }

    public SongFragment(Artist artist) {
        isAlbumDetails = false;
        isArtistDetails = true;
        album = null;
        this.artist = artist;
    }

    public static SongFragment newInstance() {
        SongFragment fragment = new SongFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_song, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.listItems);
        adapter = new SongListAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        viewModel = new ViewModelProvider(this).get(SongViewModel.class);

        if (isAlbumDetails) {
            viewModel.getSongsByAlbum(album.getName()).observe(getViewLifecycleOwner(), new Observer<List<Song>>() {
                @Override
                public void onChanged(List<Song> songs) {
                    adapter.setItems(songs);
                }
            });
        } else if (isArtistDetails) {
            viewModel.getSongsByArtist(artist.getName()).observe(getViewLifecycleOwner(), new Observer<List<Song>>() {
                @Override
                public void onChanged(List<Song> songs) {
                    adapter.setItems(songs);
                }
            });
        } else {
            viewModel.getAllSongs().observe(getViewLifecycleOwner(), new Observer<List<Song>>() {
                @Override
                public void onChanged(List<Song> songs) {
                    adapter.setItems(songs);
                }
            });
        }

        return view;
    }
}
