package cn.lastwhisper.trace.config;

import cn.lastwhisper.trace.common.collection.ConcurrentEventLruCacheMap;
import cn.lastwhisper.trace.common.collection.ConcurrentLruCacheMap;
import cn.lastwhisper.trace.event.CascadeEventPublisher;
import cn.lastwhisper.trace.model.Node;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 默认配置
 * @author lastwhisper
 * @date 2020/4/10
 */
@Configuration
public class DefaultConfig {

    /**
     * 单线程池
     */
    @Bean
    public ExecutorService singleThreadExecutor() {
        return new ThreadPoolExecutor(1, 1, 0,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), Executors.defaultThreadFactory(), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
                System.out.println("Task discarded queue size:" + executor.getQueue().size());
            }
        });
    }

    @Bean
    public ConcurrentLruCacheMap<Long, Node> nodeIdMap() {
        return new ConcurrentLruCacheMap<>(2000);
    }

    @Bean
    public ConcurrentEventLruCacheMap<Long, Node> traceIdMap() {
        return new ConcurrentEventLruCacheMap<>(100, 10, cascadeEventPublisher());
    }

    /**
     * 消息事件推送者
     */
    @Bean
    public CascadeEventPublisher<Long> cascadeEventPublisher() {
        return new CascadeEventPublisher<>();
    }

}
