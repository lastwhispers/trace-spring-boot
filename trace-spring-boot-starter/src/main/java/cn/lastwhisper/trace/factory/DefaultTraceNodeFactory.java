package cn.lastwhisper.trace.factory;

import cn.lastwhisper.trace.model.Method;
import cn.lastwhisper.trace.model.Node;

/**
 * 普通结点
 * @author lastwhisper
 * @date 2020/4/4
 */
public class DefaultTraceNodeFactory implements TraceNodeFactory {
    @Override
    public Node getNode() {
        return new Node();
    }

    @Override
    public Method getMethod() {
        return new Method();
    }
}
