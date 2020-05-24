package com.na21k.audioplayer;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "albums", indices = {@Index(value = {"name"})})
public class Album {

    @NotNull
    @PrimaryKey
    private String name;

    public Album(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
