package cn.lastwhisper.trace.factory;

import cn.lastwhisper.trace.model.HttpMethod;
import cn.lastwhisper.trace.model.HttpNode;
import cn.lastwhisper.trace.model.Method;
import cn.lastwhisper.trace.model.Node;

/**
 * 普通结点
 * @author lastwhisper
 * @date 2020/4/4
 */
public class HttpTraceNodeFactory implements TraceNodeFactory {
    @Override
    public Node getNode() {
        return new HttpNode();
    }

    @Override
    public Method getMethod() {
        return new HttpMethod();
    }
}
