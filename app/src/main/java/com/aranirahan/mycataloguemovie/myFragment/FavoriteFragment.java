package com.aranirahan.mycataloguemovie.myFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aranirahan.mycataloguemovie.R;
import com.aranirahan.mycataloguemovie.adapter.FavoriteAdapter;

import java.util.Objects;

import static com.aranirahan.mycataloguemovie.database.DatabaseContract.CONTENT_URI;

public class FavoriteFragment extends Fragment {

    private Context context;
    RecyclerView rvMain;
    private Cursor cursor;
    private FavoriteAdapter adapter;

    public FavoriteFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Objects.requireNonNull(getActivity()).setTitle(R.string.favorite_movie);
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        rvMain = view.findViewById(R.id.rv_main);
        context = view.getContext();

        adapter = new FavoriteAdapter(cursor);
        rvMain.setLayoutManager(new LinearLayoutManager(context));
        rvMain.setAdapter(adapter);

        new loadData().execute();

        return view;
    }

    @SuppressLint("StaticFieldLeak")
    private class loadData extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            return context.getContentResolver().query(
                    CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);

            FavoriteFragment.this.cursor = cursor;
            adapter.replaceAll(FavoriteFragment.this.cursor);
        }
    }
}
