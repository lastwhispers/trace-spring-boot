package cn.lastwhisper.trace.aspect.annotation;

import java.lang.annotation.*;

/**
 * 被该注解标识的类不会被监控
 * @author lastwhisper
 * @date 2020/3/03
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Mark {


}
