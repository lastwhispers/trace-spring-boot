package cn.lastwhisper.trace.service;

import java.util.Map;

/**
 *  静态资源读取策略
 * @author lastwhisper
 * @date 2020/4/2
 */
public interface ResourceService {

    /**
     *  获取classpath文件的字节数组
     * @param classPath classpath
     * @return byte[] 静态资源字节数组
     */
    byte[] read(String classPath);

    /**
     * 页面缓存大小
     */
    Map<String, Long> cacheSizeMap();
}
