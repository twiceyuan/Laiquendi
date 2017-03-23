package xyz.rasp.laiquendi.core;

import android.view.View;

/**
 * Created by twiceYuan on 2017/3/20.
 * <p>
 * ParamsComponent
 */
public interface ParamsLoadListener<Layout extends View> extends ComponentCreateListener<Layout> {

    void onLoadParams(String params);
}
