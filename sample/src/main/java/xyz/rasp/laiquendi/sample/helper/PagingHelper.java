package xyz.rasp.laiquendi.sample.helper;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.twiceyuan.commonadapter.library.adapter.CommonAdapter;
import com.twiceyuan.commonadapter.library.adapter.WrapperAdapter;
import com.twiceyuan.commonadapter.library.holder.CommonHolder;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import xyz.rasp.laiquendi.sample.components.LoadFooterView;
import xyz.rasp.laiquendi.sample.components.StateLayout;

/**
 * Created by twiceYuan on 2017/3/24.
 * <p>
 * 分页管理器
 */
@SuppressWarnings("WeakerAccess")
public class PagingHelper<Model> {

    private final AtomicBoolean mIsLoading = new AtomicBoolean(false);

    private LoadFooterView     mLoadFooterView;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView       mRecyclerView;
    private StateLayout        mStateLayout;
    private DataSource<Model>  mDataSource;

    private CommonAdapter<Model, ? extends CommonHolder<Model>> mOriginAdapter;

    private int mPage = 0;
    private int mSize = 20;

    private PagingHelper() {
    }

    public static <T> PagingHelper<T> create() {
        return new PagingHelper<>();
    }

    public PagingHelper<Model> setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        return this;
    }

    public PagingHelper<Model> setRefreshLayout(SwipeRefreshLayout refreshLayout) {
        mRefreshLayout = refreshLayout;
        return this;
    }

    public PagingHelper<Model> setAdapter(CommonAdapter<Model, ? extends CommonHolder<Model>> adapter) {
        mOriginAdapter = adapter;
        return this;
    }

    public PagingHelper<Model> setDataSource(DataSource<Model> dataSource) {
        mDataSource = dataSource;
        return this;
    }

    public PagingHelper<Model> setStateLayout(StateLayout stateLayout) {
        mStateLayout = stateLayout;
        return this;
    }

    public void init() {
        Context context = mRecyclerView.getContext();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        WrapperAdapter<Model, ?> adapter = new WrapperAdapter<>(mOriginAdapter, mRecyclerView);
        mLoadFooterView = new LoadFooterView(context);
        adapter.setFooterView(mLoadFooterView);
        mRecyclerView.setAdapter(adapter);
        mRefreshLayout.setOnRefreshListener(this::onRefresh);
        onRefresh();
        mRecyclerView.addOnScrollListener(new OnVerticalScrollListener() {
            @Override
            public void onScrolledToEnd() {
                loadMore();
            }
        });
    }

    private void onRefresh() {
        mPage = 0;
        mOriginAdapter.clear();
        loadMore();
    }

    private void loadMore() {
        synchronized (mIsLoading) {
            loading();
            mDataSource.load(mPage, mSize).subscribe(models -> {
                if (mPage == 1 && models.size() == 0) {
                    mStateLayout.showEmpty();
                    return;
                } else {
                    mStateLayout.showContent();
                }
                if (models.size() < mSize) {
                    loadComplete();
                    return;
                }
                mOriginAdapter.addAll(models);
                mOriginAdapter.notifyDataSetChanged();
                mPage++;
                loadable();
            }, throwable -> loadComplete());
        }
    }

    private void loadable() {
        mIsLoading.set(true);
        mRefreshLayout.setRefreshing(false);
        mLoadFooterView.getLoadFooter().loadable();
    }

    private void loading() {
        mIsLoading.set(true);
        mLoadFooterView.getLoadFooter().loading();
    }

    private void loadComplete() {
        mIsLoading.set(false);
        mRefreshLayout.setRefreshing(false);
        mLoadFooterView.getLoadFooter().loaded();
    }

    @SuppressWarnings("WeakerAccess")
    public interface DataSource<Model> {
        Observable<? extends List<Model>> load(int page, int size);
    }
}
