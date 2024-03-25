package com.dazhou.rzrpc.core.service.tcp;

import com.dazhou.rzrpc.core.protocol.TcpServerHandler;
import com.dazhou.rzrpc.core.service.HttpServer;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;

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
//        server.connectHandler(socket->{
//            //处理请求
//            socket.handler(buffer -> {
//                //处理接受到的字节数组
//                byte[] bufferBytes = buffer.getBytes();
//                //在这里进行自定义的字节数组处理逻辑，比如解析请求、调用服务、构造响应等
//                byte[] responseData  = handleRequest(bufferBytes);
//                //发送响应
//                socket.write(Buffer.buffer(responseData));
//            });
//        });
        server.connectHandler(new TcpServerHandler());

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
