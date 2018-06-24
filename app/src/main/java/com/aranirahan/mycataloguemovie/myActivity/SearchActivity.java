package com.aranirahan.mycataloguemovie.myActivity;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aranirahan.mycataloguemovie.R;
import com.aranirahan.mycataloguemovie.adapter.MainAdapter;
import com.aranirahan.mycataloguemovie.api.ApiClient;
import com.aranirahan.mycataloguemovie.model.SearchModel;
import com.aranirahan.mycataloguemovie.util.MyLocaleState;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    public static final String KEY_SEARCH_INPUT = "KEY_SEARCH_INPUT";

    RecyclerView rvSearch;

    private MainAdapter adapter;

    private ApiClient apiClient = new ApiClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.search_result);

        rvSearch = findViewById(R.id.rv_search);

        adapter = new MainAdapter();
        rvSearch.setLayoutManager(new LinearLayoutManager(this));
        rvSearch.setAdapter(adapter);

        String searchInput = getIntent().getStringExtra(KEY_SEARCH_INPUT);

        Call<SearchModel> apiCall = apiClient.getService().getSearchMovie(searchInput, MyLocaleState.getLocaleState());
        apiCall.enqueue(new Callback<SearchModel>() {
            @Override
            public void onResponse(@NonNull Call<SearchModel> call,
                                   @NonNull Response<SearchModel> response) {
                if (response.isSuccessful()) {
                    adapter.replaceAll(Objects.requireNonNull(response.body()).getResults());
                } else {
                    Snackbar.make(findViewById(R.id.ll_search), R.string.error_message,
                            Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchModel> call, @NonNull Throwable t) {
                Snackbar.make(findViewById(R.id.ll_search), R.string.error_message,
                        Snackbar.LENGTH_LONG).show();
            }
        });

    }

}
