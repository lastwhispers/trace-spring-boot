package cn.lastwhisper.trace.factory;

import cn.lastwhisper.trace.model.ControllerMethodNode;
import cn.lastwhisper.trace.model.MethodNode;

/**
 *
 * @author lastwhisper
 * @date 2020/3/4
 */
public class ControllerMethodNodeFactory implements NodeFactory {

    @Override
    public MethodNode getMethodNode() {
        return new ControllerMethodNode();
    }

}
