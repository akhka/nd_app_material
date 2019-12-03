package com.gcs.appmaterials.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.gcs.appmaterials.model.Article;

@Database(entities = {Article.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;
    public abstract ArticleDao dao();

    public static synchronized AppDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "article_db")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }


    // Here create initials when db first created
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            //new PopulateDBTask(instance).execute();

        }
    };

    private static class PopulateDBTask extends AsyncTask {

        private ArticleDao dao;

        private PopulateDBTask(AppDatabase db){
            dao = db.dao();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            // Here add the data needed to be populated in the db
            return null;
        }
    }

}
