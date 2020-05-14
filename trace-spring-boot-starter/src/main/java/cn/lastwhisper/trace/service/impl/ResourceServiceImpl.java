package cn.lastwhisper.trace.service.impl;

import cn.lastwhisper.trace.service.ResourceService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    private final Object mutex = new Object();

    // 弱引用，每次 GC 都会被回收
    private Map<String, byte[]> cacheByteMap = new WeakHashMap<>();

    @Override
    public byte[] read(String classPath) {
        // 从缓存拿
        byte[] resources = cacheByteMap.get(classPath);
        if (resources != null) {
            return resources;
        }
        // load static resource
        ClassPathResource resource = new ClassPathResource(classPath);
        if (!resource.exists()) {
            return null;
        }
        // resource convert bytes
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
        byte[] resourceBytes = stream.toByteArray();

        // DCL 写入缓存，重复写入?
        if (!cacheByteMap.containsKey(classPath)) {
            synchronized (mutex) {
                if (!cacheByteMap.containsKey(classPath)) {
                    cacheByteMap.put(classPath, resourceBytes);
                }
            }
        }
        return resourceBytes;
    }

}
