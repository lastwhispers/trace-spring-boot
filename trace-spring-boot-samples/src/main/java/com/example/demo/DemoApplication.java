package com.example.demo;

import cn.lastwhisper.trace.processor.annotation.EnableTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Administrator
 */
@Slf4j
@SpringBootApplication
@EnableTrace("execution(* com.example.demo.core..*(..))")
public class DemoApplication {

    //普通 http://localhost:8081/user?id=123&name=zhangsan
    //异常 http://localhost:8081/userException?id=123&name=zhangsan
    //分支1 http://localhost:8081/fork?id=123&name=zhangsan&num=1
    //分支2 http://localhost:8081/fork?id=123&name=zhangsan&num=2
    //监控 http://localhost:8081/monitor/index.html
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
                "监控: \thttp://" + ip + ":" + port + "/monitor/index.html\n\t" +
                "----------------------------------------------------------");

    }

}
