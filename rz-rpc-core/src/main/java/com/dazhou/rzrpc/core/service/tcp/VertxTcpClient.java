package com.dazhou.rzrpc.core.service.tcp;

import cn.hutool.core.util.IdUtil;
import com.dazhou.rzrpc.core.RpcApplication;
import com.dazhou.rzrpc.core.model.RpcRequest;
import com.dazhou.rzrpc.core.model.RpcResponse;
import com.dazhou.rzrpc.core.model.ServiceMetaInfo;
import com.dazhou.rzrpc.core.protocol.*;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Vert.x 请求客户端
 *
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-30 16:06
 */
public class VertxTcpClient {

    /**
     * 发起请求
     *
     * @param selectedServiceMetaInfo
     * @param rpcRequest
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public static RpcResponse doRequest(ServiceMetaInfo selectedServiceMetaInfo, RpcRequest rpcRequest) throws InterruptedException, ExecutionException {
        //发起Tcp请求
        Vertx vertx = Vertx.vertx();
        NetClient netClient = vertx.createNetClient();
        CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();
        //建立连接
        netClient.connect(selectedServiceMetaInfo.getServicePort(), selectedServiceMetaInfo.getServiceHost(),
                result -> {
                    if (result.succeeded()) {
                        //建立连接成功
                        System.out.println("Connected to TCP server");
                        NetSocket socket = result.result();
                        //发送消息
                        //构建请求
                        ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
                        ProtocolMessage.Header header = new ProtocolMessage.Header();
                        header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
                        header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
                        header.setSerializer((byte) ProtocolMessageSerializerEnum.getEnumByValue(RpcApplication.getRpcConfig().getSerializer()).getKey());
                        header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
                        //生成全局请求ID
                        header.setRequestId(IdUtil.getSnowflakeNextId());
                        protocolMessage.setHeader(header);
                        protocolMessage.setBody(rpcRequest);
                        try {
                            //编码请求
                            Buffer encode = ProtocolMessageEncoder.encode(protocolMessage);
                            //发送数据
                            socket.write(encode);
                        } catch (IOException e) {
                            throw new RuntimeException("协议消息编码错误");
                        }
                        //接收响应 需要对接受的数据做粘包半包处理

                        TcpBufferHandlerWrapper tcpBufferHandlerWrapper = new TcpBufferHandlerWrapper(buffer -> {
                            //解码
                            try {
                                ProtocolMessage<RpcResponse> rpcResponseProtocolMessage =
                                        (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
                                responseFuture.complete(rpcResponseProtocolMessage.getBody());
                            } catch (IOException e) {
                                throw new RuntimeException("协议消息解码错误");
                            }
                        });
                        socket.handler(tcpBufferHandlerWrapper);
                    } else {
                        System.err.println("Failed to connect to TCP server");
                    }
                });
        RpcResponse rpcResponse = responseFuture.get();
        //关闭连接
        netClient.close();
        return rpcResponse;

    }
}
