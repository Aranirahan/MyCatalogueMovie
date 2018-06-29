package com.aranirahan.mycataloguemovie.myFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aranirahan.mycataloguemovie.R;
import com.aranirahan.mycataloguemovie.adapter.MainAdapter;
import com.aranirahan.mycataloguemovie.api.ApiClient;
import com.aranirahan.mycataloguemovie.model.main.UpcomingItem;
import com.aranirahan.mycataloguemovie.model.sub.ResultsItem;
import com.aranirahan.mycataloguemovie.util.MyLocaleState;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpcomingFragment extends Fragment {
    public static String UPCOMING_RESULTS_ITEM_KEY = "UpcomingKeyResultsItems";

    private MainAdapter mainAdapter;
    public ArrayList<ResultsItem> resultsItems;

    public UpcomingFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity()).setTitle(R.string.upcoming_movie);
        final View view = inflater.inflate(R.layout.fragment_main, container, false);


        RecyclerView rvMain = view.findViewById(R.id.rv_main);

        mainAdapter = new MainAdapter();
        rvMain.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvMain.setAdapter(mainAdapter);
        if (savedInstanceState != null) {
            resultsItems = savedInstanceState.getParcelableArrayList(UPCOMING_RESULTS_ITEM_KEY);
            mainAdapter.replaceListResultsItem(resultsItems);
        } else {
            ApiClient apiClient = new ApiClient();
            Call<UpcomingItem> apiCall = apiClient.getService()
                    .getUpcomingMovie(MyLocaleState.getLocaleState());
            apiCall.enqueue(new Callback<UpcomingItem>() {
                @Override
                public void onResponse(@NonNull Call<UpcomingItem> call,
                                       @NonNull Response<UpcomingItem> response) {
                    if (response.isSuccessful()) {
                        mainAdapter.replaceListResultsItem(
                                Objects.requireNonNull(response.body()).getResults());
                        resultsItems = Objects.requireNonNull(response.body()).getResults();
                    } else {
                        failedSnackbar(view);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<UpcomingItem> call,
                                      @NonNull Throwable t) {
                    failedSnackbar(view);
                }
            });
        }

        return view;
    }

    private void failedSnackbar(View view) {
        Snackbar snack = Snackbar.make(view.findViewById(R.id.fl_main),
                R.string.error_message,
                Snackbar.LENGTH_LONG);
        View myView = snack.getView();
        TextView tv = myView.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.RED);
        snack.show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(UPCOMING_RESULTS_ITEM_KEY, resultsItems);
        super.onSaveInstanceState(outState);
    }
}
