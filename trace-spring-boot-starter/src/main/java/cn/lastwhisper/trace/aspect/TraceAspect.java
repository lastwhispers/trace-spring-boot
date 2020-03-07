package cn.lastwhisper.trace.aspect;

import cn.lastwhisper.trace.util.IdUtil;
import cn.lastwhisper.trace.service.TraceInstance;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author lastwhisper
 * @date 2020/2/17
 */
@Deprecated
//@Aspect
//@Component
public class TraceAspect {

    private static Logger logger = LoggerFactory.getLogger(TraceAspect.class);

    // 一定要排除config包，因为config可能在spring容器创建时被调用，也会被切点表达式织入
    //@Pointcut("execution(public * com.wdd.studentmanager..*(..)) && !within(com.wdd.studentmanager.config.*) && !@annotation(cn.lastwhisper.trace.aspect.annotation.Mark)")
    @Pointcut("execution(public * com.example.demo..*(..))")
    public void matchCondition() {

    }

    /**
     * 1、监控方法调用链
     * 2、监控方法调用时间
     * 3、监控方法调用异常
     * 4、统计top调用、top耗时
     *
     *
     */
    @Around("matchCondition()")
    public Object methodProxy(ProceedingJoinPoint pjp) throws Throwable {

        logger.info("trace begin:{},traceId:{}", pjp.getTarget().getClass().getName(), IdUtil.getTraceId());
        TraceInstance.before(pjp);

        Object result = null;
        try {
            // 方法执行
            result = pjp.proceed();
        } catch (Throwable throwable) {
            TraceInstance.exceptionAfter(throwable);
            // 数据采集不管异常
            throw throwable;
        }
        logger.info("trace end:{},traceId:{}", pjp.getTarget().getClass().getName(), IdUtil.getTraceId());
        TraceInstance.after();

        return result;
    }


}
