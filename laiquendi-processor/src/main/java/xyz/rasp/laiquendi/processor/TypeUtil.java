package xyz.rasp.laiquendi.processor;

import javax.lang.model.element.Element;
import javax.lang.model.type.MirroredTypeException;

import xyz.rasp.laiquendi.core.SuperClass;

/**
 * Created by twiceYuan on 2017/3/21.
 * <p>
 * Type 操作工具
 */
public class TypeUtil {

    public static Class getSuperClass(Element element) {
        SuperClass superClass = element.getAnnotation(SuperClass.class);
        try {
            return superClass.value();
        } catch (MirroredTypeException e) {
            try {
                return Class.forName(e.getTypeMirror().toString());
            } catch (ClassNotFoundException e1) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
