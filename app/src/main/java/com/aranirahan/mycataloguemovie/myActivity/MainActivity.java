package com.aranirahan.mycataloguemovie.myActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.aranirahan.mycataloguemovie.R;
import com.aranirahan.mycataloguemovie.myFragment.FavoriteFragment;
import com.aranirahan.mycataloguemovie.myFragment.PlayingFragment;
import com.aranirahan.mycataloguemovie.myFragment.UpcomingFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        if (savedInstanceState == null) {
//            Fragment fragment = null;
//            Class fragmentClass;
//            fragmentClass = PlayingFragment.class;
//            try {
//                fragment = (Fragment) fragmentClass.newInstance();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.fl_content, fragment).commit();
//        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            Fragment currentFragment = new PlayingFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_content, currentFragment)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint(getResources().getString(R.string.search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra(SearchActivity.KEY_SEARCH_INPUT, query);
                startActivity(intent);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_language) {
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        } if(id == R.id.action_reminder){
            Intent intentReminder = new Intent(this, SettingActivity.class);
            startActivity(intentReminder);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

//        Fragment fragment = null;
//        Class fragmentClass = null;
//
//        int id = item.getItemId();
//
//        if (id == R.id.playing) {
//            fragmentClass = PlayingFragment.class;
//        } else if (id == R.id.upcoming) {
//            fragmentClass = UpcomingFragment.class;
//        } else if (id == R.id.favorite) {
//            fragmentClass = FavoriteFragment.class;
//        }
//
//        try {
//            assert fragmentClass != null;
//            fragment = (Fragment) fragmentClass.newInstance();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.fl_content, fragment).commit();
//
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
        int id = item.getItemId();

        Fragment fragment = null;

        if (id == R.id.playing) {
            fragment = new PlayingFragment();

        } else if (id == R.id.upcoming) {
            fragment = new UpcomingFragment();

        } else if (id == R.id.favorite) {
            fragment = new FavoriteFragment();

        }


        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_content, fragment)
                    .commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
