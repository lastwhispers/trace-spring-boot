package cn.lastwhisper.trace.processor.annotation;

import java.lang.annotation.*;

/**
 * apt扫描注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface EnableTrace {
    String value() default "";
}
