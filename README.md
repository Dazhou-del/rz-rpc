# RzRPC

## 项目介绍

RzRPC是基于Java+Etcd+Vert.x+自定义协议实现的RPC框架，利用SPI动态拓展机制用户可自定义序列化器、负载均衡、重试、和容错策略等。

通过引入Spring Boot Starter,通过注解和配置文件快速使用框架，像调用本地方法一样轻松调用远程服务。

## 组织架构

```
rz-rpc
├── example-common -- 工具类及通用代码
├── example-consumer -- 消费端
├── example-provider -- 服务提供者
├── rz-rpc-core -- rpc主要实现
├── rz-rpc-easy -- 简易版rpc
```

## 技术选型

| 技术           | 说明                                                         | 官网                                                         |
| -------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| Etcd           | 高可用的分布式键值(key-value)数据库                          | https://etcd.io/                                             |
| vert.x         | 支持多种编程语言的高性能异步、非阻塞、响应式全栈java web框架 | https://vertx.io/docs/                                       |
| SpringBoot     | Web应用开发框架                                              | https://spring.io/projects/spring-boot                       |
| Hutool         | Java工具类库                                                 | https://github.com/looly/hutool                              |
| Lombok         | Java语言增强库                                               | [ https://github.com/rzwitserloot/lombok](https://github.com/rzwitserloot/lombok) |
| guava-retrying | Google Guava库的一个扩展包，可以为任意函数调用创建可配置的重试机制 | https://github.com/rholder/guava-retrying                    |
| kryo           | Java对象序列化框架                                           | https://github.com/EsotericSoftware/kryo                     |
| hessian        | Java对象序列化框架                                           | http://hessian.caucho.com/                                   |

##架构图

![image-20240401160054818](https://tptptptpt.oss-cn-guangzhou.aliyuncs.com/picture/image-20240401160054818.png)

## 关于作者

有问题可联系作者:

![image-20240401160708843](https://tptptptpt.oss-cn-guangzhou.aliyuncs.com/picture/image-20240401160708843.png)