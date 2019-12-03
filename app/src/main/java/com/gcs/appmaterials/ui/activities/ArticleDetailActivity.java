package com.gcs.appmaterials.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.gcs.appmaterials.R;
import com.gcs.appmaterials.databinding.ActivityArticleDetailBinding;

public class ArticleDetailActivity extends AppCompatActivity {

    ActivityArticleDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_article_detail);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_article_detail);
    }
}
