package xyz.rasp.laiquendi.processor.types;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;

import java.lang.annotation.Annotation;

/**
 * Created by twiceYuan on 2017/3/21.
 * <p>
 * 配置的 Class 相关信息。可以作为混淆过滤的参考
 */
@SuppressWarnings("WeakerAccess")
public class Types {

    public static final ClassName ANNOTATION_COMPONENT   = ClassName.get("xyz.rasp.laiquendi.core", "Component");
    public static final ClassName ANNOTATION_SUPER_CLASS = ClassName.get("xyz.rasp.laiquendi.core", "SuperClass");

    public static final ClassName INTERFACE_PARAMS_LOAD             = ClassName.get("xyz.rasp.laiquendi.core", "ParamsLoadListener");
    public static final String    INTERFACE_PARAMS_LOAD_METHOD      = "onLoadParams";

    public static final ClassName INTERFACE_COMPONENT_CREATE        = ClassName.get("xyz.rasp.laiquendi.core", "ComponentCreateListener");
    public static final String    INTERFACE_COMPONENT_CREATE_METHOD = "onComponentCreate";

    public static final ClassName R = ClassName.get("xyz.rasp.laiquendi.core", "R");

    public static final ClassName CONTEXT         = ClassName.get("android.content", "Context");
    public static final ClassName ACTIVITY        = ClassName.get("android.app", "Activity");
    public static final ClassName ATTRIBUTE_SET   = ClassName.get("android.util", "AttributeSet");
    public static final ClassName VIEW            = ClassName.get("android.view", "View");
    public static final ClassName FRAME_LAYOUT    = ClassName.get("android.widget", "FrameLayout");
    public static final ClassName LAYOUT_INFLATER = ClassName.get("android.view", "LayoutInflater");
    public static final ClassName TYPED_ARRAY     = ClassName.get("android.content.res", "TypedArray");

    public static final AnnotationSpec RESOURCE_TYPE_WARNING = AnnotationSpec.get(new SuppressWarnings() {
        @Override
        public Class<? extends Annotation> annotationType() {
            return SuppressWarnings.class;
        }

        @Override
        public String[] value() {
            return new String[]{"ResourceType"};
        }
    });
}
