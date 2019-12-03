package com.gcs.appmaterials.data;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;

import androidx.lifecycle.LiveData;

import com.gcs.appmaterials.model.Article;
import com.gcs.appmaterials.service.ApiService;
import com.gcs.appmaterials.service.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleRepository {

    private Application application;
    private ArticleDao dao;
    private AppDatabase db;
    private LiveData<List<Article>> localArticles;
    private List<Article> onlineArticles = new ArrayList<>();
    private ApiService apiService;

    public ArticleRepository(Application application) {
        this.application = application;
        db = AppDatabase.getInstance(application);
        dao = db.dao();
        localArticles = dao.getAllArticles();
    }

    // get local articles **************************************************************************
    public LiveData<List<Article>> getLocalArticles() {
        return localArticles;
    }
    // =============================================================================================

    // get online data *****************************************************************************
    public List<Article> getOnlineArticles() {
        new OnlineArticlesTask().execute();
        return onlineArticles;
    }

    private class OnlineArticlesTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            checkOnlineList();
            return null;
        }
    }

    private void checkOnlineList() {
        apiService = RetrofitInstance.getApiService();
        Call<List<Article>> call = apiService.getArticles();
        call.enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                if (!response.isSuccessful())
                    return;
                onlineArticles = response.body();
                if (!onlineArticles.isEmpty()) {
                    deleteAllArticles();
                    Article[] articles = new Article[onlineArticles.size()];
                    insertArticles(onlineArticles.toArray(articles));
                }
            }

            @Override
            public void onFailure(Call<List<Article>> call, Throwable t) {
                t.printStackTrace();
                onlineArticles = null;
            }
        });
    }

    // =============================================================================================

    // get articles by ids *************************************************************************
    public List<Article> getArticlesByIds(int... ids) {
        List<Article> articles;
        GetByIdsTask task = new GetByIdsTask(ids);
        try {
            articles = task.execute().get();
            return articles;
        } catch (ExecutionException e) {
            e.printStackTrace();
            articles = null;
            return articles;
        } catch (InterruptedException e) {
            e.printStackTrace();
            articles = null;
            return articles;
        }

    }

    private class GetByIdsTask extends AsyncTask<Void, Void, List<Article>> {

        private int[] ids;
        private List<Article> articles;

        public GetByIdsTask(int... ids) {
            this.ids = ids;
        }

        @Override
        protected List<Article> doInBackground(Void... voids) {
            articles = dao.getArticleByIds(ids);
            return articles;
        }

        @Override
        protected void onPostExecute(List<Article> articles) {
            super.onPostExecute(articles);
        }
    }
    // =============================================================================================


    // insert articles *****************************************************************************
    private void insertArticles(final Article... articles) {
        new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] objects) {
                dao.insert(articles);
                return null;
            }
        }.execute();
    }
    // =============================================================================================

    // delete all articles *************************************************************************
    private void deleteAllArticles() {
        new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] objects) {
                dao.deleteAll();
                return null;
            }
        }.execute();
    }
    // =============================================================================================

    // check connection to internet ****************************************************************
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) application
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT < 23) {
            NetworkInfo ni = cm.getActiveNetworkInfo();

            if (ni != null) {
                return (ni.isConnected() && (ni.getType() == ConnectivityManager.TYPE_WIFI
                        || ni.getType() == ConnectivityManager.TYPE_MOBILE));
            } else {
                Network n = cm.getActiveNetwork();
                if (n != null) {
                    NetworkCapabilities nc = cm.getNetworkCapabilities(n);
                    return (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                            || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI));
                }
            }
        } else {
            Network n = cm.getActiveNetwork();

            if (n != null) {
                NetworkCapabilities nc = cm.getNetworkCapabilities(n);

                return (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI));
            }
        }

        return false;
    }
    // =============================================================================================


}
