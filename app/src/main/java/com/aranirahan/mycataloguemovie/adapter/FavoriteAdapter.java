package com.aranirahan.mycataloguemovie.adapter;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aranirahan.mycataloguemovie.BuildConfig;
import com.aranirahan.mycataloguemovie.R;
import com.aranirahan.mycataloguemovie.model.ResultsItem;
import com.aranirahan.mycataloguemovie.myActivity.DetailActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private Cursor list;

    public FavoriteAdapter(Cursor items) {
        replaceAll(items);
    }

    public void replaceAll(Cursor items) {
        list = items;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_movie, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.getCount();
    }

    private ResultsItem getItem(int position) {
        if (!list.moveToPosition(position)) {
            throw new IllegalStateException("Position invalid!");
        }
        return new ResultsItem(list);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_poster)
        ImageView img_poster;

        @BindView(R.id.tv_title)
        TextView tv_title;

        @BindView(R.id.tv_overview)
        TextView tv_overview;

        @BindView(R.id.tv_release_date)
        TextView tv_release_date;

        @BindView(R.id.btn_detail)
        Button btn_detail;

        @BindView(R.id.btn_share)
        Button btn_share;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final ResultsItem item) {
            tv_title.setText(item.getTitle());
            tv_overview.setText(item.getOverview());
            tv_release_date.setText(item.getReleaseDate());
            Glide.with(itemView.getContext())
                    .load(BuildConfig.BASE_URL_IMAGE + "w154" + item.getPosterPath())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.ic_launcher_background)
                            .centerCrop()
                    )
                    .into(img_poster);

            btn_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), DetailActivity.class);
                    intent.putExtra(DetailActivity.KEY_ITEM, new Gson().toJson(item));
                    itemView.getContext().startActivity(intent);
                }
            });

            btn_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TITLE, item.getTitle());
                    intent.putExtra(Intent.EXTRA_SUBJECT, item.getTitle());
                    intent.putExtra(Intent.EXTRA_TEXT, item.getTitle() + "\n\n" + item.getOverview());
                    itemView.getContext().startActivity(Intent.createChooser(intent, itemView.getResources().getString(R.string.share)));
                }
            });
        }
    }
}
