# 工程简介
* Spring Boot: 2.3.7.RELEASE
* Spring Cloud Alibaba: 2.2.2.RELEASE
* JDK: 1.8
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


