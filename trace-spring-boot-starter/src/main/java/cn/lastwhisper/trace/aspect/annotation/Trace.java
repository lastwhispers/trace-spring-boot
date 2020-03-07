package cn.lastwhisper.trace.aspect.annotation;

import java.lang.annotation.*;

/**
 * 调用链路监控配置注解
 * @author lastwhisper
 * @date 2020/3/03
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Trace {


}
