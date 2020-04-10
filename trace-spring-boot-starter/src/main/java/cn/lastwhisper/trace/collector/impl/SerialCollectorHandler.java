package cn.lastwhisper.trace.collector.impl;

import cn.lastwhisper.trace.collector.CollectorHandler;
import cn.lastwhisper.trace.common.util.CollectionUtil;
import cn.lastwhisper.trace.common.util.CollectorUtil;
import cn.lastwhisper.trace.model.ExceptionInfo;
import cn.lastwhisper.trace.model.Method;
import cn.lastwhisper.trace.model.Node;
import cn.lastwhisper.trace.service.IdService;
import cn.lastwhisper.trace.service.RealTraceService;
import cn.lastwhisper.trace.service.StaticTraceService;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 串行化收集器管理者
 * @author lastwhisper
 * @date 2020/2/17
 */
@Component
public class SerialCollectorHandler implements CollectorHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    // 某线程的调用链
    private Map<Long, LinkedList<Node>> nodeStackMap = new HashMap<>();
    // 某线程的静态调用链
    //private Map<Long, LinkedList<MethodInfo>> methodStackMap = new HashMap<>();
    // 某线程是否有异常
    private Map<Long, Boolean> flagMap = new HashMap<>();

    @Autowired
    private RealTraceService realTraceService;

    @Autowired
    private StaticTraceService staticTraceService;

    @Autowired
    private IdService idService;

    interface Persistence {
        void callback(Method method);
    }

    /**
     * 目标方法执行前收集信息
     *
     * @param jp ProceedingJoinPoint
     * @param request HttpServletRequest
     * @param threadId threadId
     */
    public void collector(JoinPoint jp, HttpServletRequest request, long threadId) {
        LinkedList<Node> nodeStack = nodeStackMap.get(threadId);
        // 创建线程关联栈
        if (CollectionUtil.isNull(nodeStack)) {
            nodeStack = new LinkedList<>();
            nodeStackMap.put(threadId, nodeStack);
        }
        Node node;
        if (nodeStack.isEmpty()) {
            node = CollectorUtil.getHttpNode(request, jp);
            logger.info("Collect begin {} request", idService.getTraceCount() + 1);
        } else {
            node = CollectorUtil.getDefaultNode(jp);
        }
        // 设置node的id
        node.setThreadId(threadId);
        node.setNodeId(idService.getNodeId(threadId));
        node.setTraceId(idService.getTraceId(threadId));
        // 构建关系
        nodeStack.push(node);
    }

    /**
     * 目标方法执行结束后调用
     * @param throwable throwable
     * @param threadId threadId
     */
    public void build(Throwable throwable, long threadId) {
        LinkedList<Node> nodeStack = nodeStackMap.get(threadId);
        if (CollectionUtil.isEmpty(nodeStack)) {
            logger.warn("The call chain cannot be empty. thread id:{} ", threadId);
        } else if (nodeStack.size() >= 2) {
            // 构建实时调用链关系
            Node node = nodeStack.pop();
            Node pNode = nodeStack.pop();
            nodeRelation(node, pNode);
            appendException(node, throwable, threadId);
            // 构建静态调用链关系
            Method method = node.getMethod();
            Method pMethod = pNode.getMethod();
            methodRelation(method, pMethod);
            staticMethod(method, threadId, new Persistence() {
                @Override
                public void callback(Method method) {
                    staticTraceService.saveMethod(method);
                }
            });
            // 持久化
            realTraceService.saveNode(node);
            nodeStack.push(pNode);
        } else {
            logger.info("Collect end {} request", idService.getTraceCount());
            /*============================实时调用链=================================*/
            Node node = nodeStack.pop();
            appendException(node, throwable, threadId);
            // 该线程内是否出现异常，默认无异常
            Boolean flag = flagMap.getOrDefault(threadId, false);
            flagMap.put(threadId, false);
            /*=============================静态调用链================================*/
            Method method = node.getMethod();
            staticMethod(method, threadId, new Persistence() {
                @Override
                public void callback(Method method) {
                    staticTraceService.saveMethod(method);
                    staticTraceService.saveHttpMethod(method);
                }
            });

            realTraceService.saveNode(node);
            realTraceService.saveHttpNode(node, flag);
            idService.clearTraceId(threadId);
            idService.clearChainId(threadId);
        }

    }

    /**
     * 构建节点关系
     *
     * @param node 子节点
     * @param pNode 父节点
     */
    private void nodeRelation(Node node, Node pNode) {
        List<Node> nodeChild = pNode.getChildren();
        if (CollectionUtil.isNull(nodeChild)) {
            nodeChild = new ArrayList<>();
            pNode.setChildren(nodeChild);
        }
        nodeChild.add(node);
    }

    /**
     *  节点追加异常信息
     * @param node 子节点
     * @param throwable 异常
     * @param threadId 主线程id
     */
    private void appendException(Node node, Throwable throwable, long threadId) {
        // 有异常
        if (throwable != null) {
            node.setExceptionInfo(new ExceptionInfo(throwable.getStackTrace()[0],
                    throwable.getMessage(), throwable.getClass().getName()));
            node.setException(true);
            flagMap.put(threadId, true);
            logger.warn("threadId:{},method name:{},exception type:{}", threadId, node.getMethod().getName(), throwable.getClass().getName());
        } else {
            node.setException(false);
        }
        node.setEnd(new Date());
        node.setRelative(node.getEnd().getTime() - node.getStart().getTime());
    }

    /**
     * 构建方法关系
     *
     * @param method 子方法
     * @param pMethod 父方法
     */
    private void methodRelation(Method method, Method pMethod) {
        List<Method> methodChild = pMethod.getChildren();
        if (CollectionUtil.isNull(methodChild)) {
            methodChild = new ArrayList<>();
            pMethod.setChildren(methodChild);
        }
        methodChild.add(method);
    }


    private void staticMethod(Method method, long threadId, Persistence persistence) {
        // 获取方法的签名
        String signature = CollectorUtil.getSignature(method);
        // 签名映射签名id
        Long signatureId = idService.getSignatureId(signature);
        method.setSignatureId(signatureId);
        // 根据签名id获取方法
        Method existMethod = staticTraceService.getMethodBySignatureId(signatureId);
        if (!CollectorUtil.isSame(method, existMethod)) {
            Long chainId = idService.getChainId(threadId);
            method.setChainId(chainId);
            persistence.callback(method);
        }

    }

}
