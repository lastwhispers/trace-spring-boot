package cn.lastwhisper.trace.repository;

import cn.lastwhisper.trace.model.Node;
import cn.lastwhisper.trace.model.RealVo;

import java.util.List;
import java.util.Set;

/**
 * 读写实时调用链
 * @author lastwhisper
 * @date 2020/3/26
 */
public interface RealTraceRepository {

    /**
     * 请求总数
     */
    int requestTotal();

    /**
     * 根据起始索引和结束索引查询部分数据
     * @param start 起始索引
     * @param end 结束索引
     */
    List<RealVo> listByLimit(int start, int end);

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
     */
    void saveHttpNode(Node node);

    /**
     * 保存页面展示信息
     * @param realVo 页面信息
     */
    void savePage(RealVo realVo);

    /**
     * 清空所有数据
     */
    void clear();

    /**
     * 级联删除
     */
    void removePage(Set<RealVo> buffer);
}
