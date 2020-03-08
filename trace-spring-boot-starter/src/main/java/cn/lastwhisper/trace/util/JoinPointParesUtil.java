package cn.lastwhisper.trace.util;

import cn.lastwhisper.trace.factory.ControllerMethodNodeFactory;
import cn.lastwhisper.trace.factory.MethodNodeFactory;
import cn.lastwhisper.trace.factory.NodeFactory;
import cn.lastwhisper.trace.model.ControllerMethodNode;
import cn.lastwhisper.trace.model.MethodNode;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 根据JoinPoint封装需要的数据
 * @author lastwhisper
 */
public class JoinPointParesUtil {

    private static Map<String, Long> methodIdMap = new HashMap<>();

    private static Logger logger = LoggerFactory.getLogger(JoinPointParesUtil.class);

    /**
     * 获取当前正在调用service、dao或者其他在ioc容器对象的方法
     */
    public static MethodNode getMethodNode(JoinPoint jp) {
        return buildMethodNode(new MethodNodeFactory(), jp);
    }

    /**
     * 获取当前正在调用controller的方法
     */
    public static MethodNode getControllerMethodNode(JoinPoint jp) {
        ControllerMethodNode methodNode = (ControllerMethodNode) buildMethodNode(new ControllerMethodNodeFactory(), jp);

        HttpServletRequest request = RequestContextUtil.getHttpServletRequest();
        if (request != null) {
            methodNode.setHttpPath(request.getRequestURI());
            methodNode.setHttpMethod(request.getMethod());
        } else {
            logger.warn("无法获取原生javax.servlet.http.HttpServletRequest");
        }

        return methodNode;
    }


    private static MethodNode buildMethodNode(NodeFactory nodeFactory, JoinPoint jp) {
        MethodNode methodNode = nodeFactory.getMethodNode();
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

        Map<String, String> parameters = new HashMap<>((int) (parameterNames.length / 0.75 + 1));
        for (int i = 0; i < parameterNames.length; i++) {
            parameters.put(parameterNames[i], parameterTypes[i].getName());
        }
        // 异常列表
        Class<?>[] exceptionTypes = methodSignature.getExceptionTypes();

        StringBuilder methodIdKeySb = new StringBuilder();
        methodIdKeySb.append(className);
        methodIdKeySb.append("#");
        methodIdKeySb.append(methodName);

        for (Class<?> exceptionType : exceptionTypes) {
            methodIdKeySb.append("#");
            methodIdKeySb.append(exceptionType.getName());
        }
        String methodIdKey = methodIdKeySb.toString();

        Long methodId = methodIdMap.get(methodIdKey);
        //String methodId = methodIdMap.get(methodIdKey);
        if (methodId == null) {
            methodId = IdUtil.getSnowflakeId();
            methodIdMap.put(methodIdKey, methodId);
        }
        methodNode.setMethodId(methodId);

        methodNode.setClassName(className);
        methodNode.setModifier(modifier);
        methodNode.setReturnType(returnType);
        methodNode.setMethodName(methodName);
        methodNode.setParameters(parameters);
        methodNode.setExceptionTypes(exceptionTypes);

        methodNode.setRandomId(IdUtil.getSnowflakeId());
        methodNode.setThreadId(Thread.currentThread().getId());
        methodNode.setTraceId(IdUtil.getTraceId());
        methodNode.setStartTime(new Date());

        return methodNode;
    }

}
