package cn.lastwhisper.trace.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.Objects;

/**
 *
 * @author lastwhisper
 * @date 2020/3/10
 */
public class RealVo {

    //@JsonSerialize(using = ToStringSerializer.class)
    private Long traceId;
    /**
     * 请求的URI
     */
    private String uri;

    /**
     * className
     */
    private String className;

    /**
     * methodName
     */
    private String methodName;

    /**
     * Http请求方式
     */
    private String httpMethod;

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
     * 是否出现异常
     */
    private Boolean flag;

    public RealVo() {
    }

    public RealVo(Long traceId) {
        this.traceId = traceId;
    }

    public RealVo(Long traceId, String uri, String className, String methodName, String httpMethod, Date start, Date end, Long relative, Boolean flag) {
        this.traceId = traceId;
        this.uri = uri;
        this.className = className;
        this.methodName = methodName;
        this.httpMethod = httpMethod;
        this.start = start;
        this.end = end;
        this.relative = relative;
        this.flag = flag;
    }

    public Long getTraceId() {
        return traceId;
    }

    public void setTraceId(Long traceId) {
        this.traceId = traceId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
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

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RealVo realVo = (RealVo) o;
        return Objects.equals(traceId, realVo.traceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(traceId);
    }


    @Override
    public String toString() {
        return String.valueOf(traceId);
    }
}
