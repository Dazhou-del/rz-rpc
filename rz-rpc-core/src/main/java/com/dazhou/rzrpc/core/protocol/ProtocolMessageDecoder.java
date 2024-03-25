package com.dazhou.rzrpc.core.protocol;

import com.dazhou.rzrpc.core.model.RpcRequest;
import com.dazhou.rzrpc.core.model.RpcResponse;
import com.dazhou.rzrpc.core.serializer.Serializer;
import com.dazhou.rzrpc.core.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;

/**
 * 协议消息解码器
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-25 15:28
 */
public class ProtocolMessageDecoder {

    public static ProtocolMessage<?> decode(Buffer buffer) throws IOException{
        //分别从指定位置读取buffer
        ProtocolMessage.Header header = new ProtocolMessage.Header();
        byte magic = buffer.getByte(0);
        if (magic!=ProtocolConstant.PROTOCOL_MAGIC){
            throw new RuntimeException("消息 magic 非法");
        }
        header.setMagic(buffer.getByte(0));
        header.setVersion(buffer.getByte(1));

        header.setSerializer(buffer.getByte(2));
        header.setType(buffer.getByte(3));
        header.setStatus(buffer.getByte(4));
        header.setRequestId(buffer.getLong(5));
        //一个long占8个字节
        header.setBodyLength(buffer.getInt(13));

        //解决粘包问题，只读指定长度的数据 从17开始到17+header.getBodyLength()结束
        byte[] bufferBytes = buffer.getBytes(17, 17 + header.getBodyLength());

        //解析消息体
        //获取序列化
        ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum.getEnumByKey(header.getSerializer());
        Serializer serializer = SerializerFactory.getInstance(serializerEnum.getValue());
        if (serializer==null){
            throw new RuntimeException("序列化消息的协议不存在");
        }
        ProtocolMessageTypeEnum typeEnum = ProtocolMessageTypeEnum.getEnumByKey(header.getType());
        switch (typeEnum){
            case REQUEST:
                RpcRequest rpcRequest = serializer.deserialize(bufferBytes, RpcRequest.class);
                return new ProtocolMessage<>(header,rpcRequest);
            case RESPONSE:
                RpcResponse rpcResponse = serializer.deserialize(bufferBytes, RpcResponse.class);
                return new ProtocolMessage<>(header,rpcResponse);
            case HEART_BEAT:
            case OTHERS:
            default:
                throw new RuntimeException("暂不支持该消息类型");
        }
    }
}
