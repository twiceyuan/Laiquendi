package xyz.rasp.laiquendi.wallpaper.helper;

import android.content.res.Resources;

/**
 * Created by twiceYuan on 2017/3/25.
 * <p>
 * Common Utils
 */
public class Utils {

    public static int getNavigationBarHeight() {
        Resources resources = Resources.getSystem();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static int getStatusBarHeight() {
        Resources resources = Resources.getSystem();
        int result = 0;
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
