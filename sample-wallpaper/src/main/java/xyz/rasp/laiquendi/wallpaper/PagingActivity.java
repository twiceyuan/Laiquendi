package xyz.rasp.laiquendi.wallpaper;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.twiceyuan.commonadapter.library.LayoutId;
import com.twiceyuan.commonadapter.library.Singleton;
import com.twiceyuan.commonadapter.library.ViewId;
import com.twiceyuan.commonadapter.library.adapter.CommonAdapter;
import com.twiceyuan.commonadapter.library.holder.CommonHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import xyz.rasp.laiquendi.wallpaper.helper.PagingHelper;
import xyz.rasp.laiquendi.wallpaper.helper.WallpaperUtil;

/**
 * Created by twiceYuan on 2017/3/24.
 * <p>
 * 分页简单实现
 */
public class PagingActivity extends Activity {

    private static final int REQUEST_STORAGE = 1001;

    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.refresh)      SwipeRefreshLayout mRefresh;

    private Action mOnPermissionAllowed = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paging);
        ButterKnife.bind(this);

        CommonAdapter<String, ItemHolder> adapter = new CommonAdapter<>(this, ItemHolder.class);

        PagingHelper.<String>create()
                .setAdapter(adapter)
                .setRecyclerView(mRecyclerView)
                .setRefreshLayout(mRefresh)
                .setDataSource(this::mockDataSource)
                .setSize(5)
                .init();

        adapter.setOnItemClickListener((position, s) -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE);
                mOnPermissionAllowed = () -> WallpaperUtil.set(this, s);
            } else {
                WallpaperUtil.set(this, s);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mOnPermissionAllowed != null) {
                    try {
                        mOnPermissionAllowed.run();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 模拟五页数据
     */
    private Observable<List<String>> mockDataSource(int page, int size) {

        String url = "https://bing.ioliu.cn/v1?w=800&d=%s";
        if (page >= 50) return Observable.just(new ArrayList<>());

        return Observable.range(page * size, size)
                .map(number -> String.format(url, number + 1))
                .toList()
                .delay(1, TimeUnit.SECONDS)
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread());
    }

    @SuppressWarnings("WeakerAccess")
    @LayoutId(R.layout.item_sample)
    public static class ItemHolder extends CommonHolder<String> {

        @ViewId(R.id.iv_image) ImageView mImageView;

        @Singleton
        RequestManager mManager;

        @Override
        public void initSingleton() {
            mManager = Glide.with(getItemView().getContext());
        }

        @Override
        public void bindData(String s) {
            mManager.load(s).into(mImageView);
        }
    }
}
