package com.na21k.audioplayer;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Album.class, Artist.class, Song.class}, version = 4)
public abstract class UserAudioDatabase extends RoomDatabase {

    public abstract AlbumDao albumDao();

    public abstract ArtistDao artistDao();

    public abstract SongDao songDao();

    private static volatile UserAudioDatabase audioDatabaseInstance;

    public static UserAudioDatabase getDatabase(final Context context) {
        if (audioDatabaseInstance == null) {
            synchronized (UserAudioDatabase.class) {
                if (audioDatabaseInstance == null) {
                    audioDatabaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                            UserAudioDatabase.class, "user_audio_database").build();
                }
            }
        }
        return audioDatabaseInstance;
    }
}
