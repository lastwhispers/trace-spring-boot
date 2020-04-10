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
    private StackTraceElement ste;

    /**
     * 异常详情
     */
    private String msg;

    /**
     * 异常全限定名
     */
    private String name;

    public ExceptionInfo() {
    }

    public ExceptionInfo(StackTraceElement ste, String msg, String name) {
        this.ste = ste;
        this.msg = msg;
        this.name = name;
    }

    public StackTraceElement getSte() {
        return ste;
    }

    public void setSte(StackTraceElement ste) {
        this.ste = ste;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

