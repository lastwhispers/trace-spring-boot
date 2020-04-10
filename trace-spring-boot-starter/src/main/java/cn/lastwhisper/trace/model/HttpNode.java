package cn.lastwhisper.trace.model;

/**
 * controller方法
 * @author lastwhisper
 * @date 2020/2/28
 */
public class HttpNode extends Node {

    /**
     * 请求的url
     */
    private String uri;

    /**
     * Http请求方式
     */
    private String httpMethod;

    public HttpNode() {
    }


    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }
}
