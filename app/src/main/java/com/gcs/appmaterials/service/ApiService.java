package com.gcs.appmaterials.service;

import com.gcs.appmaterials.model.Article;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("xyz-reader-json")
    Call<List<Article>> getArticles();

}
