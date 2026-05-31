package com.example.demo.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 应用启动后定时随机调用测试接口，持续生成监控数据
 */
@Slf4j
@Component
@Order(1)
public class EndpointAutoTester implements ApplicationRunner {

    private final RestTemplate restTemplate = new RestTemplate();
    private final Random random = new Random();
    private final AtomicInteger callCount = new AtomicInteger(0);

    @Value("${server.port:8081}")
    private int port;

    private String baseUrl;
    private List<Endpoint> endpoints = new ArrayList<>();

    @Override
    public void run(ApplicationArguments args) {
        // 初始化接口列表
        endpoints.add(new Endpoint("普通", "/user", "id=123&name=zhangsan", HttpMethod.GET));
        endpoints.add(new Endpoint("异常", "/userException", "id=123&name=zhangsan", HttpMethod.GET));
        endpoints.add(new Endpoint("分支(偶数)", "/fork", "id=123&name=zhangsan&num=2", HttpMethod.GET));
        endpoints.add(new Endpoint("分支(奇数)", "/fork", "id=123&name=zhangsan&num=1", HttpMethod.GET));
        endpoints.add(new Endpoint("[基础追踪]", "/test/hello", null, HttpMethod.GET));
        endpoints.add(new Endpoint("[循环调用]", "/test/user/list", null, HttpMethod.GET));
        endpoints.add(new Endpoint("[PathVariable]", "/test/user/detail/" + (random.nextInt(100) + 1), null, HttpMethod.GET));
        endpoints.add(new Endpoint("[POST]", "/test/user", "id=" + random.nextInt(100) + "&name=auto", HttpMethod.POST));
        endpoints.add(new Endpoint("[PUT]", "/test/user/" + (random.nextInt(100) + 1), "name=updated", HttpMethod.PUT));
        endpoints.add(new Endpoint("[DELETE]", "/test/user/" + (random.nextInt(100) + 1), null, HttpMethod.DELETE));
        endpoints.add(new Endpoint("[深度调用链]", "/test/teacher/nested", "id=" + random.nextInt(100) + "&name=deep", HttpMethod.GET));
        endpoints.add(new Endpoint("[@Include/@Exclude]", "/test/convert/data", "id=" + random.nextInt(100), HttpMethod.GET));
        endpoints.add(new Endpoint("[多类型异常]", "/test/exception/multi", null, HttpMethod.GET));
        endpoints.add(new Endpoint("[慢请求]", "/test/slow-query", null, HttpMethod.GET));
        endpoints.add(new Endpoint("[批量混合]", "/test/batch/mixed", null, HttpMethod.GET));
        endpoints.add(new Endpoint("[多PathVariable]", "/test/param/" + random.nextInt(100) + "/user/auto", null, HttpMethod.GET));
    }

    /**
     * 每3秒随机调用一个接口
     */
    @Scheduled(fixedRate = 3000)
    public void randomCall() {
        if (baseUrl == null) {
            baseUrl = "http://localhost:" + port;
            log.info("\n========== 定时随机调用已启动，每3秒执行一次 ==========");
        }

        Endpoint ep = endpoints.get(random.nextInt(endpoints.size()));
        int count = callCount.incrementAndGet();
        callEndpoint(count, ep);
    }

    /**
     * 每30秒执行一次批量调用（连续5个请求），模拟突发流量
     */
    @Scheduled(fixedRate = 3000, initialDelay = 1000)
    public void batchCall() {
        log.info("\n--- 批量调用: 连续5个请求 ---");
        for (int i = 0; i < 5; i++) {
            Endpoint ep = endpoints.get(random.nextInt(endpoints.size()));
            int count = callCount.incrementAndGet();
            callEndpoint(count, ep);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private void callEndpoint(int count, Endpoint ep) {
        String path = ep.path;
        // 动态生成参数
        String query = ep.query != null ? ep.query
                .replaceAll("randomId", String.valueOf(random.nextInt(1000)))
                : null;
        String url = baseUrl + path + (query != null ? "?" + query : "");
        String displayPath = path + (query != null ? "?" + query : "");

        try {
            ResponseEntity<String> response;
            switch (ep.method) {
                case POST:
                    response = restTemplate.postForEntity(url, null, String.class);
                    break;
                case PUT:
                    restTemplate.put(url, null);
                    response = ResponseEntity.ok().body("");
                    break;
                case DELETE:
                    response = restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);
                    break;
                default:
                    response = restTemplate.getForEntity(url, String.class);
            }
            int status = response.getStatusCodeValue();
            String statusStr = status >= 200 && status < 300 ? "OK" : "WARN";
            log.info("[#{}] {} {} -> {}", count, ep.label, displayPath, statusStr);
        } catch (Exception e) {
            // 异常接口预期会抛出，记录为正常
            if (ep.label.contains("异常")) {
                log.info("[#{}] {} {} -> Exception(预期)", count, ep.label, displayPath);
            } else {
                log.warn("[#{}] {} {} -> FAIL: {}", count, ep.label, displayPath, e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            }
        }
    }

    private static class Endpoint {
        final String label;
        final String path;
        final String query;
        final HttpMethod method;

        Endpoint(String label, String path, String query, HttpMethod method) {
            this.label = label;
            this.path = path;
            this.query = query;
            this.method = method;
        }
    }
}
