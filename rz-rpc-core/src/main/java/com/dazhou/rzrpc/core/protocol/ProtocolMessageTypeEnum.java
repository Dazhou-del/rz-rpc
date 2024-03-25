package com.dazhou.rzrpc.core.protocol;

import lombok.Getter;

/**
 * 协议消息的类型枚举
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-25 14:16
 */
@Getter
public enum ProtocolMessageTypeEnum {

    REQUEST(0),
    RESPONSE(1),
    HEART_BEAT(2),
    OTHERS(3);

    private final int key;

    ProtocolMessageTypeEnum(int key){
        this.key=key;
    }

    /**
     * 根据value获取枚举
     * @param key
     * @return
     */
    public static ProtocolMessageTypeEnum getEnumByKey(int key){
        for (ProtocolMessageTypeEnum typeEnum : ProtocolMessageTypeEnum.values()) {
            if (typeEnum.key==key){
                return typeEnum;
            }
        }
        return null;
    }
}
