package com.example.demo;

import cn.lastwhisper.trace.processor.annotation.EnableTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Administrator
 */
@Slf4j
@SpringBootApplication(scanBasePackages = "cn.lastwhisper.trace")
@EnableTrace("execution(* com.example.demo.core..*(..))")
public class DemoApplication {

    //普通 http://localhost:8080/user?id=123&name=zhangsan
    //异常 http://localhost:8080/userException?id=123&name=zhangsan
    //分支 http://localhost:8080/fork?id=123&name=zhangsan&num=1
    //分支 http://localhost:8080/fork?id=123&name=zhangsan&num=2
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
