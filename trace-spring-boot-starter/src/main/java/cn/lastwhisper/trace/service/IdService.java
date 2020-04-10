package cn.lastwhisper.trace.service;

import java.util.Map;

/**
 *  Id生成策略服务
 * @author lastwhisper
 * @date 2020/4/2
 */
public interface IdService {

    /**
     * 获取请求次数
     */
    long getTraceCount();

    /**
     * 获取方法被调用次数
     */
    long getNodeCount();

    /**
     * 获取静态方法的数量
     */
    long getSignatureCount();

    /**
     * 统计线程调用请求情况
     * @return java.util.Map<java.lang.Long, java.lang.Long>
     */
    Map<Long, Long> getTraceIdCountMap();

    /**
     * 统计线程调用请求下的方法情况
     * @return java.util.Map<java.lang.Long, java.lang.Long>
     */
    Map<Long, Long> getNodeIdCountMap();

    /**
     * 同线程id内traceId相同
     */
    Long getTraceId(long threadId);

    /**
     * 同线程id内traceId相同
     */
    Long getChainId(long threadId);

    /**
     * 线程id与spanId可能关联
     */
    Long getNodeId(long threadId);

    /**
     * 获取signature对应的Id
     */
    Long getSignatureId(String signature);

    /**
     * 清空线程id对应的traceId，保证每次请求traceId不同
     */
    void clearTraceId(long threadId);

    /**
     * 清空线程id对应的chainId，保证每次请求chainId不同
     */
    void clearChainId(long threadId);

}
