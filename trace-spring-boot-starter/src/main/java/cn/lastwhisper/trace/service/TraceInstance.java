package cn.lastwhisper.trace.service;

import cn.lastwhisper.trace.common.ApplicationContextHelper;
import cn.lastwhisper.trace.util.CollectionUtil;
import cn.lastwhisper.trace.util.IdUtil;
import cn.lastwhisper.trace.util.JoinPointParesUtil;
import cn.lastwhisper.trace.model.ExceptionInfo;
import cn.lastwhisper.trace.model.MethodNode;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 *
 * @author lastwhisper
 * @date 2020/2/17
 */
public class TraceInstance {

    /**
     * k:threadId,v:call stack
     *
     * 线程池会复用线程，id会重新出现，是否可以只更新时间不更新调用链？
     * 不可以，有可能参数不同调用链不同
     *
     */
    private static Map<Long, LinkedList<MethodNode>> threadIdStackMap = new HashMap<>();

    private static Logger logger = LoggerFactory.getLogger(TraceInstance.class);

    /**
     * 方法进入时调用
     */
    public static void before(ProceedingJoinPoint pjp) {
        long threadId = Thread.currentThread().getId();
        LinkedList<MethodNode> stack = threadIdStackMap.get(threadId);
        // 线程池会复用线程，所以stack只会在线程第一次出现的时候被创建
        if (CollectionUtil.isNull(stack)) {
            stack = new LinkedList<>();
            threadIdStackMap.put(threadId, stack);
        }
        MethodNode methodNode;
        if (stack.isEmpty()) {
            // controller调用时
            methodNode = JoinPointParesUtil.getControllerMethodNode(pjp);
        } else {
            methodNode = JoinPointParesUtil.getMethodNode(pjp);
        }
        stack.push(methodNode);
    }

    /**
     * 方法无异常结束时调用
     */
    public static void after() {
        exceptionAfter(null);
    }

    /**
     * 方法异常结束时调用
     */
    public static void exceptionAfter(Throwable throwable) {
        long threadId = Thread.currentThread().getId();
        LinkedList<MethodNode> stack = threadIdStackMap.get(threadId);
        if (CollectionUtil.isEmpty(stack)) {
            logger.warn("警告线程id:{}下的调用栈不应该为空", threadId);
        } else if (stack.size() >= 2) {
            MethodNode methodNode = stack.pop();
            MethodNode parentMethod = stack.pop();
            List<MethodNode> childNodeList = parentMethod.getChildNodeList();
            if (CollectionUtil.isNull(childNodeList)) {
                childNodeList = new ArrayList<>();
                parentMethod.setChildNodeList(childNodeList);
            }

            // 有异常
            if (throwable != null) {
                ExceptionInfo exceptionInfo = new ExceptionInfo();
                exceptionInfo.setStackTraceElement(throwable.getStackTrace()[0]);
                exceptionInfo.setDetailMessage(throwable.getMessage());
                exceptionInfo.setExceptionName(throwable.getClass().getName());
                methodNode.setExceptionInfo(exceptionInfo);
                methodNode.setException(true);
            } else {
                methodNode.setException(false);
            }

            methodNode.setEndTime(new Date());
            methodNode.setRelativeTime(methodNode.getEndTime().getTime() - methodNode.getStartTime().getTime());

            saveGeneral(methodNode);
            childNodeList.add(methodNode);
            stack.push(parentMethod);
        } else {
            MethodNode methodNode = stack.pop();
            methodNode.setEndTime(new Date());
            methodNode.setRelativeTime(methodNode.getEndTime().getTime() - methodNode.getStartTime().getTime());

            saveController(methodNode);
            saveGeneral(methodNode);
            stack.clear();
            IdUtil.clearTraceId();
        }

    }

    private static void saveController(MethodNode methodNode) {
        try {
            TraceService traceService = ApplicationContextHelper.getBean(TraceService.class);
            traceService.saveController(methodNode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveGeneral(MethodNode methodNode) {
        try {
            TraceService traceService = ApplicationContextHelper.getBean(TraceService.class);
            traceService.saveMethodNode(methodNode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
