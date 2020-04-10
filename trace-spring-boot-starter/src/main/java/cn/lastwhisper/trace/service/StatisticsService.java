package cn.lastwhisper.trace.service;

import java.util.Map;

/**
 *  统计
 * @author lastwhisper
 * @date 2020/4/4
 */
public interface StatisticsService {


    /**
     * 当前请求总数(清除后，重新计数)
     */
    int requestTotal();

    /**
     * 请求总数
     */
    int requestCount();

    /**
     * 所有方法被调用总数
     */
    int callCount();

    /**
     * 方法总数
     */
    int methodCount();

    /**
     * 页面缓存大小
     */
    Map<String, Long> cacheSizeMap();

    /**
     * 监控常量key
     */
    Long cacheSize();


}
