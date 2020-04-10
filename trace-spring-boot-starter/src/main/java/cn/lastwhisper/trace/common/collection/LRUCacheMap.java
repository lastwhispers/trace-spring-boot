package cn.lastwhisper.trace.common.collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 固定大小lru策略淘汰数据的{@link LinkedHashMap} 实现
 *
 * @author lastwhisper
 * @date 2020/3/11
 * @param <K> 键类型
 * @param <V> 值类型
 */
public class LRUCacheMap<K, V> extends LinkedHashMap<K, V> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /** 容量，超过此容量自动删除末尾元素 */
    private int capacity;

    public LRUCacheMap(int capacity) {
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
