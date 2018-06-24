package com.aranirahan.mycataloguemovie.myActivity;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aranirahan.mycataloguemovie.R;
import com.aranirahan.mycataloguemovie.adapter.MainAdapter;
import com.aranirahan.mycataloguemovie.api.ApiClient;
import com.aranirahan.mycataloguemovie.model.main.SearchModel;
import com.aranirahan.mycataloguemovie.util.MyLocaleState;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    public static final String KEY_SEARCH_INPUT = "KEY_SEARCH_INPUT";

    RecyclerView rvSearch;
    private MainAdapter mainAdapter;
    private ApiClient apiClient = new ApiClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.search_result);

        rvSearch = findViewById(R.id.rv_search);

        mainAdapter = new MainAdapter();
        rvSearch.setLayoutManager(new LinearLayoutManager(this));
        rvSearch.setAdapter(mainAdapter);

        String searchInput = getIntent().getStringExtra(KEY_SEARCH_INPUT);

        Call<SearchModel> apiCall = apiClient.getService().getSearchMovie(searchInput,
                MyLocaleState.getLocaleState());
        apiCall.enqueue(new Callback<SearchModel>() {
            @Override
            public void onResponse(@NonNull Call<SearchModel> call,
                                   @NonNull Response<SearchModel> response) {
                if (response.isSuccessful()) {
                    mainAdapter.replaceListResultsItem(Objects
                            .requireNonNull(response.body())
                            .getResults());
                } else {
                    Snackbar snack = Snackbar.make(findViewById(R.id.ll_search),
                            R.string.error_message,
                            Snackbar.LENGTH_LONG);
                    View myView = snack.getView();
                    TextView tv = myView.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(Color.RED);
                    snack.show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchModel> call, @NonNull Throwable t) {
                Snackbar snack = Snackbar.make(findViewById(R.id.ll_search),
                        R.string.error_message,
                        Snackbar.LENGTH_LONG);
                View myView = snack.getView();
                TextView tv = myView.findViewById(android.support.design.R.id.snackbar_text);
                tv.setTextColor(Color.RED);
                snack.show();
            }
        });

    }

}
