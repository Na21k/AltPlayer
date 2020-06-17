package com.na21k.audioplayer;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ArtistViewModel extends AndroidViewModel {

    private ArtistDao artistDao;
    private UserAudioDatabase audioDatabase;
    private LiveData<List<Artist>> allArtists;

    public ArtistViewModel(@NonNull Application application) {
        super(application);
        audioDatabase = UserAudioDatabase.getDatabase(application);
        artistDao = audioDatabase.artistDao();
        allArtists = artistDao.getAllArtists();
    }

    public void insert(Artist artist) {
        new InsertAsyncTask(artistDao).execute(artist);
    }

    public LiveData<List<Artist>> getAllArtists() {
        return allArtists;
    }

    public void clearAllArtists() {
        new ClearAsyncTask(artistDao).execute();
    }

    private static class InsertAsyncTask extends AsyncTask<Artist, Void, Void> {

        private ArtistDao mArtistDao;

        public InsertAsyncTask(ArtistDao mArtistDao) {
            this.mArtistDao = mArtistDao;
        }

        @Override
        protected Void doInBackground(Artist... artists) {
            mArtistDao.insert(artists[0]);
            return null;
        }
    }

    private static class ClearAsyncTask extends AsyncTask<Void, Void, Void> {

        private ArtistDao mArtistDao;

        public ClearAsyncTask(ArtistDao mArtistDao) {
            this.mArtistDao = mArtistDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mArtistDao.deleteAllArtists();
            return null;
        }
    }
}
