package cn.lastwhisper.trace.collector;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;

/**
 * 收集并处理数据
 * @author lastwhisper
 * @date 2020/4/2
 */
public interface CollectorHandler {

    /**
     * 收集数据
     */
    void collector(JoinPoint pjp, HttpServletRequest request, long threadId);

    /**
     * 处理数据
     */
    void build(Throwable throwable, long threadId);

}
