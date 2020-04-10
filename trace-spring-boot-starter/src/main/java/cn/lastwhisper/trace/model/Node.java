package cn.lastwhisper.trace.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.util.Date;
import java.util.List;

/**
 * 方法运行时的信息
 *
 * @author lastwhisper
 * @date 2020/2/17
 */
public class Node {

    /**
     * 全局唯一(method的运行时信息)
     */
    //@JsonSerialize(using = ToStringSerializer.class)
    private Long nodeId;

    /**
     * 一次请求调用的唯一标识(一次Controller、service、dao调用共享一个)
     */
    //@JsonSerialize(using = ToStringSerializer.class)
    private Long traceId;

    /**
     * 调用线程id(多次Controller、service、dao调用共享一个,用于统计线程池的情况)
     *
     */
    private Long threadId;

    /**
     * 同method全局唯一(method的静态信息)
     */
    //private Long signatureId;

    /**
     * 方法静态信息
     */
    private Method method;

    /**
     * 是否出现异常
     */
    private boolean exception;

    /**
     * 异常名称
     */
    private ExceptionInfo exceptionInfo;

    /**
     * 调用开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date start;
    /**
     * 调用结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date end;
    /**
     * 耗时
     */
    private Long relative;

    /**
     * 调用的方法
     */
    private List<Node> children;

    public Node() {

    }

    public Node(Long nodeId, Long traceId, Long threadId, Method method, boolean exception, ExceptionInfo exceptionInfo, Date start, Date end, Long relative, List<Node> children) {
        this.nodeId = nodeId;
        this.traceId = traceId;
        this.threadId = threadId;
        this.method = method;
        this.exception = exception;
        this.exceptionInfo = exceptionInfo;
        this.start = start;
        this.end = end;
        this.relative = relative;
        this.children = children;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public Long getTraceId() {
        return traceId;
    }

    public void setTraceId(Long traceId) {
        this.traceId = traceId;
    }

    public Long getThreadId() {
        return threadId;
    }

    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public boolean getException() {
        return exception;
    }

    public void setException(boolean exception) {
        this.exception = exception;
    }

    public ExceptionInfo getExceptionInfo() {
        return exceptionInfo;
    }

    public void setExceptionInfo(ExceptionInfo exceptionInfo) {
        this.exceptionInfo = exceptionInfo;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Long getRelative() {
        return relative;
    }

    public void setRelative(Long relative) {
        this.relative = relative;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }


    @Override
    public String toString() {
        return "Node{" +
                "nodeId=" + nodeId +
                ", traceId=" + traceId +
                ", threadId=" + threadId +
                ", methodName=" + method.getName() +
                ", start=" + start +
                ", end=" + end +
                ", relative=" + relative +
                '}';
    }
}
