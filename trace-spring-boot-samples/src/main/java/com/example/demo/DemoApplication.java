package com.example.demo;

import cn.lastwhisper.trace.processor.annotation.EnableTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Administrator
 */
@Slf4j
@SpringBootApplication
@EnableScheduling
@EnableTrace("execution(* com.example.demo.core..*(..))")
public class DemoApplication {

    // 原有接口
    // http://localhost:8081/user?id=123&name=zhangsan
    // http://localhost:8081/userException?id=123&name=zhangsan
    // http://localhost:8081/fork?id=123&name=zhangsan&num=1
    // http://localhost:8081/fork?id=123&name=zhangsan&num=2
    // 测试接口 /test/*
    // http://localhost:8081/test/hello
    // http://localhost:8081/test/user/list
    // http://localhost:8081/test/user/detail/1
    // http://localhost:8081/test/user (POST) id=1&name=test
    // http://localhost:8081/test/user/1 (PUT) ?name=updated
    // http://localhost:8081/test/user/1 (DELETE)
    // http://localhost:8081/test/teacher/nested?id=1
    // http://localhost:8081/test/convert/data?id=1
    // http://localhost:8081/test/exception/multi
    // http://localhost:8081/test/slow-query
    // http://localhost:8081/test/batch/mixed
    // http://localhost:8081/test/param/1/user/test
    // 监控 http://localhost:8081/monitor/index.html
    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(DemoApplication.class, args);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        //String path = env.getProperty("server.servlet.context-path");
        log.info("\n----------------------------------------------------------\n\t" +
                "Application trace-spring-boot-samples is running! Access URLs:\n\t" +
                "Local: \t\thttp://localhost:" + port + "/\n\t" +
                "普通: \thttp://" + ip + ":" + port + "/user?id=123&name=zhangsan\n\t" +
                "异常: \thttp://" + ip + ":" + port + "/userException?id=123&name=zhangsan\n\t" +
                "分支1: \thttp://" + ip + ":" + port + "/fork?id=123&name=zhangsan&num=1\n\t" +
                "分支2: \thttp://" + ip + ":" + port + "/fork?id=123&name=zhangsan&num=2\n\t" +
                "[基础追踪] \thttp://" + ip + ":" + port + "/test/hello\n\t" +
                "[循环调用] \thttp://" + ip + ":" + port + "/test/user/list\n\t" +
                "[PathVariable] \thttp://" + ip + ":" + port + "/test/user/detail/1\n\t" +
                "[POST/PUT/DELETE] \thttp://" + ip + ":" + port + "/test/user (POST/PUT/DELETE)\n\t" +
                "[深度调用链] \thttp://" + ip + ":" + port + "/test/teacher/nested?id=1\n\t" +
                "[@Include/@Exclude] \thttp://" + ip + ":" + port + "/test/convert/data?id=1\n\t" +
                "[多类型异常] \thttp://" + ip + ":" + port + "/test/exception/multi\n\t" +
                "[慢请求] \thttp://" + ip + ":" + port + "/test/slow-query\n\t" +
                "[批量混合] \thttp://" + ip + ":" + port + "/test/batch/mixed\n\t" +
                "[多PathVariable] \thttp://" + ip + ":" + port + "/test/param/1/user/test\n\t" +
                "监控: \thttp://" + ip + ":" + port + "/monitor/index.html\n\t" +
                "----------------------------------------------------------");

    }

}
