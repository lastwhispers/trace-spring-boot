package cn.lastwhisper.trace.repository.impl;

import cn.lastwhisper.trace.common.collection.ConcurrentEventLruCacheMap;
import cn.lastwhisper.trace.common.collection.ConcurrentLruCacheMap;
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
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *  内存多读一写实现
 *
 * @author lastwhisper
 * @date 2020/4/2
 */
@Repository
public class RealTraceInMemoryRepository implements RealTraceRepository {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // 页面展示信息
    private List<RealVo> pages = new CopyOnWriteArrayList<>();//多读一写
    // nodeId,Node(某个method运行时信息)
    @Autowired
    @Qualifier(value = "nodeIdMap")
    private ConcurrentLruCacheMap<Long, Node> nodeIdMap;
    // traceId,Node(完整运行时调用链)
    @Autowired
    @Qualifier(value = "traceIdMap")
    private ConcurrentEventLruCacheMap<Long, Node> traceIdMap;

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
        Node node = traceIdMap.syncGet(traceId);
        if (node == null) {
            logger.debug("traceId:{} not find Trace", traceId);
        }
        return node;
    }

    @Override
    public Node getNodeBySpanId(Long nodeId) {
        Node node = nodeIdMap.syncGet(nodeId);
        if (node == null) {
            logger.debug("randomId:{} not find MethodNode", nodeId);
        }
        return node;
    }

    @Override
    public void saveNode(Node node) {
        nodeIdMap.syncPut(node.getNodeId(), node);
    }

    @Override
    public void saveHttpNode(Node node) {
        traceIdMap.syncPut(node.getTraceId(), node);
    }

    @Override
    public void savePage(RealVo realVo) {
        pages.add(realVo);
    }

    @Override
    public void clear() {
        pages.clear();
        nodeIdMap.syncClear();
        traceIdMap.syncClear();
    }

    @Override
    public void removePage(Set<RealVo> buffer) {
        /*
         * pages -->1,2,3,4.....page.size-1
         * buffer-->1,2,3,4.....buffer.size-1
         * pages.size = n
         * buffer.size = m
         * 调用pages.size次buffer.contains(pages[1~n-1])即可
         * 时间复杂度：n
         */
        pages.removeAll(buffer);
    }

}
