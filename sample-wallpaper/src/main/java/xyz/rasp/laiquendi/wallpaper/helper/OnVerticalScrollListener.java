package xyz.rasp.laiquendi.wallpaper.helper;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class OnVerticalScrollListener extends RecyclerView.OnScrollListener {

    private final AtomicBoolean isScrollToEnd = new AtomicBoolean(false);

    private SwipeRefreshLayout mRefreshLayout = null;

    public OnVerticalScrollListener() {
    }

    public OnVerticalScrollListener(SwipeRefreshLayout refreshLayout) {
        mRefreshLayout = refreshLayout;
    }

    @Override
    public final void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        // 处理 SwipeRefreshLayout 和 RecyclerView 滚动冲突问题
        if (mRefreshLayout != null) {
            recyclerView.setNestedScrollingEnabled(false);
            int topRowVerticalPosition = recyclerView.getChildCount() == 0 ? 0 : recyclerView.getChildAt(0).getTop();
            mRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
        }

        if (!recyclerView.canScrollVertically(1)) {
            synchronized (isScrollToEnd) {
                isScrollToEnd.set(true);
            }
        } else if (!recyclerView.canScrollVertically(-1)) {
            onScrolledToTop();
        } else if (dy < 0) {
            onScrolledUp();
        } else if (dy > 0) {
            onScrolledDown();
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        //停止滚动
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            synchronized (isScrollToEnd) {
                if (isScrollToEnd.get()) {
                    onScrolledToEnd();
                    isScrollToEnd.set(false);
                }
            }
        }
    }

    public void onScrolledUp() {
    }

    public void onScrolledDown() {
    }

    public void onScrolledToEnd() {
    }

    public void onScrolledToTop() {
    }
}
