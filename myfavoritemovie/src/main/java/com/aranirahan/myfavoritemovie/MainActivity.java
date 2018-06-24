package com.aranirahan.myfavoritemovie;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.aranirahan.myfavoritemovie.adapter.FavoriteAdapter;

import static com.aranirahan.myfavoritemovie.database.DatabaseContract.CONTENT_URI;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvFavorite;

    private Cursor cursor;
    private FavoriteAdapter favoriteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvFavorite = findViewById(R.id.rv_favorite);

        setupList();
        new loadDataAsync().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new loadDataAsync().execute();
    }

    private void setupList() {
        favoriteAdapter = new FavoriteAdapter(cursor);
        rvFavorite.setLayoutManager(new LinearLayoutManager(this));
        rvFavorite.addItemDecoration(new DividerItemDecoration(rvFavorite.getContext(), DividerItemDecoration.VERTICAL));
        rvFavorite.setAdapter(favoriteAdapter);
    }

    @SuppressLint("StaticFieldLeak")
    private class loadDataAsync extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            return getContentResolver().query(
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

            MainActivity.this.cursor = cursor;
            favoriteAdapter.replaceAll(MainActivity.this.cursor);
        }
    }
}
