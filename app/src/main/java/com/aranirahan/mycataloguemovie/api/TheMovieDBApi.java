package com.aranirahan.mycataloguemovie.api;

import com.aranirahan.mycataloguemovie.model.main.PlayingItem;
import com.aranirahan.mycataloguemovie.model.main.SearchItem;
import com.aranirahan.mycataloguemovie.model.main.UpcomingItem;
import com.aranirahan.mycataloguemovie.model.main.DetailItem;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheMovieDBApi {

    @GET("search/movie")
    Call<SearchItem> getSearchMovie(@Query("query") String query,
                                    @Query("language") String language);

    @GET("movie/now_playing")
    Call<PlayingItem> getPlayingMovie(@Query("language") String language);

    @GET("movie/upcoming")
    Call<UpcomingItem> getUpcomingMovie(@Query("language") String language);

    @GET("movie/{movie_id}")
    Call<DetailItem> getDetailMovie(@Path("movie_id") String movie_id,
                                    @Query("language") String language);

}
