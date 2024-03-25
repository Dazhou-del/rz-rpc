package com.dazhou.rzrpc.core.protocol;

/**
 * 协议常量
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-25 12:00
 */
public interface ProtocolConstant {
    /**
     * 消息头长度
     */
    int MESSAGE_HEADER_LENGTH = 17;

    /**
     * 协议魔数
     */
    byte PROTOCOL_MAGIC = 0x1;

    /**
     * 协议版本号
     */
    byte PROTOCOL_VERSION = 0x1;
}
