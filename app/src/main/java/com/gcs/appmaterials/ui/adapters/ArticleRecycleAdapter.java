package com.gcs.appmaterials.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.gcs.appmaterials.R;
import com.gcs.appmaterials.databinding.ListItemArticleBinding;
import com.gcs.appmaterials.model.Article;
import com.gcs.appmaterials.utils.Handlers;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ArticleRecycleAdapter extends RecyclerView.Adapter<ArticleRecycleAdapter.ViewHolder> {

    private Context context;
    private List<Article> articles = new ArrayList<>();
    private Handlers handler;

    public ArticleRecycleAdapter(Context context, List<Article> articles){
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemArticleBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.list_item_article, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Article article = articles.get(position);
        holder.binding.setArticle(article);
        holder.binding.itemTitle.setText(article.getTitle());
        holder.binding.itemAuthor.setText(article.getAuthor());
        if (article.getThumb() != null){
            try{
                holder.bitmap = new AsyncTask<Void, Void, Bitmap>() {
                    @Override
                    protected Bitmap doInBackground(Void... voids) {
                        try{
                            return Picasso.get().load(articles.get(position).getThumb()).get();
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute().get();
                if (holder.bitmap != null){
                    holder.binding.itemThumbnail.setImageBitmap(holder.bitmap);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        handler = new Handlers(context, article.getId());
        holder.binding.setHandler(handler);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ListItemArticleBinding binding;
        public Bitmap bitmap;

        public ViewHolder(@NonNull ListItemArticleBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
