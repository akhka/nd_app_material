package com.gcs.appmaterials.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;

import com.gcs.appmaterials.R;
import com.gcs.appmaterials.databinding.ActivityArticleListBinding;
import com.gcs.appmaterials.model.Article;
import com.gcs.appmaterials.ui.adapters.ArticleRecycleAdapter;
import com.gcs.appmaterials.viewmodel.ArticleViewModel;

import java.util.ArrayList;
import java.util.List;

public class ArticleListActivity extends AppCompatActivity {

    private ActivityArticleListBinding binding;
    private ArticleViewModel viewModel;
    private ArticleRecycleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_article_list);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_article_list);
        binding.homeArticleRv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        //binding.homeArticleRv.setHasFixedSize(true);

        viewModel = ViewModelProviders.of(this).get(ArticleViewModel.class);

        if (viewModel.checkConnection()){
            List<Article> onlineList = new ArrayList<>();
            onlineList = viewModel.getOnlineArticles();
            if (onlineList.isEmpty()){
                viewModel.getLocalArticles().observe(this, new Observer<List<Article>>() {
                    @Override
                    public void onChanged(List<Article> articles) {
                        setRecyclerViewAdapter(articles);
                    }
                });
            }
            else {
                setRecyclerViewAdapter(onlineList);
            }
        }
        else {
            viewModel.getLocalArticles().observe(this, new Observer<List<Article>>() {
                @Override
                public void onChanged(List<Article> articles) {
                    setRecyclerViewAdapter(articles);
                }
            });
        }
    }

    public void setRecyclerViewAdapter(List<Article> articles){
        adapter = new ArticleRecycleAdapter(ArticleListActivity.this, articles);
        binding.homeArticleRv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
