# Trace Spring Boot - 项目 Wiki

## 项目概述

**Trace** 是一个基于 Spring Boot 的单体架构异步非阻塞监控追踪系统（APM 工具），用于收集应用运行时的调用链路、性能耗时、异常信息等数据，并提供可视化的监控面板。

| 属性 | 值 |
|------|-----|
| 项目地址 | https://github.com/lastwhispers/trace-spring-boot |
| 在线演示 | http://39.105.49.50:8081/monitor/index.html |
| 当前版本 | 1.0.3.RELEASE（starter），1.0.2.RELEASE（samples） |
| Java 版本 | JDK 1.8 |
| 框架依赖 | Spring Boot 2.2.2.RELEASE（AOP + Web） |
| 许可证 | Apache 2.0 |

---

## 核心功能

### 1. 实时调用链（Real-Time Trace）
- 实时收集接口请求的调用链路
- 记录每个方法节点的耗时
- 捕获并展示异常堆栈信息
- 支持级联事件关联

### 2. 静态调用链（Static Trace）
- 对调用链路去重，保存静态调用关系
- 可用于学习 Web 项目代码结构
- 可用于生成类图等文档

### 3. 统计信息（Statistics）
- 提供接口请求的统计分析

---

## 项目结构

```
trace-spring-boot/
├── trace-spring-boot-starter/          # 核心库（可发布到 Maven 中央仓库）
│   └── src/main/java/cn/lastwhisper/trace/
│       ├── ProjectAutoConfiguration.java       # Spring Boot 自动配置入口
│       ├── aspect/                             # AOP 切面
│       │   ├── CollectorAspect.java            # 收集器切面（基类）
│       │   └── annotation/
│       │       ├── Exclude.java                # 排除方法/类不被织入
│       │       └── Include.java                # 包含方法/类织入
│       ├── collector/                          # 数据采集器
│       │   ├── Collector.java                  # 采集器接口
│       │   ├── CollectorHandler.java           # 采集器处理器接口
│       │   ├── impl/AsyncSerialCollector.java  # 异步串行采集器
│       │   └── impl/SerialCollectorHandler.java # 串行采集处理器
│       ├── collector/                          # 数据收集
│       ├── common/                             # 公共工具
│       │   ├── RequestHelper.java              # HTTP 请求辅助
│       │   ├── collection/
│       │   │   ├── ConcurrentEventLruCacheMap.java  # 支持级联事件的 LRU 缓存
│       │   │   └── ConcurrentLruCacheMap.java      # 并发 LRU 缓存
│       │   └── util/
│       │       ├── CollectionUtil.java         # 集合工具
│       │       └── CollectorUtil.java          # 采集器工具
│       ├── config/
│       │   └── DefaultConfig.java              # 默认配置
│       ├── controller/                         # 监控 API 控制器
│       │   ├── RealTraceController.java        # 实时调用链 API
│       │   ├── StaticTraceController.java      # 静态调用链 API
│       │   ├── StatisticsController.java       # 统计信息 API
│       │   └── ResourceController.java         # 静态资源控制器
│       ├── event/                              # 级联事件
│       │   ├── CascadeEvent.java               # 级联事件
│       │   ├── CascadeEventPublisher.java      # 事件发布器
│       │   └── CascadeIdNotifier.java          # 级联 ID 通知器
│       ├── factory/                            # 节点工厂
│       │   ├── TraceNodeFactory.java           # 节点工厂接口
│       │   ├── DefaultTraceNodeFactory.java    # 默认实现
│       │   └── HttpTraceNodeFactory.java       # HTTP 节点工厂
│       ├── model/                              # 数据模型
│       │   ├── Node.java                       # 调用节点基类
│       │   ├── Method.java                     # 方法节点
│       │   ├── HttpNode.java                   # HTTP 节点
│       │   ├── HttpMethod.java                 # HTTP 方法枚举
│       │   ├── ExceptionInfo.java              # 异常信息
│       │   ├── RealVo.java                     # 实时调用链 VO
│       │   └── StaticVo.java                   # 静态调用链 VO
│       ├── processor/                          # 编译期注解处理器
│       │   ├── GenerateAspectProcessor.java    # 生成 CollectorAspect 的 APT 处理器
│       │   └── annotation/
│       │       └── EnableTrace.java            # 启用 Trace 的注解
│       ├── repository/                         # 数据仓储接口
│       │   ├── RealTraceRepository.java        # 实时调用链仓储接口
│       │   ├── StaticTraceRepository.java      # 静态调用链仓储接口
│       │   └── impl/                           # 内存实现（默认）
│       │       ├── RealTraceInMemoryRepository.java
│       │       └── StaticTraceInMemoryRepository.java
│       └── service/                            # 业务服务层
│           ├── IdService.java                  # ID 生成服务接口
│           ├── RealTraceService.java           # 实时调用链服务接口
│           ├── StaticTraceService.java         # 静态调用链服务接口
│           ├── StatisticsService.java          # 统计服务接口
│           ├── ResourceService.java            # 资源服务接口
│           └── impl/                           # 服务实现
│               ├── IdIncrServiceImpl.java      # 自增 ID 实现
│               ├── RealTraceServiceImpl.java
│               ├── StaticTraceServiceImpl.java
│               └── ResourceServiceImpl.java
│
├── trace-spring-boot-samples/            # 使用示例项目
│   └── src/main/java/com/example/demo/
│       ├── DemoApplication.java          # 启动类（带 @EnableTrace）
│       ├── core/
│       │   ├── controller/
│       │   │   └── UserController.java   # 演示控制器
│       │   ├── service/
│       │   │   ├── UserService.java      # 用户服务
│       │   │   └── TeacherService.java   # 教师服务
│       │   ├── dao/
│       │   │   ├── UserDao.java          # 用户 DAO
│       │   │   └── TeacherDao.java       # 教师 DAO
│       │   └── model/
│       │       ├── User.java             # 用户实体
│       │       └── Teacher.java          # 教师实体
│       └── common/
│           └── Convert.java              # 转换工具类
```

---

## 架构原理

### 整体流程

```
HTTP 请求 → AOP 织入方法 → Collector 采集节点信息 → 存储到 Repository → 前端监控面板展示
```

### 编译期 APT 机制

1. 用户在启动类添加 `@EnableTrace("execution(* com.example..*(..))")` 注解
2. 编译时，`GenerateAspectProcessor`（JSR 269 注解处理器）扫描 `@EnableTrace`
3. 根据切点表达式生成 `CollectorAspect.java` 源码到 `generated-sources/annotations/`
4. 生成的 `CollectorAspect` 继承自 `CollectorAspect` 基类，织入指定切点

### 数据模型

- **Node**: 调用节点抽象，包含方法名、耗时、子节点等
- **Method**: 普通方法调用节点
- **HttpNode**: HTTP 请求节点，包含 URL、Method、参数等
- **ExceptionInfo**: 异常信息，包含异常类型、消息、堆栈

### 存储架构

默认使用内存存储，通过数据结构实现：
- **ArrayList**: 分页查询
- **LinkedList**: LRU 淘汰
- **HashMap**: 快速查找

需要持久化时，只需重写 `*Repository` 接口实现即可。

---

## 快速接入

### 1. 引入依赖

```xml
<!-- Trace 核心 -->
<dependency>
    <groupId>io.github.lastwhispers</groupId>
    <artifactId>trace-spring-boot-starter</artifactId>
    <version>1.0.2.RELEASE</version>
</dependency>

<!-- Spring Boot AOP + Web（防止依赖传递被排除，需自行引入） -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

### 2. 添加 @EnableTrace 注解

```java
@SpringBootApplication
@EnableTrace("execution(* com.example.demo..*(..))")
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

### 3. 编译项目

> **重要**: 必须先编译项目，让 APT 生成 `CollectorAspect.java`。

### 4. 访问监控面板

启动项目后访问 `127.0.0.1:8080/monitor/index.html`

### 注解说明

| 注解 | 位置 | 作用 |
|------|------|------|
| `@EnableTrace` | 启动类 | 启用 Trace，value 为切点表达式 |
| `@Exclude` | 方法/类 | 排除切点内的特定方法或类 |
| `@Include` | 方法/类 | 对排除后的方法或类重新包含 |

---

## API 接口

| 路径 | 功能 |
|------|------|
| `/monitor/index.html` | 监控面板首页 |
| `/monitor/real/*` | 实时调用链查询 |
| `/monitor/static/*` | 静态调用链查询 |
| `/monitor/statistics/*` | 统计信息查询 |
| `/monitor/resource/*` | 静态资源 |

---

## 注意事项

1. **不要注入 final 修饰的类**: 无法运行时代理 final 类，会导致启动失败。如必须注入，用 `@Exclude` 排除。
2. **编译是必须的**: 编译后 `generated-sources` 目录下才会生成 `CollectorAspect.java`。
3. **权限配置**: 如果项目有 Shiro/Spring Security，需将 `/monitor/**` 路径纳入权限控制。

---

## 扩展开发

### 自定义持久化

实现以下接口并替换默认实现：

- `RealTraceRepository`: 实时调用链持久化
- `StaticTraceRepository`: 静态调用链持久化

### 自定义节点工厂

实现 `TraceNodeFactory` 接口可定制节点创建逻辑。

### 完全移除 Trace

1. 删除 `@EnableTrace`、`@Exclude`、`@Include` 注解及导入
2. 删除 Maven 依赖
3. 执行 `mvn clean` 或手动删除 `target/generated-sources/`

---

## 版本历史

| 版本 | 说明 |
|------|------|
| 1.0.2.RELEASE | 当前 Samples 使用版本 |
| 1.0.3.RELEASE | Starter 当前版本 |
