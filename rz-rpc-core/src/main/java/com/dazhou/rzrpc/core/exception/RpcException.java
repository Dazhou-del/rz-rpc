package com.dazhou.rzrpc.core.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 通用异常
 *
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-04-04 23:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcException {


    private int errorCode;

    private String errorMessage;


}
