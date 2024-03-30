package com.dazhou.rzrpc.core.service.tcp;

import com.dazhou.rzrpc.core.model.RpcRequest;
import com.dazhou.rzrpc.core.model.RpcResponse;
import com.dazhou.rzrpc.core.protocol.ProtocolMessage;
import com.dazhou.rzrpc.core.protocol.ProtocolMessageDecoder;
import com.dazhou.rzrpc.core.protocol.ProtocolMessageEncoder;
import com.dazhou.rzrpc.core.protocol.ProtocolMessageTypeEnum;
import com.dazhou.rzrpc.core.registry.LocalRegistry;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 请求处理器
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-25 16:05
 */
public class TcpServerHandler implements Handler<NetSocket> {
    @Override
    public void handle(NetSocket netSocket) {
        //处理连接

//        netSocket.handler(buffer -> {
        TcpBufferHandlerWrapper bufferHandlerWrapper = new TcpBufferHandlerWrapper(buffer -> {
            //接受请求，解码
            ProtocolMessage<RpcRequest> protocolMessage;
            try {
                protocolMessage = (ProtocolMessage<RpcRequest>) ProtocolMessageDecoder.decode(buffer);
            } catch (IOException e) {
                throw new RuntimeException("协议消息解码错误");
            }
            RpcRequest rpcRequest = protocolMessage.getBody();
            //处理请求
            //构建响应对象
            RpcResponse rpcResponse = new RpcResponse();
            // 获取要调用的服务实现类，通过反射调用
            try {
                Class<?> implClass = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                Object result = method.invoke(implClass.newInstance(), rpcRequest.getArgs());
                //封装结果放回
                rpcResponse.setData(result);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMessage("ok");
            } catch (Exception e) {
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }
            //发送响应，编码
            ProtocolMessage.Header header = protocolMessage.getHeader();
            header.setType((byte) ProtocolMessageTypeEnum.RESPONSE.getKey());
            ProtocolMessage<RpcResponse> rpcResponseProtocolMessage = new ProtocolMessage<>(header, rpcResponse);
            //使用编码器进行编码 
            try {
                Buffer encode = ProtocolMessageEncoder.encode(rpcResponseProtocolMessage);
                //写到Socket中
                netSocket.write(encode);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
        netSocket.handler(bufferHandlerWrapper);
    }
}
