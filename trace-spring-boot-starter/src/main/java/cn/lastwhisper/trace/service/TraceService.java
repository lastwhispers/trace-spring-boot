package cn.lastwhisper.trace.service;

import cn.lastwhisper.trace.model.ControllerMethodNode;
import cn.lastwhisper.trace.model.MethodNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author lastwhisper
 * @date 2020/2/17
 */
@Service
public class TraceService {

    // 调用链
    //private static LinkedList<MethodNode> callChainList = new LinkedList<>();
    // traceId,url、http method、traceId
    private static Map<Long, String> traceIdUrlMap = new ConcurrentHashMap<>();//防止rehash时链表循环引用
    // randomId,MethodNode(某个method)
    private static Map<Long, MethodNode> randomIdMap = new ConcurrentHashMap<>();
    // traceId,MethodNode(完整实时调用链)
    private static Map<Long, MethodNode> traceIdChainMap = new ConcurrentHashMap<>();

    private Logger logger = LoggerFactory.getLogger(TraceService.class);

    /**
     * 获取controller的url、http method
     */
    public Collection<String> getTraceUrl() {
        return traceIdUrlMap.values();
    }

    /**
     * 获取controller的url、http method
     */
    public MethodNode getCallChain(Long traceId) {
        return traceIdChainMap.get(traceId);
    }

    public MethodNode getMethodNode(Long randomId) {
        MethodNode methodNode = randomIdMap.get(randomId);
        if (methodNode == null) {
            logger.info("randomId:{} not find MethodNode", randomId);
        }
        return methodNode;
    }

    /**
     *
     */
    public void saveMethodNode(MethodNode methodNode) {
        Long randomId = methodNode.getRandomId();
        if (randomIdMap.containsKey(randomId)) {
            logger.info("saveMethodNode randomId:{} duplicate", randomId);
        } else {
            logger.info("saveMethodNode randomId:{} save success", randomId);
        }
        randomIdMap.put(randomId, methodNode);
    }

    public void saveController(MethodNode methodNode) {
        ControllerMethodNode controller = (ControllerMethodNode) methodNode;
        // 维护traceId和调用链的关系
        traceIdChainMap.put(controller.getTraceId(), controller);
        // 保存前缀
        //callChainList.push(node);
        traceIdUrlMap.put(controller.getTraceId(), controller.getHttpPath() +
                "#" + controller.getHttpMethod() + "#" + controller.getTraceId());
    }

    public void clear() {
        traceIdUrlMap.clear();
        traceIdChainMap.clear();
        randomIdMap.clear();
    }

}
