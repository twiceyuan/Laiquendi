package xyz.rasp.laiquendi.core;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.view.View;

import java.lang.reflect.Method;

/**
 * Created by twiceYuan on 2017/3/23.
 * <p>
 * Bind Component
 */
public class Eru {

    public static <T> T get(Class<T> componentClass, Activity activity, @IdRes int id) {
        try {
            Class<?> viewClass = Class.forName(componentClass.getCanonicalName() + "View");
            Object view = activity.findViewById(id);
            Method getMethod = viewClass.getDeclaredMethod("get" + componentClass.getSimpleName());
            Object component = getMethod.invoke(view);
            return componentClass.cast(component);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T get(Class<T> componentClass, View parentView, @IdRes int id) {
        try {
            Class<?> viewClass = Class.forName(componentClass.getCanonicalName() + "View");
            Object view = parentView.findViewById(id);
            Method getMethod = viewClass.getDeclaredMethod("get" + componentClass.getSimpleName());
            Object component = getMethod.invoke(view);
            return componentClass.cast(component);
        } catch (Exception e) {
            return null;
        }
    }
}
