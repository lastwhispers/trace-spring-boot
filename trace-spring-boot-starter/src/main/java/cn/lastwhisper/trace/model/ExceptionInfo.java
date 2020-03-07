package cn.lastwhisper.trace.model;

/**
 * 封装异常简单信息
 * @author lastwhisper
 * @date 2020/3/1
 */
public class ExceptionInfo {

    /**
     * 出现异常的方法
     * 异常代码行号
     */
    private StackTraceElement stackTraceElement;

    /**
     * 异常详情
     */
    private String detailMessage;

    /**
     * 异常全限定名
     */
    private String exceptionName;

    public ExceptionInfo() {
    }

    public ExceptionInfo(StackTraceElement stackTraceElement, String detailMessage, String exceptionName) {
        this.stackTraceElement = stackTraceElement;
        this.detailMessage = detailMessage;
        this.exceptionName = exceptionName;
    }

    public StackTraceElement getStackTraceElement() {
        return stackTraceElement;
    }

    public void setStackTraceElement(StackTraceElement stackTraceElement) {
        this.stackTraceElement = stackTraceElement;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public void setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
    }

    public String getExceptionName() {
        return exceptionName;
    }

    public void setExceptionName(String exceptionName) {
        this.exceptionName = exceptionName;
    }
}

