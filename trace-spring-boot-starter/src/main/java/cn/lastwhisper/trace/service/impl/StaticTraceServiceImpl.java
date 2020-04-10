package cn.lastwhisper.trace.service.impl;

import cn.lastwhisper.trace.model.HttpMethod;
import cn.lastwhisper.trace.model.Method;
import cn.lastwhisper.trace.model.StaticVo;
import cn.lastwhisper.trace.repository.StaticTraceRepository;
import cn.lastwhisper.trace.service.StaticTraceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 静态调用链
 * @author lastwhisper
 * @date 2020/4/3
 */
@Service
public class StaticTraceServiceImpl implements StaticTraceService {

    @Autowired
    private StaticTraceRepository staticTraceRepository;

    @Override
    public int methodTotal() {
        return staticTraceRepository.methodTotal();
    }

    @Override
    public List<StaticVo> listByPage(int curr, int size) {
        int count = staticTraceRepository.methodTotal() - 1;

        int start, end;
        if (curr == 1) {
            start = 0;
            end = size;
        } else {
            start = (curr - 1) * size;
            end = (curr) * size;
        }

        if (start > count) {
            return null;
        }
        if (end > count) {
            end = count;
        }

        return staticTraceRepository.listByLimit(start, end);
    }

    @Override
    public Method getChainByChainId(Long chainId) {
        return staticTraceRepository.getChainByChainId(chainId);
    }

    @Override
    public Method getMethodBySignatureId(Long signatureId) {
        return staticTraceRepository.getMethodBySignatureId(signatureId);
    }

    @Override
    public void saveMethod(Method method) {
        staticTraceRepository.saveMethod(method);
    }

    @Override
    public void saveHttpMethod(Method method) {
        staticTraceRepository.saveHttpMethod(method);
        savePage(method);
    }

    @Override
    public void savePage(Method method) {
        HttpMethod httpMethod = (HttpMethod) method;
        staticTraceRepository.savePage(new StaticVo(httpMethod.getChainId(), httpMethod.getClassName(),
                httpMethod.getName(),httpMethod.getUri(), httpMethod.getHttpMethod()));
    }

    @Override
    public void clear() {
        staticTraceRepository.clear();
    }
}
