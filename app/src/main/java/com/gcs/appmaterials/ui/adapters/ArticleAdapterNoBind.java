package com.gcs.appmaterials.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gcs.appmaterials.R;
import com.gcs.appmaterials.model.Article;
import com.gcs.appmaterials.ui.activities.ArticleDetailActivity;
import com.gcs.appmaterials.utils.Constants;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ArticleAdapterNoBind extends RecyclerView.Adapter<ArticleAdapterNoBind.ViewHolder> {

    private Context context;
    private List<Article> articles = new ArrayList<>();

    public ArticleAdapterNoBind(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_article, parent, false);
        final ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) context,
                                vh.thumb,
                                Constants.TRANSATION_NAME);

                SharedPreferences preferences = ((Activity) context).getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor prefEditor = preferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(options);
                prefEditor.putString("options", json);
                prefEditor.commit();

                Intent intent = new Intent(context, ArticleDetailActivity.class);
                intent.putExtra("id", articles.get(vh.getAdapterPosition()).getId());
                context.startActivity(intent);
            }
        });

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Article article = articles.get(position);
        holder.title.setText(article.getTitle());
        holder.author.setText(article.getAuthor());
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
                    holder.thumb.setImageBitmap(holder.bitmap);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView thumb;
        private TextView title;
        private TextView author;

        Bitmap bitmap;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.item_thumbnail);
            title = itemView.findViewById(R.id.item_title);
            author = itemView.findViewById(R.id.item_author);
        }
    }

    public void setList(List<Article> articles){
        this.articles = articles;
    }
}
