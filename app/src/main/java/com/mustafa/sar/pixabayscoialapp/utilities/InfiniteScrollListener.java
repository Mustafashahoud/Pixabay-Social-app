package com.mustafa.sar.pixabayscoialapp.utilities;


import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


public abstract class InfiniteScrollListener extends RecyclerView.OnScrollListener {

    private int previousTotal = 0;
    private boolean loadMore = true;
    private int current_page = 1;

    private final LinearLayoutManager mLinearLayoutManager;

    protected InfiniteScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = mLinearLayoutManager.getItemCount();
        int firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        if (loadMore && (totalItemCount > previousTotal)) {
            loadMore = false;
            previousTotal = totalItemCount;
        }

        int visibleThreshold = 5;
        if (!loadMore && (totalItemCount - visibleItemCount)
                <= (firstVisibleItem + visibleThreshold)) {
            current_page++;
            onLoadMore(current_page);
            loadMore = true;
        }
    }

    public abstract void onLoadMore(int currentPage);

    public void resetCurrentPage() {
        current_page = 1;
        previousTotal = 0;
        loadMore = true;
    }
}