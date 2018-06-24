package com.aranirahan.myfavoritemovie.adapter;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aranirahan.myfavoritemovie.BuildConfig;
import com.aranirahan.myfavoritemovie.R;
import com.aranirahan.myfavoritemovie.provider.FavoriteModel;
import com.squareup.picasso.Picasso;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.CategoryViewHolder> {

    private Cursor cursor;

    public FavoriteAdapter(Cursor items) {
        replaceAll(items);
    }

    public void replaceAll(Cursor items) {
        cursor = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_movie, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        cursor.moveToPosition(position);
        holder.bind( new FavoriteModel(cursor));
    }

    @Override
    public int getItemCount() {
        if (cursor == null) return 0;
        else return cursor.getCount();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        ImageView ivPoster;
        TextView tvTitle;
        TextView tvOverview;


        CategoryViewHolder(View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.iv_poster);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvOverview = itemView.findViewById(R.id.tv_overview);
        }

        void bind(final FavoriteModel item) {
            tvTitle.setText(item.getTitle());
            tvOverview.setText(item.getOverview());

            Picasso.get()
                    .load(BuildConfig.BASE_URL_IMAGE + "w154" + item.getPosterPath())
                    .into(ivPoster);
        }
    }
}
