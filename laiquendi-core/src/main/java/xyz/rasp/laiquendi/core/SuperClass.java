package xyz.rasp.laiquendi.core;

import android.view.ViewGroup;

/**
 * Created by twiceYuan on 2017/3/21.
 *
 * Specific Super Class
 */
public @interface SuperClass {
    Class<? extends ViewGroup> value();
}
