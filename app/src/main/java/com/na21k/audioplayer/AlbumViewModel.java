package com.na21k.audioplayer;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class AlbumViewModel extends AndroidViewModel {

    private AlbumDao albumDao;
    private UserAudioDatabase audioDatabase;
    private LiveData<List<Album>> allAlbums;

    public AlbumViewModel(@NonNull Application application) {
        super(application);
        audioDatabase = UserAudioDatabase.getDatabase(application);
        albumDao = audioDatabase.albumDao();
        allAlbums = albumDao.getAllAlbums();
    }

    public void insert(Album album) {
        new InsertAsyncTask(albumDao).execute(album);
    }

    public LiveData<List<Album>> getAllAlbums() {
        return allAlbums;
    }

    public void clearAllAlbums() {
        new ClearAsyncTask(albumDao).execute();
    }

    private static class InsertAsyncTask extends AsyncTask<Album, Void, Void> {

        private AlbumDao mAlbumDao;

        InsertAsyncTask(AlbumDao mAlbumDao) {
            this.mAlbumDao = mAlbumDao;
        }

        @Override
        protected Void doInBackground(Album... albums) {
            mAlbumDao.insert(albums[0]);
            return null;
        }
    }

    private static class ClearAsyncTask extends AsyncTask<Void, Void, Void> {

        private AlbumDao mAlbumDao;

        public ClearAsyncTask(AlbumDao mAlbumDao) {
            this.mAlbumDao = mAlbumDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAlbumDao.deleteAllAlbums();
            return null;
        }
    }
}
