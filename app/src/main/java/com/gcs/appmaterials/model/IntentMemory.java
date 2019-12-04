package com.gcs.appmaterials.model;

import android.content.Intent;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "intent")
public class IntentMemory {

    @PrimaryKey
    @ColumnInfo
    private int id;

    @ColumnInfo
    private Intent intent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }
}
