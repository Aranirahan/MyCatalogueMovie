package com.aranirahan.mycataloguemovie.myActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aranirahan.mycataloguemovie.BuildConfig;
import com.aranirahan.mycataloguemovie.R;
import com.aranirahan.mycataloguemovie.api.ApiClient;
import com.aranirahan.mycataloguemovie.database.FavoriteHelper;
import com.aranirahan.mycataloguemovie.model.ResultsItem;
import com.aranirahan.mycataloguemovie.model.detail.DetailModel;
import com.aranirahan.mycataloguemovie.provider.FavoriteColumns;
import com.aranirahan.mycataloguemovie.util.MyLocaleState;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.aranirahan.mycataloguemovie.provider.DatabaseContract.CONTENT_URI;


public class DetailActivity extends AppCompatActivity {

    public static final String KEY_ITEM = "KEY_ITEM";

    TextView tv_title;
    ImageView img_backdrop;
    ImageView img_poster;
    TextView tv_release_date;
    TextView tv_vote;
    TextView tv_revenue;
    TextView tv_overview;
    ImageView iv_fav;
    ImageView[] img_vote;

    private Call<DetailModel> apiCall;
    private ApiClient apiClient = new ApiClient();
    private Gson gson = new Gson();

    private ResultsItem item;
    private FavoriteHelper favoriteHelper;
    private Boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        img_vote = new ImageView[5];
        tv_title = findViewById(R.id.tv_title);
        img_backdrop = findViewById(R.id.img_backdrop);
        img_poster = findViewById(R.id.img_poster);
        tv_release_date = findViewById(R.id.tv_release_date);
        tv_vote = findViewById(R.id.tv_vote);
        tv_overview = findViewById(R.id.tv_overview);
        tv_revenue = findViewById(R.id.tv_revenue);
        iv_fav = findViewById(R.id.iv_fav);
        img_vote[0] = findViewById(R.id.img_star1);
        img_vote[1] = findViewById(R.id.img_star2);
        img_vote[2] = findViewById(R.id.img_star3);
        img_vote[3] = findViewById(R.id.img_star4);
        img_vote[4] = findViewById(R.id.img_star5);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        String json = getIntent().getStringExtra(KEY_ITEM);
        item = gson.fromJson(json, ResultsItem.class);

        favoriteHelper = new FavoriteHelper(this);
        favoriteHelper.open();

        Cursor cursor = getContentResolver().query(
                Uri.parse(CONTENT_URI + "/" + item.getId()),
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) isFavorite = true;
            cursor.close();
        }

        favoriteSet();

        getSupportActionBar().setTitle(item.getTitle());
        tv_title.setText(item.getTitle());
        tv_release_date.setText(item.getReleaseDate());
        tv_vote.setText(String.valueOf(item.getVoteAverage()));
        tv_overview.setText(item.getOverview());

        Picasso.get()
                .load(BuildConfig.BASE_URL_IMAGE + "w154" + item.getPosterPath())
                .into(img_poster);

        Picasso.get()
                .load(BuildConfig.BASE_URL_IMAGE + "w154" + item.getBackdropPath())
                .into(img_backdrop);

        apiCall = apiClient.getService().getDetailMovie(String.valueOf(item.getId()), MyLocaleState.getLocaleState());
        apiCall.enqueue(new Callback<DetailModel>() {
            @Override
            public void onResponse(Call<DetailModel> call, Response<DetailModel> response) {
                if (response.isSuccessful()) {
                    DetailModel item = response.body();

                    tv_revenue.setText("Vanue : $ " + NumberFormat.getIntegerInstance().format(item.getRevenue()));


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
            public void onFailure(Call<DetailModel> call, Throwable t) {
                Snackbar snack = Snackbar.make(findViewById(R.id.sv_detail), R.string.error_message,
                        Snackbar.LENGTH_LONG);
                View myView = snack.getView();
                TextView tv = myView.findViewById(android.support.design.R.id.snackbar_text);
                tv.setTextColor(Color.RED);
                snack.show();
            }
        });

        int voteAverage = (int) (item.getVoteAverage());

        for (int i = 0; i < voteAverage / 2; i++) {
            img_vote[i].setImageResource(R.drawable.ic_star_black_24dp);
        }

        iv_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFavorite) FavoriteRemove();
                else FavoriteSave();

                isFavorite = !isFavorite;
                favoriteSet();
            }
        });
    }

    private void favoriteSet() {
        if (isFavorite) iv_fav.setImageResource(R.drawable.ic_favorite_black_24dp);
        else iv_fav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
    }

    private void loadFailed() {
        Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show();
    }

    private void FavoriteSave() {
        //Log.d("TAG", "FavoriteSave: " + item.getId());
        ContentValues cv = new ContentValues();
        cv.put(FavoriteColumns.COLUMN_ID, item.getId());
        cv.put(FavoriteColumns.COLUMN_TITLE, item.getTitle());
        cv.put(FavoriteColumns.COLUMN_BACKDROP, item.getBackdropPath());
        cv.put(FavoriteColumns.COLUMN_POSTER, item.getPosterPath());
        cv.put(FavoriteColumns.COLUMN_RELEASE_DATE, item.getReleaseDate());
        cv.put(FavoriteColumns.COLUMN_VOTE, item.getVoteAverage());
        cv.put(FavoriteColumns.COLUMN_OVERVIEW, item.getOverview());

        getContentResolver().insert(CONTENT_URI, cv);
        Toast.makeText(this, R.string.add_favorite, Toast.LENGTH_SHORT).show();
    }

    private void FavoriteRemove() {
        getContentResolver().delete(
                Uri.parse(CONTENT_URI + "/" + item.getId()),
                null,
                null
        );
        Toast.makeText(this, R.string.remove_favorite, Toast.LENGTH_SHORT).show();
    }
}
