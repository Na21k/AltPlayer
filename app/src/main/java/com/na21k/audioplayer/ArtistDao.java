package com.na21k.audioplayer;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ArtistDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Artist artist);

    @Query("select  * from artists")
    LiveData<List<Artist>> getAllArtists();

    @Query("delete from artists")
    void deleteAllArtists();
}
