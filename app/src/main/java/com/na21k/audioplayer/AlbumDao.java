package com.na21k.audioplayer;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AlbumDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Album album);

    @Query("select  * from albums")
    LiveData<List<Album>> getAllAlbums();

    @Query("delete from albums")
    void deleteAllAlbums();
}
