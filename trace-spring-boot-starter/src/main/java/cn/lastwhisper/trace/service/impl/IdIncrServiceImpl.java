package cn.lastwhisper.trace.service.impl;

import cn.lastwhisper.trace.service.IdService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 自增Id服务
 * @author lastwhisper
 * @date 2020/4/2
 */
@Service
public class IdIncrServiceImpl implements IdService {

    /**
     * Thread association trace id
     *
     * k:thread id,v:one request
     */
    private Map<Long, Long> traceIdMap = new HashMap<>();

    /**
     * Thread association method id
     *
     * k:thread id,v:method id
     */
    private Map<Long, Long> chainIdMap = new HashMap<>();

    /**
     * 1.For statistical
     * 2.Cache trace data will be eliminated by lru
     *
     * k:thread id,v:http method invocation count
     */
    private Map<Long, Long> traceIdCountMap = new HashMap<>();

    /**
     * 1.For statistical
     * 2.Cache trace data will be eliminated by lru
     *
     * k:thread id,v:http method invocation count
     */
    private Map<Long, Long> chainIdCountMap = new HashMap<>();

    /**
     * 1.For statistical
     * 2.Cache method data will be eliminated by lru
     *
     * k:thread id,v:method invocation count
     */
    private Map<Long, Long> nodeIdCountMap = new HashMap<>();

    /**
     * Method signature association signature id
     *
     * k:signature,v:signature id
     */
    private Map<String, Long> signatureIdMap = new HashMap<>();

    private long traceIdCounter = 0L;

    private long nodeIdCounter = 0L;

    private long chainIdCounter = 0L;

    private long signatureIdCounter = 0L;

    /**
     * 获取请求的数量
     */
    @Override
    public long getTraceCount() {
        return traceIdCounter;
    }

    /**
     * 获取所有方法被调用的次数
     */
    @Override
    public long getNodeCount() {
        return nodeIdCounter;
    }

    /**
     * 获取静态方法的数量
     */
    @Override
    public long getSignatureCount() {
        return signatureIdCounter;
    }

    @Override
    public Map<Long, Long> getTraceIdCountMap() {
        return traceIdCountMap;
    }

    @Override
    public Map<Long, Long> getNodeIdCountMap() {
        return nodeIdCountMap;
    }

    /**
     * 获取traceId
     *
     * traceId依赖于threadId，同线程id内traceId相同
     */
    @Override
    public Long getTraceId(long threadId) {
        Long traceId = traceIdMap.get(threadId);
        if (traceId == null) {
            traceId = incrTraceId();
            traceIdMap.put(threadId, traceId);
            // 记录线程id对应
            Long threadIdCounter = traceIdCountMap.getOrDefault(threadId, 0L);
            traceIdCountMap.put(threadId, ++threadIdCounter);
        }
        return traceId;
    }

    /**
     * 获取traceId
     *
     * chainId依赖于threadId，同线程id内chainId相同
     */
    @Override
    public Long getChainId(long threadId) {
        Long chainId = chainIdMap.get(threadId);
        if (chainId == null) {
            chainId = incrChainId();
            chainIdMap.put(threadId, chainId);
            // 记录线程id对应
            Long threadIdCounter = chainIdCountMap.getOrDefault(threadId, 0L);
            chainIdCountMap.put(threadId, ++threadIdCounter);
        }
        return chainId;
    }

    /**
     * 获取nodeId
     *
     *  nodeId不依赖于threadId，主要用于统计
     */
    @Override
    public Long getNodeId(long threadId) {
        long nodeId = incrNodeId();
        // 记录线程id对应
        Long threadIdCounter = nodeIdCountMap.getOrDefault(threadId, 0L);
        nodeIdCountMap.put(threadId, ++threadIdCounter);
        return nodeId;
    }

    /**
     * 获取signature对应的Id
     */
    @Override
    public Long getSignatureId(String signature) {
        Long signatureId = signatureIdMap.get(signature);
        if (signatureId == null) {
            signatureId = incrSignatureId();
            signatureIdMap.put(signature, signatureId);
        }
        return signatureId;
    }

    /**
     * 清空线程id对应的traceId，保证每次请求traceId不同
     */
    @Override
    public void clearTraceId(long threadId) {
        traceIdMap.remove(threadId);
    }

    @Override
    public void clearChainId(long threadId) {
        chainIdMap.remove(threadId);
    }

    /**
     * 增加trace数量
     */
    private long incrTraceId() {
        return ++traceIdCounter;
    }

    /**
     * 增加Node数量
     */
    private long incrChainId() {
        return ++chainIdCounter;
    }

    /**
     * 增加Node数量
     */
    private long incrNodeId() {
        return ++nodeIdCounter;
    }

    /**
     * 增加signature数量
     */
    private long incrSignatureId() {
        return ++signatureIdCounter;
    }

}
