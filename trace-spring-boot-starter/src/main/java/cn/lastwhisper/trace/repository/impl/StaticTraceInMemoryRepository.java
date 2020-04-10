package cn.lastwhisper.trace.repository.impl;

import cn.lastwhisper.trace.model.Method;
import cn.lastwhisper.trace.model.StaticVo;
import cn.lastwhisper.trace.repository.StaticTraceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 内存读写实现
 * @author lastwhisper
 * @date 2020/4/3
 */
@Repository
public class StaticTraceInMemoryRepository implements StaticTraceRepository {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private List<StaticVo> pages = new CopyOnWriteArrayList<>();
    // chainId,MethodInfo(完整静态调用链)
    private Map<Long, Method> chainIdMap = new ConcurrentHashMap<>();
    // signatureId,MethodInfo(某个method静态信息)
    private Map<Long, Method> signatureIdMap = new ConcurrentHashMap<>();

    @Override
    public int methodTotal() {
        return pages.size();
    }

    @Override
    public List<StaticVo> listByLimit(int start, int end) {
        List<StaticVo> result = new ArrayList<>(end - start);
        logger.debug("listByLimit start:{}\tend:{}", start, end);
        for (int i = start; i <= end; i++) {
            result.add(pages.get(i));
        }
        return result;
    }

    @Override
    public Method getChainByChainId(Long chainId) {
        return chainIdMap.get(chainId);
    }

    @Override
    public Method getMethodBySignatureId(Long signatureId) {
        return signatureIdMap.get(signatureId);
    }

    @Override
    public void saveMethod(Method method) {
        signatureIdMap.put(method.getSignatureId(), method);
    }

    @Override
    public void saveHttpMethod(Method method) {
        chainIdMap.put(method.getChainId(), method);
    }

    @Override
    public void savePage(StaticVo staticVo) {
        pages.add(staticVo);
    }

    @Override
    public void clear() {
        signatureIdMap.clear();
        chainIdMap.clear();
    }

}
