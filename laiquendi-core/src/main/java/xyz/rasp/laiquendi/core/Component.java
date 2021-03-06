package xyz.rasp.laiquendi.core;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static android.view.View.NO_ID;

/**
 * Created by twiceYuan on 2017/3/20.
 * <p>
 * Layout Id
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface Component {
    int value() default NO_ID;
}
