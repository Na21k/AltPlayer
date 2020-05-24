package com.na21k.audioplayer;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SongDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Song song);

    @Query("select  * from songs")
    LiveData<List<Song>> getAllSongs();

    @Query("delete from songs")
    void deleteAllSongs();

    @Query("select * from songs where album = :album")
    LiveData<List<Song>> getSongsByAlbum(String album);

    @Query("select * from songs where artist = :artist")
    LiveData<List<Song>> getSongsByArtist(String artist);

    @Query("select count(*) from songs")
    int getCount();
}
