package com.dazhou.rzrpc.core.test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-20 10:34
 */

class Dog{
    private String a="22";
    static {
        System.out.println("Loading Dog");
    }
}
class Cat{
    public void Cat(){

    }
    private String ab="22";
    static {
        System.out.println("Loading Cat");

    }
    public void call(){
        System.out.println("Jioa");
    }
}

public class ClassTest {


    public static void main(String[] args) throws Exception {
//        Class<?> aClass = Class.forName("com.dazhou.rzrpc.core.test.Dog");
        Class<?> aClass2 = Class.forName("com.dazhou.rzrpc.core.test.Cat");
        System.out.println(aClass2.getName());
//        System.out.println(aClass2.getDec());

//        Dog dog = new Dog();
//        Class<? extends Dog> aClass1 = dog.getClass();
        Class<Dog> dogClass = Dog.class;

    }
}
