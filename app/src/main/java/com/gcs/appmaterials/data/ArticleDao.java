package com.gcs.appmaterials.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.gcs.appmaterials.model.Article;

import java.util.List;

@Dao
public interface ArticleDao {

    // insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Article...articles);

    // update
    @Update
    void update(Article...articles);

    // delete
    @Delete
    void delete(Article...articles);

    @Query("DELETE FROM article_table")
    void deleteAll();

    // get
    @Query("SELECT * FROM article_table ORDER BY title")
    LiveData<List<Article>> getAllArticles();

    @Query("SELECT * FROM article_table WHERE id IN (:ids)")
    List<Article> getArticleByIds(int...ids);

}
