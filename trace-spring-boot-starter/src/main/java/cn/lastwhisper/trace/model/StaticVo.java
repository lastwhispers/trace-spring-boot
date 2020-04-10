package cn.lastwhisper.trace.model;

/**
 * 静态调用链页面展示
 * @author lastwhisper
 * @date 2020/4/5
 */
public class StaticVo {

    /**
     * 方法签名
     */
    private Long chainId;

    /**
     * 全限定名
     */
    private String className;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 请求的URI
     */
    private String uri;

    /**
     * Http请求方式
     */
    private String httpMethod;

    public StaticVo(Long chainId, String className, String methodName, String uri, String httpMethod) {
        this.chainId = chainId;
        this.className = className;
        this.methodName = methodName;
        this.uri = uri;
        this.httpMethod = httpMethod;
    }

    public Long getChainId() {
        return chainId;
    }

    public void setChainId(Long chainId) {
        this.chainId = chainId;
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
