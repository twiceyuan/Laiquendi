package xyz.rasp.laiquendi.processor.types;

import com.squareup.javapoet.ClassName;

import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;

/**
 * Created by twiceYuan on 2017/3/21.
 * <p>
 * Type 操作工具
 */
public class TypeUtil {

    /**
     * 根据 SuperClass 注解获取值
     */
    public static ClassName getSuperClass(Element element) {
        List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();
        for (AnnotationMirror annotationMirror : annotationMirrors) {
            if (annotationMirror.getAnnotationType().toString().equals(Types.ANNOTATION_SUPER_CLASS.toString())) {
                try {
                    return ClassName.bestGuess(annotationMirror
                            .getElementValues()
                            .values()
                            .iterator()
                            .next()
                            .getValue()
                            .toString());
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * 根据注解 ClassName 和 Element（被注解元素）来获取注解值。仅在单注解值有效
     *
     * @param annotationClass 注解类的 ClassName
     * @param element         被注解的元素
     * @return 注解的值
     */
    public static Object getAnnotationSingleValue(ClassName annotationClass, Element element) {
        List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();
        for (AnnotationMirror annotationMirror : annotationMirrors) {
            DeclaredType annotationType = annotationMirror.getAnnotationType();
            if (annotationType.toString().equals(annotationClass.toString())) {
                return annotationMirror.getElementValues().values().iterator().next().getValue();
            }
        }
        return null;
    }
}
