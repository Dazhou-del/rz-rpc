package com.dazhou.rzrpc.core.service.tcp;

import io.vertx.core.Vertx;
import io.vertx.core.net.NetSocket;

/**
 * Tcp客户端
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-25 14:39
 */
public class VertxTcpClient {
    private void start(){
        //创建Vert.x实例
        Vertx vertx = Vertx.vertx();

        //连接服务器
        vertx.createNetClient().connect(8888,"localhost",result->{
            if (result.succeeded()){
                System.out.println("Connected to TCP server");
                NetSocket socket = result.result();
                //发送数据
                socket.write("Hello,server");
                //接受响应
                socket.handler(buffer -> {
                    System.out.println("Received response from server: " + buffer.toString());
                });
            }else {
                System.err.println("Failed to connect to TCP server");
            }
        });

    }

    public static void main(String[] args) {
        new VertxTcpClient().start();;
    }
}
