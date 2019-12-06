package com.gcs.appmaterials.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.transition.Fade;
import android.widget.Toast;

import com.gcs.appmaterials.R;
import com.gcs.appmaterials.databinding.ActivityArticleListBinding;
import com.gcs.appmaterials.model.Article;
import com.gcs.appmaterials.ui.adapters.ArticleAdapterNoBind;
import com.gcs.appmaterials.ui.adapters.ArticleRecycleAdapter;
import com.gcs.appmaterials.viewmodel.ArticleViewModel;

import java.util.ArrayList;
import java.util.List;

public class ArticleListActivity extends AppCompatActivity implements ArticleAdapterNoBind.OnItemClickListener {

    private ActivityArticleListBinding binding;
    private ArticleViewModel viewModel;
    private ArticleAdapterNoBind adapter;
    private List<Article> mArticles;

    private Parcelable savedRecyclerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_article_list);

        Fade fade = new Fade();
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);

        if (savedInstanceState == null){
            binding = DataBindingUtil.setContentView(this, R.layout.activity_article_list);
            binding.homeArticleRv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            binding.homeArticleRv.setHasFixedSize(true);
            adapter = new ArticleAdapterNoBind(this);
            adapter.setOItemClickListener(this);
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
                    mArticles = articles;
                }
            });

            getList();
        }

        else {
            savedRecyclerLayout = savedInstanceState.getParcelable("rv_layout");
        }
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


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("rv_layout", binding.homeArticleRv.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, ArticleDetailActivity.class);
        Article article = mArticles.get(position);
        int id = article.getId();
        intent.putExtra("id", id);
        startActivity(intent);
    }
}
