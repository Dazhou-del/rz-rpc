package com.dazhou.example.common.model;

import java.io.Serializable;

/**
 * 用户
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-07 10:10
 */
public class User implements Serializable {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}