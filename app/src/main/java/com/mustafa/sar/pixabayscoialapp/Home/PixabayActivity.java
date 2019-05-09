package com.mustafa.sar.pixabayscoialapp.Home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mustafa.sar.pixabayscoialapp.R;
import com.mustafa.sar.pixabayscoialapp.api_services.InternetCheck;
import com.mustafa.sar.pixabayscoialapp.api_services.PixabayService;
import com.mustafa.sar.pixabayscoialapp.models.Image;
import com.mustafa.sar.pixabayscoialapp.models.ImageList;
import com.mustafa.sar.pixabayscoialapp.utilities.InfiniteScrollListener;
import com.mustafa.sar.pixabayscoialapp.utilities.RecyclerImageListAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PixabayActivity extends AppCompatActivity {
    private static final String TAG = "PixabayFragment";
    private List<Image> imageList;
    private RecyclerImageListAdapter recyclerImageListAdapter;
    private InfiniteScrollListener infiniteScrollListener;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private TextView noResults;
    private MenuItem searchMenuItem;
    private String defaultQuery = "fruits";
    private Context mContext = PixabayActivity.this ;


    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pixabay);
        initViews();
        initRecyclerView();
        initToolbar();
        loadImages(1, defaultQuery);
        if (!InternetCheck.isInternetAvailable(mContext)) {
            initSnackBar(R.string.no_internet);
        }
    }

    private void initViews( ) {
        recyclerView = findViewById(R.id.activity_main_list);
        progressBar = findViewById(R.id.activity_main_progress);
        toolbar = findViewById(R.id.activity_main_toolbar);
        noResults = findViewById(R.id.activity_main_no_results_text);
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 3);
       // LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLayoutManager);
        imageList = new ArrayList<>();
        recyclerImageListAdapter = new RecyclerImageListAdapter(imageList);
        recyclerView.setAdapter(recyclerImageListAdapter);
        initInfiniteScrollListener(mLayoutManager);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
    }

    private void initSnackBar(int messageId) {
        progressBar.setVisibility(View.GONE);
        final Snackbar snackbar = Snackbar.make(recyclerView, messageId, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.dismiss, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    private void initInfiniteScrollListener(GridLayoutManager mLayoutManager) {
        infiniteScrollListener = new InfiniteScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page) {
                progressBar.setVisibility(View.VISIBLE);
                loadImages(page, defaultQuery);
            }
        };
        recyclerView.addOnScrollListener(infiniteScrollListener);
    }

    private void loadImages(int page, String query) {
        PixabayService.createService(mContext)
                .getImages(getString(R.string.API_KEY), query, page)
                .enqueue(new Callback<ImageList>() {
                    @Override
                    public void onResponse(Call<ImageList> call, Response<ImageList> response) {
                        if (response.isSuccessful()){
                            addImagesToList(response.body()); //The deserialized response body
                        }
                        else{
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                    @Override
                    public void onFailure(Call<ImageList> call, Throwable t) {
                        initSnackBar(R.string.error);
                    }
                });
    }

    private void addImagesToList(ImageList response) {
        progressBar.setVisibility(View.GONE);
        int position = imageList.size();
        imageList.addAll(response.getHits());
        recyclerImageListAdapter.notifyItemRangeInserted(position, position + 20);
        if (imageList.isEmpty()) noResults.setVisibility(View.VISIBLE);
        else noResults.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(searchListener);
        return true;
    }

    private SearchView.OnQueryTextListener searchListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            searchMenuItem.collapseActionView();
            defaultQuery = query;
            resetImageList();
            progressBar.setVisibility(View.VISIBLE);
            noResults.setVisibility(View.GONE);
            loadImages(1, defaultQuery);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };

    private void resetImageList() {
        imageList.clear();
        infiniteScrollListener.resetCurrentPage();
        recyclerImageListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (!InternetCheck.isInternetAvailable(mContext))
            initSnackBar(R.string.no_internet);
    }
}
