package cn.lastwhisper.trace.common.collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 固定大小lru策略淘汰数据的{@link LinkedHashMap} 实现
 *
 *  同步lru缓存
 *
 * @author lastwhisper
 * @date 2020/3/11
 * @param <K> 键类型
 * @param <V> 值类型
 */
public class ConcurrentLruCacheMap<K, V> {
    // 读写锁
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock readLock = readWriteLock.readLock();

    private final Lock writeLock = readWriteLock.writeLock();

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private LruCacheMap cache;

    public ConcurrentLruCacheMap(int capacity) {
        this.cache = new LruCacheMap(capacity);
    }

    private class LruCacheMap extends LinkedHashMap<K, V> {

        /** 容量，超过此容量自动删除末尾元素 */
        private int capacity;

        public LruCacheMap(int capacity) {
            super(capacity, 0.75f, true);
            this.capacity = capacity;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            boolean flag = size() > capacity;
            if (flag) {
                logger.debug("remove k:{},v:{}", eldest.getKey(), eldest.getValue());
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
