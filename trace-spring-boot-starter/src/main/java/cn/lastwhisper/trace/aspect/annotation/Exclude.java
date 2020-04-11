package cn.lastwhisper.trace.aspect.annotation;

import java.lang.annotation.*;

/**
 * 被该注解标识的方法不会被织入
 * @author lastwhisper
 * @date 2020/3/03
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Exclude {


}
