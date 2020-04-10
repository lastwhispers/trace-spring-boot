package cn.lastwhisper.trace.aspect;

import cn.lastwhisper.trace.collector.Collector;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author lastwhisper
 * @date 2020/2/17
 */
@Deprecated
//@Aspect
//@Component
public class CollectorAspect {

    @Autowired
    private Collector collector;

    @Pointcut("(execution(public * com.example.demo..*(..)) || @annotation(cn.lastwhisper.trace.aspect.annotation.Include)) && !@annotation(cn.lastwhisper.trace.aspect.annotation.Exclude)")
    public void matchCondition() {

    }

    @Around("matchCondition()")
    public Object methodProxy(ProceedingJoinPoint pjp) throws Throwable {
        collector.before(pjp);
        Object result;
        try {
            result = pjp.proceed();
        } catch (Throwable throwable) {
            collector.exceptionAfter(pjp, throwable);
            throw throwable;
        }
        collector.after(pjp);
        return result;
    }


}
