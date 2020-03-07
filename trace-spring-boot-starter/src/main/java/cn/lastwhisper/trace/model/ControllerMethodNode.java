package cn.lastwhisper.trace.model;

/**
 * controller方法
 * @author lastwhisper
 * @date 2020/2/28
 */
public class ControllerMethodNode extends MethodNode {

    /**
     * 请求的url
     */
    private String httpPath;

    /**
     * Http请求方式
     */
    private String httpMethod;

    public ControllerMethodNode() {
    }

    public ControllerMethodNode(String httpPath, String httpMethod) {
        this.httpPath = httpPath;
        this.httpMethod = httpMethod;
    }

    public String getHttpPath() {
        return httpPath;
    }

    public void setHttpPath(String httpPath) {
        this.httpPath = httpPath;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }
}
