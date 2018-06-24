package com.aranirahan.mycataloguemovie.myActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aranirahan.mycataloguemovie.BuildConfig;
import com.aranirahan.mycataloguemovie.R;
import com.aranirahan.mycataloguemovie.api.ApiClient;
import com.aranirahan.mycataloguemovie.database.FavoriteHelper;
import com.aranirahan.mycataloguemovie.database.FavoriteTable;
import com.aranirahan.mycataloguemovie.model.sub.ResultsItem;
import com.aranirahan.mycataloguemovie.model.main.DetailModel;
import com.aranirahan.mycataloguemovie.util.MyLocaleState;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.aranirahan.mycataloguemovie.database.DatabaseContract.CONTENT_URI;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String KEY_ITEM = "KEY_ITEM";

    TextView tvTitle;
    ImageView imgBackdrop;
    ImageView imgPoster;
    TextView tvReleaseDate;
    TextView tvVote;
    TextView tvRevenue;
    TextView tvOverview;
    ImageView ivFavorite;
    ImageView[] imgVote;

    private Call<DetailModel> apiCall;
    private ApiClient apiClient = new ApiClient();
    private Gson gson = new Gson();

    private ResultsItem resultsItem;
    private FavoriteHelper favoriteHelper;
    private Boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.detail_movie);

        imgVote = new ImageView[5];
        tvTitle = findViewById(R.id.tv_title);
        imgBackdrop = findViewById(R.id.img_backdrop);
        imgPoster = findViewById(R.id.img_poster);
        tvReleaseDate = findViewById(R.id.tv_release_date);
        tvVote = findViewById(R.id.tv_vote);
        tvOverview = findViewById(R.id.tv_overview);
        tvRevenue = findViewById(R.id.tv_revenue);
        ivFavorite = findViewById(R.id.iv_favorite);
        imgVote[0] = findViewById(R.id.img_star1);
        imgVote[1] = findViewById(R.id.img_star2);
        imgVote[2] = findViewById(R.id.img_star3);
        imgVote[3] = findViewById(R.id.img_star4);
        imgVote[4] = findViewById(R.id.img_star5);

        String json = getIntent().getStringExtra(KEY_ITEM);
        resultsItem = gson.fromJson(json, ResultsItem.class);

        favoriteHelper = new FavoriteHelper(this);
        favoriteHelper.open();

        Cursor cursor = getContentResolver().query(
                Uri.parse(CONTENT_URI + "/" + resultsItem.getId()),
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) isFavorite = true;
            cursor.close();
        }

        setFavorite();

        tvTitle.setText(resultsItem.getTitle());
        tvReleaseDate.setText(resultsItem.getReleaseDate());
        tvVote.setText(String.valueOf(resultsItem.getVoteAverage()));
        tvOverview.setText(resultsItem.getOverview());

        Picasso.get()
                .load(BuildConfig.BASE_URL_IMAGE + "w154" + resultsItem.getPosterPath())
                .into(imgPoster);

        Picasso.get()
                .load(BuildConfig.BASE_URL_IMAGE + "w154" + resultsItem.getBackdropPath())
                .into(imgBackdrop);

        apiCall = apiClient.getService().getDetailMovie(String.valueOf(resultsItem.getId()),
                MyLocaleState.getLocaleState());
        apiCall.enqueue(new Callback<DetailModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<DetailModel> call,
                                   @NonNull Response<DetailModel> response) {
                if (response.isSuccessful()) {
                    DetailModel item = response.body();

                    assert item != null;
                    tvRevenue.setText("Revenue : $ " + NumberFormat.getIntegerInstance()
                            .format(item.getRevenue()));


                } else {
                    Snackbar snack = Snackbar.make(findViewById(R.id.sv_detail), R.string.error_message,
                            Snackbar.LENGTH_LONG);
                    View myView = snack.getView();
                    TextView tv = myView.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(Color.RED);
                    snack.show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<DetailModel> call, @NonNull Throwable t) {
                Snackbar snack = Snackbar.make(findViewById(R.id.sv_detail), R.string.error_message,
                        Snackbar.LENGTH_LONG);
                View myView = snack.getView();
                TextView tv = myView.findViewById(android.support.design.R.id.snackbar_text);
                tv.setTextColor(Color.RED);
                snack.show();
            }
        });

        int voteAverage = (int) (resultsItem.getVoteAverage());

        for (int i = 0; i < voteAverage / 2; i++) {
            imgVote[i].setImageResource(R.drawable.ic_star_black_24dp);
        }

        ivFavorite.setOnClickListener(this);
    }

    private void setFavorite() {
        if (isFavorite) {
            ivFavorite.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            ivFavorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
    }

    private void saveFavorite() {
        ContentValues cv = new ContentValues();
        cv.put(FavoriteTable.ID_, resultsItem.getId());
        cv.put(FavoriteTable.POSTER, resultsItem.getPosterPath());
        cv.put(FavoriteTable.BACKDROP, resultsItem.getBackdropPath());
        cv.put(FavoriteTable.TITLE, resultsItem.getTitle());
        cv.put(FavoriteTable.OVERVIEW, resultsItem.getOverview());
        cv.put(FavoriteTable.RELEASE_DATE, resultsItem.getReleaseDate());
        cv.put(FavoriteTable.VOTE, resultsItem.getVoteAverage());

        getContentResolver().insert(CONTENT_URI, cv);

        Snackbar.make(findViewById(R.id.sv_detail),
                R.string.add_favorite,
                Snackbar.LENGTH_LONG).show();
    }

    private void removeFavorite() {
        getContentResolver().delete(
                Uri.parse(CONTENT_URI + "/" + resultsItem.getId()),
                null,
                null
        );

        Snackbar.make(findViewById(R.id.sv_detail),
                R.string.remove_favorite,
                Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (apiCall != null) {
            apiCall.cancel();
        }
        if (favoriteHelper != null) {
            favoriteHelper.close();
        }
    }

    @Override
    public void onClick(View v) {
        if (isFavorite) removeFavorite();
        else saveFavorite();
        isFavorite = !isFavorite;
        setFavorite();
    }
}
