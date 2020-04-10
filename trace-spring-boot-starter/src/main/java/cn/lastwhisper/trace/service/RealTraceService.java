package cn.lastwhisper.trace.service;

import cn.lastwhisper.trace.model.Node;
import cn.lastwhisper.trace.model.RealVo;

import java.util.List;

/**
 * 动态调用链接口
 * @author lastwhisper
 * @date 2020/4/2
 */
public interface RealTraceService {

    /**
     * trace总记录数
     */
    int realTotal();

    /**
     * 根据起始索引和结束索引查询部分数据
     * @param curr 当前页码
     * @param size 当前页大小
     */
    List<RealVo> listByPage(int curr, int size);

    /**
     * 获取一次请求构建的调用链
     * @param traceId 一次请求生成的id
     */
    Node getTraceByTraceId(Long traceId);

    /**
     * 获取方法运行时信息和静态信息
     * @param nodeId 一次请求中的一个方法生成的随机id
     */
    Node getNodeBySpanId(Long nodeId);

    /**
     * 保存一个方法结点
     * @param node 方法结点
     */
    void saveNode(Node node);

    /**
     * 保存一个Controller方法结点
     * @param node Controller结点
     * @param flag 整个调用链是否出现异常
     */
    void saveHttpNode(Node node, Boolean flag);

    /**
     * 保存页面展示信息
     * @param node Controller结点
     * @param flag 整个调用链是否出现异常
     */
    void savePage(Node node, Boolean flag);

    /**
     * 清空所有数据
     */
    void clear();

}
