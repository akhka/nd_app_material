package com.gcs.appmaterials.viewmodel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gcs.appmaterials.data.ArticleRepository;
import com.gcs.appmaterials.model.Article;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ArticleViewModel extends AndroidViewModel {

    private ArticleRepository repository;
    private LiveData<List<Article>> localArticles;

    public ArticleViewModel(@NonNull Application application) {
        super(application);
        repository = new ArticleRepository(application);
        localArticles = repository.getLocalArticles();
    }

    public LiveData<List<Article>> getLocalArticles(){
        return localArticles;
    }

    public List<Article> getOnlineArticles(){
        return repository.getOnlineArticles();
    }

    public List<Article> getById(int...ids){
        return repository.getArticlesByIds(ids);
    }

    /*class GetByIdTask extends AsyncTask<Void, Void, List<Article>>{

        int[] ids;

        public GetByIdTask(int...ids){
            this.ids = ids;
        }

        @Override
        protected List<Article> doInBackground(Void... voids) {
            return repository.getArticlesByIds(ids);
        }

        @Override
        protected void onPostExecute(List<Article> articles) {
            super.onPostExecute(articles);
        }
    }*/

    public void checkOnlineList(){
        repository.checkOnlineList();
    }

    public boolean checkConnection(){
        return repository.isNetworkConnected();
    }

}
