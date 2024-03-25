package com.dazhou.rzrpc.core.protocol;

import lombok.Getter;

/**
 * 协议消息的状态枚举
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-25 14:09
 */
@Getter
public enum ProtocolMessageStatusEnum {

    OK("ok",20),
    BAD_REQUEST("badRequest",40),
    BAD_RESPONSE("badResponse",50);

    private final String text;

    private final int value;

    ProtocolMessageStatusEnum(String text,int value){
        this.text=text;
        this.value=value;
    }

    /**
     * 根据value 获取枚举值
     * @param value
     * @return
     */
    public static ProtocolMessageStatusEnum getEnumByValue(int value){
        for (ProtocolMessageStatusEnum protocolMessageStatusEnum : ProtocolMessageStatusEnum.values()) {
            if (protocolMessageStatusEnum.value==value){
                return protocolMessageStatusEnum;
            }
        }
        return null;
    }
}
