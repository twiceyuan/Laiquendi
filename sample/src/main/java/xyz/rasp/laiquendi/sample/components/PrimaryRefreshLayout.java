package xyz.rasp.laiquendi.sample.components;

import android.support.v4.widget.SwipeRefreshLayout;

import xyz.rasp.laiquendi.core.Component;
import xyz.rasp.laiquendi.core.ComponentCreateListener;
import xyz.rasp.laiquendi.core.SuperClass;
import xyz.rasp.laiquendi.sample.R;

/**
 * Created by twiceYuan on 2017/3/23.
 *
 * Primary Color 的刷新布局
 */
@Component
@SuperClass(SwipeRefreshLayout.class)
public class PrimaryRefreshLayout implements ComponentCreateListener<SwipeRefreshLayout> {

    @Override
    public void onComponentCreate(SwipeRefreshLayout rootView) {
        rootView.setColorSchemeResources(R.color.colorPrimary);
    }
}
