package com.na21k.audioplayer;

import android.media.MediaMetadataRetriever;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.io.File;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FetchFilesActivity extends AppCompatActivity {

    private AlbumViewModel albumViewModel;
    private ArtistViewModel artistViewModel;
    private SongViewModel songViewModel;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_files);

        albumViewModel = new ViewModelProvider(this).get(AlbumViewModel.class);
        artistViewModel = new ViewModelProvider(this).get(ArtistViewModel.class);
        songViewModel = new ViewModelProvider(this).get(SongViewModel.class);
        timer = new Timer();
        timer.schedule(new RefillDatabaseTask(), 70);
    }

    private void refillDatabase() {
        songViewModel.clearAllSongs();
        albumViewModel.clearAllAlbums();
        artistViewModel.clearAllArtists();

        //TODO ask to select a folder
        //File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath());
        //TODO set it firstly in on create in main activity
        File dir = new File(MainActivity.musicPath);
        ArrayList<File> files = new ArrayList<>();
        getAllFiles(dir.getAbsolutePath(), files);

        final MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
        Song newElement;
        int i = 1;
        for (File current : files) {
            metaRetriever.setDataSource(current.getAbsolutePath());
            newElement = new Song(i, metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER),
                    metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE),
                    metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST),
                    metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM),
                    metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE),
                    metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE),
                    current.getAbsolutePath());

            albumViewModel.insert(new Album(newElement.getAlbum()));
            artistViewModel.insert(new Artist(newElement.getArtist()));
            songViewModel.insert(newElement);

            i++;
        }

        finish();
    }

    private void getAllFiles(String directoryName, List<File> files) {
        File directory = new File(directoryName);

        File[] fList = directory.listFiles();
        if (fList != null)
            for (File file : fList) {
                if (file.isFile()) {
                    String mimeType = URLConnection.guessContentTypeFromName(file.getAbsolutePath());
                    if (mimeType != null && mimeType.startsWith("audio")) {
                        files.add(file);
                    }
                } else if (file.isDirectory()) {
                    getAllFiles(file.getAbsolutePath(), files);
                }
            }
    }

    private class RefillDatabaseTask extends TimerTask {

        @Override
        public void run() {
            timer.cancel();
            refillDatabase();
        }
    }
}
