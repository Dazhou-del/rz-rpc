package com.dazhou.rzrpc.core.service.tcp;

import com.dazhou.rzrpc.core.protocol.TcpServerHandler;
import com.dazhou.rzrpc.core.service.HttpServer;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.parsetools.RecordParser;

/**
 * Tcp服务端
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-25 14:28
 */
public class VertxTcpServer implements HttpServer {

    private byte[] handleRequest(byte[] requestData){
        //在这里编写处理请求的逻辑，根据requestData构造响应数据并返回
        //这里只是一个示例，实际逻辑需要根据具体的业务需求来实现
        return "I is Server!".getBytes();
    }

    @Override
    public void doStart(int port) {
        //创建Vert.x实例
        Vertx vertx = Vertx.vertx();
        //创建Tcp服务器
        NetServer server = vertx.createNetServer();
        //处理请求
        server.connectHandler(socket->{
            // 构造 parser
            RecordParser parser = RecordParser.newFixed(8);


        });
        //使用自定义请求处理器
//        server.connectHandler(new TcpServerHandler());

        server.listen(port,result->{
            if (result.succeeded()){
                System.out.println("TCP server started on port " + port);
            }else {
                System.err.println("Failed to start TCP server: " + result.cause());
            }
        });
    }

    public static void main(String[] args) {
        new VertxTcpServer().doStart(8888);
    }

}
