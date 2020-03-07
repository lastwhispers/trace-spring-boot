package com.example.demo;

import cn.lastwhisper.trace.processor.annotation.EnableTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Administrator
 */
@Slf4j
@SpringBootApplication
@EnableTrace("execution(* com.example.demo..*(..))")
public class DemoApplication {

    // http://localhost:8080/user?id=123&name=zhangsan
    // http://localhost:8080/userException?id=123&name=zhangsan
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
