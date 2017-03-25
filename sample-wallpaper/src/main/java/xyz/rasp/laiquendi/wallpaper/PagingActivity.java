package xyz.rasp.laiquendi.wallpaper;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.twiceyuan.commonadapter.library.LayoutId;
import com.twiceyuan.commonadapter.library.Singleton;
import com.twiceyuan.commonadapter.library.ViewId;
import com.twiceyuan.commonadapter.library.adapter.CommonAdapter;
import com.twiceyuan.commonadapter.library.holder.CommonHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.rasp.laiquendi.wallpaper.func.Action;
import xyz.rasp.laiquendi.wallpaper.func.Callback;
import xyz.rasp.laiquendi.wallpaper.helper.GlideCompleteListener;
import xyz.rasp.laiquendi.wallpaper.helper.PagingHelper;
import xyz.rasp.laiquendi.wallpaper.helper.Utils;
import xyz.rasp.laiquendi.wallpaper.helper.WallpaperUtil;

/**
 * Created by twiceYuan on 2017/3/24.
 * <p>
 * 分页简单实现
 */
public class PagingActivity extends Activity {

    private static final int REQUEST_STORAGE = 1001;

    @BindView(R.id.recyclerView) RecyclerView       mRecyclerView;
    @BindView(R.id.refresh)      SwipeRefreshLayout mRefresh;

    private Action mOnPermissionAllowed = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_paging);
        ButterKnife.bind(this);

        mRecyclerView.setPadding(0, Utils.getStatusBarHeight(), 0, Utils.getNavigationBarHeight());
        mRecyclerView.setClipToPadding(false);

        CommonAdapter<String, ItemHolder> adapter = new CommonAdapter<>(this, ItemHolder.class);

        PagingHelper.<String>create()
                .setAdapter(adapter)
                .setRecyclerView(mRecyclerView)
                .setRefreshLayout(mRefresh)
                .setDataSource(this::mockDataSource)
                .setSize(5)
                .init();

        adapter.setOnItemClickListener((position, url) -> {
            new AlertDialog.Builder(this)
                    .setMessage("是否下载并设置为壁纸？")
                    .setPositiveButton("确定", (dialog, which) -> setWallpaper(url))
                    .setNegativeButton("取消", null)
                    .show();
        });
    }

    private void setWallpaper(String s) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE);
            mOnPermissionAllowed = () -> WallpaperUtil.set(this, s);
        } else {
            WallpaperUtil.set(this, s);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mOnPermissionAllowed != null) {
                    try {
                        mOnPermissionAllowed.call();
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
    private void mockDataSource(int page, int size, Callback<List<String>> loadCallback) {

        String url = "https://bing.ioliu.cn/v1?w=800&d=%s";
        if (page >= 50) {
            loadCallback.call(new ArrayList<>());
            return;
        }

        List<String> urls = new ArrayList<>();
        for (int i = page * size; i < (page + 1) * size; i++) {
            urls.add(String.format(url, i + 1));
        }

        new Thread(() -> {
            // mock request time
            SystemClock.sleep(300);
            runOnUiThread(() -> loadCallback.call(urls));
        }).start();
    }

    @SuppressWarnings("WeakerAccess")
    @LayoutId(R.layout.item_sample)
    public static class ItemHolder extends CommonHolder<String> {

        @ViewId(R.id.iv_image) ImageView   mImageView;
        @ViewId(R.id.pb_load)  ProgressBar mProgressBar;

        @Singleton
        RequestManager mManager;

        @Override
        public void initSingleton() {
            mManager = Glide.with((Activity) getItemView().getContext());
        }

        @Override
        public void bindData(String s) {
            mManager.load(s)
                    .listener(GlideCompleteListener.listen(() -> mProgressBar.setVisibility(View.GONE)))
                    .crossFade()
                    .into(mImageView);
        }
    }
}
