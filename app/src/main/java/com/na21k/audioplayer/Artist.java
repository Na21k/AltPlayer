package com.na21k.audioplayer;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "artists", indices = {@Index(value = {"name"})})
public class Artist {

    @NotNull
    @PrimaryKey
    private String name;

    public Artist(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
