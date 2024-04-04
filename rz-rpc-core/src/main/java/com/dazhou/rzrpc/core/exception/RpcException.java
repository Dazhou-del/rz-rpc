package com.dazhou.rzrpc.core.exception;

import lombok.Getter;

/**
 * 通用异常
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-04-04 23:05
 */
@Getter
public enum RpcException {

    ok(200,"成功"),
    error(400,"失败");



    private final int value;

    private final String text;

    RpcException(int value,String text){
        this.text=text;
        this.value=value;
    }
}
