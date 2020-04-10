package cn.lastwhisper.trace.collector;

import org.aspectj.lang.JoinPoint;

/**
 * 收集器接口
 * @author lastwhisper
 * @date 2020/4/2
 */
public interface Collector {

    /**
     * 目标方法执行前会调用该方法
     * @param jp org.aspectj.lang.ProceedingJoinPoint
     */
    void before(JoinPoint jp);

    /**
     * 目标方法执行出现异常会调用该方法
     * @param jp org.aspectj.lang.ProceedingJoinPoint
     * @param throwable 异常信息
     */
    void exceptionAfter(JoinPoint jp,Throwable throwable);

    /**
     * 目标方法执行无异常会调用该方法
     * @param jp org.aspectj.lang.ProceedingJoinPoint
     */
    void after(JoinPoint jp);

}
