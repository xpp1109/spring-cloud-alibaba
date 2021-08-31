# 工程简介
* Spring Boot: 2.3.7.RELEASE
* Spring Cloud Alibaba: 2.2.2.RELEASE
* JDK: 1.8
* sentinel dashboard: 1.8.2
# Nacos服务注册于发现中心、配置中心
Spring Cloud的注册中心和配置中心是分开的，Spring Cloud Alibaba使用
Nacos提供者两种功能。
Nacos使用方法请参照[官网](https://nacos.io/)
本文使用standalone模式启动
端口使用默认8848
进入bin目录，启动指令`./startup.sh -m standalone`  
![启动截图](https://raw.githubusercontent.com/xpp1109/images/main/uPic/LicWOP.png)
![首页](https://raw.githubusercontent.com/xpp1109/images/main/uPic/aTJ9Y4.png)  
默认用户名密码都是nacos
![主页](https://raw.githubusercontent.com/xpp1109/images/main/uPic/ZGpGNL.png)

# 服务注册（以author module为例）
* 增加Maven依赖
```xml
<!--nacos服务注册与发现依赖-->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```
* Spring Boot application.yml配置文件
```yaml
spring:
  application:
    name: author
  cloud:
    nacos:
      #默认是8848端口，注册中心和配置中心的地址默认都是这个地址
      server-addr: localhost:8848
server:
  port: 8080
```
* 启动项目访问：http://localhost:8080,查看nacos服务列表界面：
![nacos服务列表查看author是否注册上](https://raw.githubusercontent.com/xpp1109/images/main/uPic/Z1esU7.png)

> 配置中心  

使用配置中心时，需要注意spring cloud中的两个上下文application和bootstrap。
此处要使用bootstrap(.properties或者.yml). 加载顺序bootstrap > application.
文件名格式如下：
`${spring.application.name}. ${file-extension:properties}`
`${spring.application.name}-${profile}. ${file-extension:properties}`
profile要通过`spring.profiles.active` 配置。
后缀通过`spring.cloud.nacos.config.file-extension=yaml`约定，默认是properties。
* maven依赖
```xml
<!--配置中心依赖-->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
    <optional>true</optional>
</dependency>
```
* bootstrap.yml配置
```yaml
spring:
  application:
    name: author
  cloud:
    nacos:
      #默认是8848端口，注册中心和配置中心的地址默认都是这个地址
      server-addr: localhost:8848
      config:
        file-extension: yaml
  profiles:
    active: dev
server:
  port: 8080
```
* 在Nacos配置author name:
![nacos中author项目配置](https://raw.githubusercontent.com/xpp1109/images/main/uPic/PLJsFz.png)
* 启动项目访问：http://localhost:8080/getAuthorName
![从配置中心获取配置结果](https://raw.githubusercontent.com/xpp1109/images/main/uPic/ZXEJX8.png)
* 增加带有profile的配置，重启项目（怎么做到不重启呢？），重新访问：http://localhost:8080/getAuthorName
![增加带有profile的配置](https://raw.githubusercontent.com/xpp1109/images/main/uPic/MZLavf.png)
![结果](https://raw.githubusercontent.com/xpp1109/images/main/uPic/dxLyxt.png)
* 自动刷新
自动刷新配置有两种方式，1：`@RefreshScope`, 2: 使用配置类
1. `@RefreshScope`
![@refreshscope配置图](https://raw.githubusercontent.com/xpp1109/images/main/uPic/q1sUZ7.png)
修改author-dev.yaml配置，然后刷新：(注意要先启动项目，再修改配置文件内容)
![修改配置刷新后请求结果](https://raw.githubusercontent.com/xpp1109/images/main/uPic/H7BoNO.png)
可见，不用重启项目，但是配置已经修改生效。
2. 使用配置类（AuthorConfiguration.java）
```java
package com.xpp.author;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "spring.author")
@Component
@Data
public class AuthorConfiguration {
    private String name;
}
```
![controller注入并获取配置](https://raw.githubusercontent.com/xpp1109/images/main/uPic/l7pDFB.png)
启动服务，访问，修改配置再访问：
修改前：
![修改前](https://raw.githubusercontent.com/xpp1109/images/main/uPic/7wGLur.png)
![修改后](https://raw.githubusercontent.com/xpp1109/images/main/uPic/FKdzQg.png)
* 自定义Namespace,groups, dataId.
```properties
spring.cloud.nacos.config.namespace=
spring.cloud.nacos.config.group=
spring.cloud.nacos.config.extension-configs=
```
此处就不一一演示了。

# Sentinel哨兵
Sentinel以“流量”为切入点，在流量控制、断路、负载保护等多个领域开展工作，保障服务可靠性。
* 场景丰富： Sentinel 支持阿里巴巴双十一的关键场景10多年，如秒杀（即控制突发流量，使其在系统容量可接受范围内），消息负载转移，不可靠下游应用的断路。

* 全面的实时监控： Sentinel 提供实时监控能力。您可以秒级精确查看服务器的监控数据，甚至可以看到少于500个节点的集群的整体运行状态。

* 广泛的开源生态系统： Sentinel 提供了开箱即用的模块，可以轻松地与其他开源框架/库集成，例如 Spring Cloud、Dubbo 和 gRPC。使用Sentinel只需要引入相关的依赖，做一些简单的配置即可。

* SPI 扩展： Sentinel 提供了简单易用且完善的 SPI 扩展接口。您可以使用 SPI 扩展快速自定义逻辑，例如，您可以定义自己的规则管理，或适应特定的数据源。

[官方地址](https://github.com/alibaba/Sentinel/)
[Sentinel 面板安装文档](https://github.com/alibaba/Sentinel/wiki/%E6%8E%A7%E5%88%B6%E5%8F%B0)

单机版启动sentinel
`java -Dserver.port=9000 -Dcsp.sentinel.dashboard.server=localhost:9000 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard.jar`
启动完成访问http://localhost:9000
![sentinel 面板登录页](https://raw.githubusercontent.com/xpp1109/images/main/uPic/4L6TRW.png)
输入用户名sentinel，密码sentinel:
![sentinel面板主页](https://raw.githubusercontent.com/xpp1109/images/main/uPic/aqPhAi.png)

## spring cloud alibaba sentinel
* Maven增加sentinel依赖
```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
</dependency>
```
* 使用@SentinelResource注解
注解含义以及参数请查看https://github.com/alibaba/Sentinel/wiki/%E6%B3%A8%E8%A7%A3%E6%94%AF%E6%8C%81
![使用@SentinelResource注解](https://raw.githubusercontent.com/xpp1109/images/main/uPic/epZPaj.png)
* 在nacos配置中增加sentinel配置
![Nacos中的Sentinel配置, book](https://raw.githubusercontent.com/xpp1109/images/main/uPic/oZKeoU.png)
* 重启应用,请求getAuthorName接口。刷新sentinel。
![sentinel面板](https://raw.githubusercontent.com/xpp1109/images/main/uPic/fLszEN.png)
* 通过面板设置流控
![流控设置](https://raw.githubusercontent.com/xpp1109/images/main/uPic/WH77Fl.png)
![设置](https://raw.githubusercontent.com/xpp1109/images/main/uPic/VXasej.png)
![设置完](https://raw.githubusercontent.com/xpp1109/images/main/uPic/g6PwfF.png)
打开浏览器，快速刷新访问http://localhost:8080/getAuthorName.
![偶尔会出现流控界面](https://raw.githubusercontent.com/xpp1109/images/main/uPic/vPsuKE.png)
* **重启sentinel后数据会丢失，之后会介绍持久化方案**


