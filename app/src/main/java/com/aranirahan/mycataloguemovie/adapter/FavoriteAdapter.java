package com.aranirahan.mycataloguemovie.adapter;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aranirahan.mycataloguemovie.BuildConfig;
import com.aranirahan.mycataloguemovie.R;
import com.aranirahan.mycataloguemovie.model.sub.ResultsItem;
import com.aranirahan.mycataloguemovie.myActivity.DetailActivity;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.CategoryViewHolder> {

    private Cursor listResultsItem;

    public FavoriteAdapter(Cursor items) {
        replaceListResultsItem(items);
    }

    public void replaceListResultsItem(Cursor items) {
        listResultsItem = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_movie, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        listResultsItem.moveToPosition(position);
        holder.bind(new ResultsItem(listResultsItem));
    }

    @Override
    public int getItemCount() {
        if (listResultsItem != null) {
            return listResultsItem.getCount();
        } else {
            return 0;
        }
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPoster;
        TextView tvTitle;
        TextView tvOverview;
        TextView tvReleaseDate;
        Button btnDetail;
        Button btnShare;

        CategoryViewHolder(View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.img_poster);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvOverview = itemView.findViewById(R.id.tv_overview);
            tvReleaseDate = itemView.findViewById(R.id.tv_release_date);
            btnDetail = itemView.findViewById(R.id.btn_detail);
            btnShare = itemView.findViewById(R.id.btn_share);
        }

        void bind(final ResultsItem item) {
            tvTitle.setText(item.getTitle());
            tvOverview.setText(item.getOverview());
            tvReleaseDate.setText(item.getReleaseDate());

            Picasso.get()
                    .load(BuildConfig.BASE_URL_IMAGE + "w154" + item.getPosterPath())
                    .into(imgPoster);

            setOnClick(btnDetail, item, itemView);
            setOnClick(btnShare, item, itemView);
        }
    }

    private void setOnClick(final Button btn, final ResultsItem item, final View itemView) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_detail:
                        Intent intentDetail = new Intent(itemView.getContext(), DetailActivity.class);
                        intentDetail.putExtra(DetailActivity.KEY_ITEM, new Gson().toJson(item));
                        itemView.getContext().startActivity(intentDetail);
                        break;
                    case R.id.btn_share:
                        Intent intentShare = new Intent(Intent.ACTION_SEND);
                        intentShare.setType("text/plain");
                        intentShare.putExtra(Intent.EXTRA_TEXT,
                                item.getTitle().toUpperCase()
                                        + "\n\n" + item.getOverview());
                        itemView.getContext().startActivity(Intent.createChooser(intentShare,
                                itemView.getResources().getString(R.string.share)));
                        break;
                }
            }

        });
    }
}
