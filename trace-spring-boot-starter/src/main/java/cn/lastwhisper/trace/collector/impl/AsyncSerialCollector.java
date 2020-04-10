package cn.lastwhisper.trace.collector.impl;

import cn.lastwhisper.trace.collector.Collector;
import cn.lastwhisper.trace.collector.CollectorHandler;
import cn.lastwhisper.trace.common.RequestHelper;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutorService;

/**
 * 串行收集器
 */
@Component
public class AsyncSerialCollector implements Collector {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CollectorHandler collectorHandler;

    @Autowired
    @Qualifier("singleThreadExecutor")
    private ExecutorService singleThreadExecutor;


    public void before(JoinPoint jp) {
        HttpServletRequest request = RequestHelper.getHttpServletRequest();
        if (request != null) {
            long threadId = Thread.currentThread().getId();
            singleThreadExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    collectorHandler.collector(jp, request, threadId);
                }
            });
        }
    }

    public void exceptionAfter(JoinPoint jp, Throwable throwable) {
        HttpServletRequest request = RequestHelper.getHttpServletRequest();
        if (request != null) {
            long threadId = Thread.currentThread().getId();
            singleThreadExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    collectorHandler.build(throwable, threadId);
                }
            });
        }
    }

    public void after(JoinPoint jp) {
        exceptionAfter(jp, null);
    }

}