package cn.lastwhisper.trace.factory;

import cn.lastwhisper.trace.model.Method;
import cn.lastwhisper.trace.model.Node;

/**
 * 抽象工厂
 * @author lastwhisper
 * @date 2020/4/4
 */
public interface TraceNodeFactory {

    Node getNode();

    Method getMethod();

}
