package cn.lastwhisper.trace.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 方法运行时的信息
 *
 * @author lastwhisper
 * @date 2020/2/17
 */
public class MethodNode {

    /**
     * 全局唯一(一次Controller、service、dao调用都不同,用于定位一个traceId内的method情况)
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long randomId;

    /**
     * 一次请求调用的唯一标识(一次Controller、service、dao调用共享一个)
     */
    //@JsonSerialize(using = ToStringSerializer.class)
    private Long traceId;

    /**
     * 调用线程id(多次Controller、service、dao调用共享一个,用于统计线程池的情况)
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long threadId;

    /**
     * 同method全局唯一(多次Controller、service、dao调用都相同,用于定位一个method的静态信息)
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long methodId;

    /**
     * 权限修饰符
     */
    private String modifier;

    /**
     * 返回值类型
     */
    private Class<?> returnType;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 参数类型列表(区分重载的方法)
     */
    //private Class<?>[] parameterTypes;
    private Map<String, String> parameters;

    ///**
    // * 参数名称列表(区分重载的方法)
    // */
    //private String[] parameterNames;

    /**
     * 异常列表
     */
    private Class<?>[] exceptionTypes;

    /**
     * 类全限定名
     */
    private String className;
    ///**
    // * 方法静态信息
    // */
    //private MethodInfo methodInfo;

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
    private Date startTime;
    /**
     * 调用结束时间
     */
    private Date endTime;
    /**
     * 耗时
     */
    private Long relativeTime;

    /**
     * 调用的方法
     */
    private List<MethodNode> childNodeList;


    public MethodNode() {
    }


    public Long getRandomId() {
        return randomId;
    }

    public void setRandomId(Long randomId) {
        this.randomId = randomId;
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

    public Long getMethodId() {
        return methodId;
    }

    public void setMethodId(Long methodId) {
        this.methodId = methodId;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public Class<?>[] getExceptionTypes() {
        return exceptionTypes;
    }

    public void setExceptionTypes(Class<?>[] exceptionTypes) {
        this.exceptionTypes = exceptionTypes;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean isException() {
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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getRelativeTime() {
        return relativeTime;
    }

    public void setRelativeTime(Long relativeTime) {
        this.relativeTime = relativeTime;
    }

    public List<MethodNode> getChildNodeList() {
        return childNodeList;
    }

    public void setChildNodeList(List<MethodNode> childNodeList) {
        this.childNodeList = childNodeList;
    }
}
