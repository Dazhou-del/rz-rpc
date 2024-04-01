# RzRPC

## 项目介绍

RzRPC是基于Java+Etcd+Vert.x+自定义协议实现的RPC框架，利用SPI动态拓展机制用户可自定义序列化器、负载均衡、重试、和容错策略等。通过引入Spring Boot Starter,通过注解和配置文件快速使用框架，像调用本地方法一样轻松调用远程服务。

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

## 架构图

![image-20240401160054818](https://tptptptpt.oss-cn-guangzhou.aliyuncs.com/picture/image-20240401160054818.png)

## 整体调用链路

![QQ图片20240401180017](https://tptptptpt.oss-cn-guangzhou.aliyuncs.com/picture/QQ%E5%9B%BE%E7%89%8720240401180017.png)

蓝色->consumer 黄色->rpc-core 粉色-> 注册中心 绿色->提供端

> 调用流程

1. 消费者通过服务代理工厂创建代理对象。
2. 通过代理对象调用具体的方法
3. 代理对象会调用调用invoke方法。
4. 在invoke方法中，构建请求参数。
5. 根据注册中心工厂，获取具体的注册中心实现类，拉取全部的服务列表。
6. 根据负载均衡工厂，获取具体的负载均衡实现类，根据设置的负载均衡方法获取具体的服务。
7. 根据重试策略工厂，获取具体的重试策略实现类，选择几天的实现策略。
8. 发起TCP请求，与服务端建立连接，构建自定义协议参数，对自定义协议进行编码转成Buffer，之后向服务端发送Buffer。
9. 服务端需要对数据进行粘包半包处理，保证接收到数据是完整的，之后数据进行解码。通过发送过来的数据 反射调用提供者中真正的方法。获取结果，将结果编码然后响应。
10. VertxTcpClient接收到响应后，对响应进行解码，返回数据。
11. 在invoke中接收到VertxTcpClient类返回的结果后，将数据返回给消费端，这是消费端就获取到了调用真实接口的结果。

## 关于作者

有问题可联系作者:

![image-20240401160708843](https://tptptptpt.oss-cn-guangzhou.aliyuncs.com/picture/image-20240401160708843.png)