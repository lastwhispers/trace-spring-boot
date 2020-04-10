package cn.lastwhisper.trace.repository;

import cn.lastwhisper.trace.model.Method;
import cn.lastwhisper.trace.model.StaticVo;

import java.util.List;

/**
 * 读写静态调用链
 * @author lastwhisper
 * @date 2020/3/26
 */
public interface StaticTraceRepository {

    /**
     * 方法总数
     */
    int methodTotal();

    /**
     * 根据起始索引和结束索引查询部分数据
     * @param start 起始索引
     * @param end 结束索引
     */
    List<StaticVo> listByLimit(int start, int end);

    /**
     * 获取静态调用链
     * @param chainId 签名对应的id
     */
    Method getChainByChainId(Long chainId);

    /**
     * 获取方法静态信息
     * @param signatureId 签名对应的id
     */
    Method getMethodBySignatureId(Long signatureId);

    /**
     * 保存一个方法静态信息
     * @param method 方法静态信息
     */
    void saveMethod(Method method);

    /**
     * 保存一个Controller方法静态信息
     * @param method 方法静态信息
     */
    void saveHttpMethod(Method method);

    /**
     * 保存staticVo
     * @param staticVo staticVo
     */
    void savePage(StaticVo staticVo);

    /**
     * 清空所有数据
     */
    void clear();

}
