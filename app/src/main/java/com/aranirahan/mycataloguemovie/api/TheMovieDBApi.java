package com.aranirahan.mycataloguemovie.api;

import com.aranirahan.mycataloguemovie.model.main.PlayingModel;
import com.aranirahan.mycataloguemovie.model.main.SearchModel;
import com.aranirahan.mycataloguemovie.model.main.UpcomingModel;
import com.aranirahan.mycataloguemovie.model.main.DetailModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheMovieDBApi {

    @GET("search/movie")
    Call<SearchModel> getSearchMovie(@Query("query") String query,
                                     @Query("language") String language);

    @GET("movie/now_playing")
    Call<PlayingModel> getPlayingMovie(@Query("language") String language);

    @GET("movie/upcoming")
    Call<UpcomingModel> getUpcomingMovie(@Query("language") String language);

    @GET("movie/{movie_id}")
    Call<DetailModel> getDetailMovie(@Path("movie_id") String movie_id,
                                     @Query("language") String language);

}
