package xyz.rasp.laiquendi.core;

import android.view.View;

/**
 * Created by twiceYuan on 2017/3/20.
 * <p>
 * Component
 */
public interface ComponentCreateListener<Layout extends View> {

    void onComponentCreate(Layout rootView);
}
