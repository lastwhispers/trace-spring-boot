package cn.lastwhisper.trace.common.collection;

import cn.lastwhisper.trace.event.CascadeEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 固定大小lru策略淘汰数据的{@link LinkedHashMap} 实现
 *
 *  同步事件通知lru缓存
 *
 * @author lastwhisper
 * @date 2020/3/11
 * @param <K> 键类型
 * @param <V> 值类型
 */
public class ConcurrentEventLruCacheMap<K, V> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // 读写锁
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock readLock = readWriteLock.readLock();

    private final Lock writeLock = readWriteLock.writeLock();

    private EventLruCacheMap cache;

    /**
     *
     * @param capacity LRU大小
     * @param bufferSize 缓冲区大小
     */
    public ConcurrentEventLruCacheMap(int capacity, Integer bufferSize, CascadeEventPublisher<K> cascadeEventPublisher) {
        this.cache = new EventLruCacheMap(capacity, bufferSize, cascadeEventPublisher);
    }

    private class EventLruCacheMap extends LinkedHashMap<K, V> {
        /** 容量，超过此容量自动删除末尾元素 */
        private int capacity;
        /** 缓冲大小 */
        private Integer bufferSize;
        /** 缓冲区 */
        private List<K> buffers;
        /** 级联事件推送者 */
        private CascadeEventPublisher<K> publisher;

        /**
         * @param capacity LRU大小
         * @param bufferSize 缓冲区大小
         */
        public EventLruCacheMap(int capacity, Integer bufferSize, CascadeEventPublisher<K> cascadeEventPublisher) {
            super(capacity, 0.75f, true);
            this.capacity = capacity;
            this.bufferSize = bufferSize;
            buffers = new ArrayList<>(bufferSize);
            publisher = cascadeEventPublisher;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            boolean flag = size() > capacity;
            if (flag) {
                buffers.add(eldest.getKey());
                if (buffers.size() >= bufferSize) {
                    logger.debug("Trigger delete buffer:{}", buffers.toString());
                    publisher.sendRemoveMessage(new ArrayList<>(buffers));
                    buffers.clear();
                }
                logger.debug("There are still {} triggers to delete the buffer. buffer traceId:{} buffer Size:{}",
                        bufferSize - buffers.size(), eldest.getKey(), buffers.size());
            }
            return flag;
        }

    }

    /**
     * 同步写
     *
     * @param k k
     * @param v v
     */
    public V put(K k, V v) {
        writeLock.lock();
        try {
            return this.cache.put(k, v);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 同步读
     *
     * @param k k
     */
    public V get(K k) {
        readLock.lock();
        try {
            return this.cache.get(k);
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 同步写
     */
    public void clear() {
        writeLock.lock();
        try {
            this.cache.clear();
        } finally {
            writeLock.unlock();
        }
    }

}
