package cn.lastwhisper.trace.repository.impl;

import cn.lastwhisper.trace.model.Node;
import cn.lastwhisper.trace.model.RealVo;
import cn.lastwhisper.trace.repository.RealTraceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *  内存多读一写实现
 *
 * @author lastwhisper
 * @date 2020/4/2
 */
@Repository
public class RealTraceInMemoryRepository implements RealTraceRepository {
    // 读写锁
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // 页面展示信息
    private List<RealVo> pages = new CopyOnWriteArrayList<>();//多读一写
    // nodeId,Node(某个method运行时信息)
    @Autowired
    @Qualifier(value = "nodeIdMap")
    private Map<Long, Node> nodeIdMap;
    // traceId,Node(完整运行时调用链)
    @Autowired
    @Qualifier(value = "traceIdMap")
    private Map<Long, Node> traceIdMap;

    @Override
    public int requestTotal() {
        return pages.size();
    }

    @Override
    public List<RealVo> listByLimit(int start, int end) {
        List<RealVo> result = new ArrayList<>(end - start);
        logger.debug("listByLimit start:{}\tend:{}", start, end);
        for (int i = start; i <= end; i++) {
            result.add(pages.get(i));
        }
        return result;
    }

    @Override
    public Node getTraceByTraceId(Long traceId) {
        readLock.lock();
        Node node;
        try {
            node = traceIdMap.get(traceId);
            if (node == null) {
                logger.debug("traceId:{} not find Trace", traceId);
            }
        } finally {
            readLock.unlock();
        }
        return node;
    }

    @Override
    public Node getNodeBySpanId(Long nodeId) {
        readLock.lock();
        Node node;
        try {
            node = nodeIdMap.get(nodeId);
            if (node == null) {
                logger.debug("randomId:{} not find MethodNode", nodeId);
            }
        } finally {
            readLock.unlock();
        }
        return node;
    }

    @Override
    public void saveNode(Node node) {
        writeLock.lock();
        try {
            nodeIdMap.put(node.getNodeId(), node);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void saveHttpNode(Node node) {
        writeLock.lock();
        try {
            traceIdMap.put(node.getTraceId(), node);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void savePage(RealVo realVo) {
        writeLock.lock();
        try {
            pages.add(realVo);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void clear() {
        writeLock.lock();
        try {
            pages.clear();
            traceIdMap.clear();
            nodeIdMap.clear();
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void removePage(Set<RealVo> buffer) {
        /*
         * pages -->1,2,3,4.....page.size-1
         * buffer-->1,2,3,4.....buffer.size-1
         *
         * 调用buffer.size次buffer.contains(pages)即可
         * pages*buffer
         *
         */
        pages.removeAll(buffer);
    }

}
