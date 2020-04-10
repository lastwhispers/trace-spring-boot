package cn.lastwhisper.trace.model;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 方法静态信息
 * @author lastwhisper
 * @date 2020/3/26
 */
public class Method {


    /**
     * 同method全局唯一
     */
    //@JsonSerialize(using = ToStringSerializer.class)
    private Long signatureId;

    /**
     * 同调用链相同
     */
    private Long chainId;

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
    private String name;

    /**
     * 参数列表(区分重载的方法)
     */
    private LinkedHashMap<String, String> parameters;

    /**
     * 异常列表
     */
    private Class<?>[] exceptions;

    /**
     * 类全限定名
     */
    private String className;

    /**
     * 父接口列表
     */
    private Class<?>[] interfaces;

    /**
     * 父类
     */
    private Class<?> superclass;

    /**
     * 调用的方法
     */
    private List<Method> children;

    public Method() {
    }

    public Long getSignatureId() {
        return signatureId;
    }

    public void setSignatureId(Long signatureId) {
        this.signatureId = signatureId;
    }

    public Long getChainId() {
        return chainId;
    }

    public void setChainId(Long chainId) {
        this.chainId = chainId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedHashMap<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(LinkedHashMap<String, String> parameters) {
        this.parameters = parameters;
    }

    public Class<?>[] getExceptions() {
        return exceptions;
    }

    public void setExceptions(Class<?>[] exceptions) {
        this.exceptions = exceptions;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<Method> getChildren() {
        return children;
    }

    public void setChildren(List<Method> children) {
        this.children = children;
    }

    public Class<?>[] getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(Class<?>[] interfaces) {
        this.interfaces = interfaces;
    }

    public Class<?> getSuperclass() {
        return superclass;
    }

    public void setSuperclass(Class<?> superclass) {
        this.superclass = superclass;
    }
}
