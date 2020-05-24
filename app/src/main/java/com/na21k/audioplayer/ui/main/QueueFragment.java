package com.na21k.audioplayer.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.na21k.audioplayer.MainActivity;
import com.na21k.audioplayer.R;


public class QueueFragment extends Fragment {

    private SongListAdapter adapter;

    public QueueFragment() {
        // Required empty public constructor
    }

    public static QueueFragment newInstance() {
        QueueFragment fragment = new QueueFragment();
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

        View view = inflater.inflate(R.layout.fragment_queue, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.listItems);
        adapter = new SongListAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (MainActivity.queue != null) {
            adapter.setItems(MainActivity.queue);
        }

        return view;
    }
}
