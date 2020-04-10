# 介绍

Trace是一个单体架构的监控追踪系统。用于收集单体架构系统中的信息数据，例如在一次请求中花费的时间、请求执行的方法链、以及操作是否出现异常和异常的详细信息等。

## 实时调用链

实时收集接口请求调用链的耗时、异常相关信息
![实时调用链](https://img-blog.csdnimg.cn/20200410172312739.gif)

## 静态调用链

该功能将请求去重，保存静态调用链路，可以用来学习一个Web项目或者别的用途，比如[生成毕设中的类图]()。

![静态调用链路](https://img-blog.csdnimg.cn/20200410172337626.gif)
# 快速使用

目前只支持SpringBoot项目，trace-spring-boot-samples中有使用的例子

## 引入依赖

Trace的Maven依赖

```xml
<dependency>
    <groupId>io.github.lastwhispers</groupId>
    <artifactId>trace-spring-boot-starter</artifactId>
    <version>1.0.0.RELEASE</version>
</dependency>
```

Trace项目本身依赖了springboot的aop和web模块，为了防止依赖传递，项目本身不提供这两个模块的依赖，请自行引入。

```xml
<dependency>
 	<groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

## 配置注解

在SpringBoot的启动类上添加注解`@EnableTrace(value = "")`，该注解的value是一个[切点表达式](https://docs.spring.io/spring/docs/5.1.8.RELEASE/spring-framework-reference/core.html#aop-pointcuts)。

Trace会织入切点表达式包含的所有方法，进行信息收集。

```java
@SpringBootApplication
@EnableTrace("execution(* com.example.demo..*(..))")
public class SpingBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpingBootApplication.class, args);
    }

}
```

`@EnableTrace`配置完毕后，请先编译项目，在编译期注解处理器（Annotation Processor Tool）会生成适配于你项目的`CollectorAspect.java`源文件，这是Trace的起点。

![apt生成源码点](https://img-blog.csdnimg.cn/20200410172414260.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3Bkc3UxNjE1MzAyNDc=,size_16,color_FFFFFF,t_70)

对于未在切点表达式中的方法可以通过`@Exclude`注解进行包含，对应的可以使用`@Include`对方法或类进行排除。

## 启动项目

启动你的个人项目，访问[127.0.0.1:8080/monitor/index.html](127.0.0.1:8080/monitor/index.html)即可进入监控页面。如果有shiro、Spring Security权限控制，请赋予监控地址权限。

# 存储组件

Trace默认使用cn.lastwhisper.trace.repository.impl包下的`*InMemoryRepository`，通过几个基础的数据结构ArrayList、LinkedList、HashMap完成分页、查询、LRU。

如果你需要持久化数据，只需要重写cn.lastwhisper.trace.repository包下的`*Repository`接口，实现对应规范的方法即可。

# 原理架构

![原理架构](https://img-blog.csdnimg.cn/20200410172435250.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3Bkc3UxNjE1MzAyNDc=,size_16,color_FFFFFF,t_70)