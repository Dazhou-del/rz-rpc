package com.dazhou.rzrpc.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-26 11:48
 */
public class Testss {
    public static void main(String[] args) {

            for (int i = 0; i < 5; i++) {
                try {
                    if (i==3){
                        int num=1/0;
                    }else {
                        System.out.println("业务正常执行");
                    }
                } catch (Exception e) {
                    System.out.println("try catch 在for循环里面时 出现了异常，for循环不会被中断");
                }
            }
        }


    }


