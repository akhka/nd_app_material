package com.gcs.appmaterials.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.transition.Fade;

import com.gcs.appmaterials.R;
import com.gcs.appmaterials.databinding.ActivityArticleDetailBinding;
import com.gcs.appmaterials.model.Article;
import com.gcs.appmaterials.utils.Handlers;
import com.gcs.appmaterials.viewmodel.ArticleViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ArticleDetailActivity extends AppCompatActivity {

    private ActivityArticleDetailBinding binding;
    private ArticleViewModel viewModel;
    private Article article;
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_article_detail);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_article_detail);
        viewModel = ViewModelProviders.of(this).get(ArticleViewModel.class);

        Fade fade = new Fade();
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        fade.excludeTarget(R.id.details_fab, false);
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);

        if (id != -1){
            List<Article> articles = viewModel.getById(id);
            article = articles.get(0);
            binding.setArticle(article);

            Handlers handler = new Handlers(this, article);
            binding.setHandler(handler);

            if (article.getPhoto() != null){
                try{
                    bitmap = new AsyncTask<Void, Void, Bitmap>() {
                        @Override
                        protected Bitmap doInBackground(Void... voids) {
                            try{
                                return Picasso.get().load(article.getPhoto()).get();
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                            return null;
                        }
                    }.execute().get();
                    if (bitmap != null){
                        binding.detailsPhoto.setImageBitmap(bitmap);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        Handlers handler = new Handlers(this, article);
        binding.setHandler(handler);

    }
}
