package com.na21k.audioplayer;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class SongViewModel extends AndroidViewModel {

    private SongDao songDao;
    private UserAudioDatabase audioDatabase;
    private LiveData<List<Song>> allSongs;

    public SongViewModel(@NonNull Application application) {
        super(application);
        audioDatabase = UserAudioDatabase.getDatabase(application);
        songDao = audioDatabase.songDao();
        allSongs = songDao.getAllSongs();
    }

    public void insert(Song song) {
        new InsertAsyncTask(songDao).execute(song);
    }

    public LiveData<List<Song>> getAllSongs() {
        return allSongs;
    }

    public LiveData<List<Song>> getSongsByAlbum(String albumName) {
        return songDao.getSongsByAlbum(albumName);
    }

    public LiveData<List<Song>> getSongsByArtist(String artistName) {
        return songDao.getSongsByArtist(artistName);
    }

    public void clearAllSongs() {
        new ClearAsyncTask(songDao).execute();
    }

    public int getSongsCount() {
        GetCountAsyncTask task = new GetCountAsyncTask(songDao);
        task.execute();
        try {
            return task.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private class InsertAsyncTask extends AsyncTask<Song, Void, Void> {

        SongDao mSongDao;

        InsertAsyncTask(SongDao mSongDao) {
            this.mSongDao = mSongDao;
        }

        @Override
        protected Void doInBackground(Song... songs) {
            mSongDao.insert(songs[0]);
            return null;
        }
    }

    private class ClearAsyncTask extends AsyncTask<Void, Void, Void> {

        SongDao mSongDao;

        ClearAsyncTask(SongDao mSongDao) {
            this.mSongDao = mSongDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mSongDao.deleteAllSongs();
            return null;
        }
    }

    private class GetCountAsyncTask extends AsyncTask<Void, Void, Integer> {

        SongDao mSongDao;

        GetCountAsyncTask(SongDao mSongDao) {
            this.mSongDao = mSongDao;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return mSongDao.getCount();
        }
    }
}
