package xyz.rasp.laiquendi.wallpaper.helper;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * Created by twiceYuan on 2017/3/25.
 * <p>
 * Glide 加载图片完成监听器
 */
public class GlideCompleteListener<T, R> implements RequestListener<T, R> {

    private Runnable mAction;

    private GlideCompleteListener(Runnable action) {
        mAction = action;
    }

    public static <T, R> GlideCompleteListener<T, R> listen(Runnable action) {
        return new GlideCompleteListener<>(action);
    }

    @Override
    public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
        mAction.run();
        return false;
    }

    @Override
    public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
        mAction.run();
        return false;
    }
}
