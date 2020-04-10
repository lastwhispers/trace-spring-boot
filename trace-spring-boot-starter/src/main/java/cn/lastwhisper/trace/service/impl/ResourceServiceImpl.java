package cn.lastwhisper.trace.service.impl;

import cn.lastwhisper.trace.service.ResourceService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 *
 * @author lastwhisper
 * @date 2020/4/2
 */
@Service
public class ResourceServiceImpl implements ResourceService {

    // 对象锁
    private final Object lock = new Object();
    // 缓存静态资源
    private WeakHashMap<String, byte[]> cacheByteMap = new WeakHashMap<>();
    // 缓存大小
    private Map<String, Long> cacheSizeMap = new HashMap<>();

    @Override
    public byte[] read(String classPath) {
        //从缓存拿
        byte[] resources = cacheByteMap.get(classPath);
        if (resources != null) {
            return resources;
        }
        //判空
        ClassPathResource resource = new ClassPathResource(classPath);
        if (!resource.exists()) {
            return null;
        }
        //读取
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(resource.getInputStream());
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(stream)) {
            byte[] bytes = new byte[1024];
            int n;
            while ((n = bufferedInputStream.read(bytes)) != -1) {
                bufferedOutputStream.write(bytes, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //DCL双检查锁
        if (!cacheByteMap.containsKey(classPath)) {
            synchronized (lock) {
                if (!cacheByteMap.containsKey(classPath)) {
                    cacheByteMap.put(classPath, stream.toByteArray());
                    cacheSizeMap.put(classPath, (long) stream.size());
                }
            }
        }
        return stream.toByteArray();
    }

    @Override
    public Map<String, Long> cacheSizeMap() {
        return cacheSizeMap;
    }

}
