package com.gcs.appmaterials.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.transition.Fade;
import android.view.View;

import androidx.core.app.ActivityOptionsCompat;
import androidx.databinding.DataBindingUtil;

import com.gcs.appmaterials.R;
import com.gcs.appmaterials.databinding.ListItemArticleBinding;
import com.gcs.appmaterials.ui.activities.ArticleDetailActivity;
import com.gcs.appmaterials.viewmodel.ArticleViewModel;

public class Handlers {

    private Context context;
    private ArticleViewModel viewModel;
    private int id;

    public Handlers(Context context, int id){
        this.context = context;
        this.id = id;
    }

    public void onClickItem(View view){

        ListItemArticleBinding binding = DataBindingUtil.setContentView((Activity) context, R.layout.list_item_article);

        Fade fade = new Fade();
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        ((Activity) context).getWindow().setEnterTransition(fade);
        ((Activity) context).getWindow().setExitTransition(fade);

        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation((Activity) context,
                        binding.itemThumbnail,
                        Constants.TRANSATION_NAME);

        Intent intent = new Intent(context, ArticleDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent, options.toBundle());

    }
}
