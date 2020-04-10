package cn.lastwhisper.trace.aspect.annotation;

import java.lang.annotation.*;

/**
 * 被该注解标识的方法会被织入（切点表达式扫全包时，该注解无用）
 * @author lastwhisper
 * @date 2020/3/03
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Include {


}
