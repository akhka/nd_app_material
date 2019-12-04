package com.gcs.appmaterials.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.Toast;

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
        binding.homeArticleRv.setHasFixedSize(true);
        adapter = new ArticleRecycleAdapter(this);
        binding.homeArticleRv.setAdapter(adapter);
        binding.homeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getList();
                binding.homeRefresh.setRefreshing(false);
            }
        });

        viewModel = ViewModelProviders.of(this).get(ArticleViewModel.class);
        viewModel.getLocalArticles().observe(this, new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                setRecyclerViewAdapter(articles);
            }
        });

        getList();
    }

    public void setRecyclerViewAdapter(List<Article> articles){
        adapter.setList(articles);
        adapter.notifyDataSetChanged();
    }

    public void getList(){
        if (viewModel.checkConnection()){
            viewModel.checkOnlineList();
        }
        else {
            Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }
}
