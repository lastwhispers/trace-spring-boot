package cn.lastwhisper.trace.common.util;

import cn.lastwhisper.trace.factory.DefaultTraceNodeFactory;
import cn.lastwhisper.trace.factory.HttpTraceNodeFactory;
import cn.lastwhisper.trace.factory.TraceNodeFactory;
import cn.lastwhisper.trace.model.HttpMethod;
import cn.lastwhisper.trace.model.HttpNode;
import cn.lastwhisper.trace.model.Method;
import cn.lastwhisper.trace.model.Node;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 根据JoinPoint封装需要的数据
 * @author lastwhisper
 */
public class CollectorUtil {

    /**
     * 获取当前正在调用service、dao或者其他在ioc容器对象的方法
     */
    public static Node getDefaultNode(JoinPoint jp) {
        return getNode(new DefaultTraceNodeFactory(), jp);
    }

    /**
     * 获取当前正在调用controller的方法
     */
    public static Node getHttpNode(HttpServletRequest request, JoinPoint jp) {
        HttpNode httpNode = (HttpNode) getNode(new HttpTraceNodeFactory(), jp);
        httpNode.setUri(request.getRequestURI());
        httpNode.setHttpMethod(request.getMethod());

        HttpMethod httpMethod = (HttpMethod) httpNode.getMethod();
        httpMethod.setHttpMethod(request.getMethod());
        httpMethod.setUri(request.getRequestURI());
        return httpNode;
    }


    private static Node getNode(TraceNodeFactory traceNodeFactory, JoinPoint jp) {
        Node node = traceNodeFactory.getNode();
        node.setStart(new Date());
        node.setMethod(getMethodInfo(traceNodeFactory, jp));
        return node;
    }


    /**
     *  获取静态Method
     * @param traceNodeFactory trace结点创建工厂
     * @param jp JoinPoint
     */
    public static Method getMethodInfo(TraceNodeFactory traceNodeFactory, JoinPoint jp) {
        Method method = traceNodeFactory.getMethod();
        // 获取方法签名
        MethodSignature methodSignature = (MethodSignature) jp.getSignature();
        // 全限定名
        String className = methodSignature.getDeclaringType().getName();
        // 权限修饰符
        String modifier = Modifier.toString(methodSignature.getModifiers());
        // 返回值类型
        Class<?> returnType = methodSignature.getReturnType();
        // 方法名
        String methodName = methodSignature.getName();
        // 参数类型
        Class<?>[] parameterTypes = methodSignature.getParameterTypes();
        // 参数名称
        String[] parameterNames = methodSignature.getParameterNames();
        // 参数列表
        LinkedHashMap<String, String> parameters = new LinkedHashMap<>((int) (parameterNames.length / 0.75 + 1));
        for (int i = 0; i < parameterNames.length; i++) {
            parameters.put(parameterNames[i], parameterTypes[i].getName());
        }
        // 异常列表
        Class<?>[] exceptionTypes = methodSignature.getExceptionTypes();
        // 获取接口
        Class<?>[] interfaces = methodSignature.getDeclaringType().getInterfaces();
        // 获取父类
        Class<?> superclass = methodSignature.getDeclaringType().getSuperclass();

        method.setModifier(modifier);
        method.setReturnType(returnType);
        method.setName(methodName);
        method.setParameters(parameters);
        method.setExceptions(exceptionTypes);
        method.setClassName(className);
        method.setInterfaces(interfaces);
        method.setSuperclass(superclass);
        //return new Method(modifier, returnType, methodName, parameters, exceptionTypes, fullClassName);
        return method;
    }

    /**
     *  获取方法链签名<s>并加入字符串常量池</s>
     */
    public static String getSignature(Method method) {
        StringBuilder signature = getSignatureBuilder(method);
        // jdk7：toString()在堆中创建字符串，intern()在常量池中引用堆中字符串
        return signature.toString();
    }

    /**
     *  获取方法链签名
     */
    public static StringBuilder getSignatureBuilder(Method method) {
        if (method == null) {
            return new StringBuilder();
        }
        StringBuilder signature = new StringBuilder(buildSignature(method));
        List<Method> children = method.getChildren();
        if (children == null) {
            return signature;
        }
        for (Method child : children) {
            signature.append(getSignature(child));
        }
        return signature;
    }

    /**
     *  构建方法签名
     * @param method 方法静态信息
     */
    public static StringBuilder buildSignature(Method method) {
        String fullClassName = method.getClassName();
        String methodName = method.getName();
        Collection<String> parameterTypes = method.getParameters().values();

        StringBuilder sb = new StringBuilder();
        sb.append(fullClassName);
        sb.append("#");
        sb.append(methodName);

        for (String parameterType : parameterTypes) {
            sb.append("#");
            sb.append(parameterType);
        }
        sb.append("$");

        return sb;
    }

    /**
     * 两颗树是否相同
     * @param p MethodInfo
     * @param q MethodInfo
     * @return boolean 相同true，不同false
     */
    public static boolean isSame(Method p, Method q) {
        // p、q都不为空
        if (p == null && q == null) {
            return true;
        }
        if (p == null || q == null) {
            return false;
        }
        // p==q
        if (!p.getSignatureId().equals(q.getSignatureId())) {
            return false;
        }

        List<Method> pChild = p.getChildren();
        List<Method> qChild = q.getChildren();

        // p、q孩子都不为空
        if (CollectionUtil.isEmpty(qChild) && CollectionUtil.isEmpty(pChild)) {
            return true;
        }
        if (CollectionUtil.isEmpty(qChild) || CollectionUtil.isEmpty(pChild)) {
            return false;
        }
        // p、q孩子长度相同
        if (pChild.size() != qChild.size()) {
            return false;
        }

        for (int i = 0; i < pChild.size(); i++) {
            if (!isSame(pChild.get(i), qChild.get(i))) {
                return false;
            }
        }

        return true;
    }

}
