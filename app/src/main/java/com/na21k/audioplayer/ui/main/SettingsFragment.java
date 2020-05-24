package com.na21k.audioplayer.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.na21k.audioplayer.FetchFilesActivity;
import com.na21k.audioplayer.MainActivity;
import com.na21k.audioplayer.R;

import java.io.File;
import java.util.Objects;


public class SettingsFragment extends Fragment {

    private final int SELECT_FOLDER_REQUEST_CODE = 2;
    private Button refillDatabaseBtn;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
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

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        refillDatabaseBtn = view.findViewById(R.id.refillDatabaseButton);
        refillDatabaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FetchFilesActivity.class);
                startActivity(intent);
            }
        });

        Button chooseFolderBtn = view.findViewById(R.id.chooseMusicFolderButton);
        chooseFolderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                startActivityForResult(intent, SELECT_FOLDER_REQUEST_CODE);
                refillDatabaseBtn.setEnabled(true);
            }
        });

        if (MainActivity.musicPath == null) {
            refillDatabaseBtn.setEnabled(false);
        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_FOLDER_REQUEST_CODE:
                if (data != null) {
                    File file = new File(Objects.requireNonNull(Objects.requireNonNull(data.getData()).getPath()));
                    String path = file.getPath();
                    /*path = path.replace("document", "storage");*/
                    path = path.replace("tree", "storage");
                    path = path.replace("primary", "emulated/0");
                    path = path.replace(':', '/');
                    MainActivity.musicPath = path;
                    Toast.makeText(getContext(), path, Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
