package cn.lastwhisper.trace.service;

import cn.lastwhisper.trace.model.Method;
import cn.lastwhisper.trace.model.StaticVo;

import java.util.List;

/**
 * 静态调用链接口
 * @author lastwhisper
 * @date 2020/4/3
 */
public interface StaticTraceService {

    /**
     * 方法总数
     */
    int methodTotal();

    /**
     * 根据起始索引和结束索引查询部分数据
     * @param curr 当前页码
     * @param size 当前页大小
     */
    List<StaticVo> listByPage(int curr, int size);

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
     * 保存Method
     * @param method Method
     */
    void savePage(Method method);

    /**
     * 清空所有数据
     */
    void clear();

}
