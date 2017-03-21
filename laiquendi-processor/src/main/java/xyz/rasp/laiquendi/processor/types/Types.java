package xyz.rasp.laiquendi.processor.types;

import com.squareup.javapoet.ClassName;

/**
 * Created by twiceYuan on 2017/3/21.
 *
 * 用到的 Android 类
 */
public class Types {
    public static final ClassName CONTEXT = ClassName.get("android.content", "Context");
    public static final ClassName ACTIVITY = ClassName.get("android.app", "Activity");
    public static final ClassName ATTRIBUTE_SET = ClassName.get("android.util", "AttributeSet");
    public static final ClassName VIEW = ClassName.get("android.view", "View");
    public static final ClassName VIEW_GROUP = ClassName.get("android.view", "ViewGroup");
    public static final ClassName FRAME_LAYOUT = ClassName.get("android.widget", "FrameLayout");
    public static final ClassName LINEAR_LAYOUT = ClassName.get("android.widget", "LinearLayout");
}
