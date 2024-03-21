package com.dazhou.rzrpc.core.test;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.KV;
import io.etcd.jetcd.kv.GetResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * EtcdDemo
 * @author <a href="https://github.com/Dazhou-del">Dazhou</a>
 * @create 2024-03-21 16:10
 */
public class EtcdRegistry {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //创建客户端
        Client client = Client.builder().endpoints("http://localhost:2379").build();

        KV kvClient = client.getKVClient();

        ByteSequence keys = ByteSequence.from("test_key".getBytes());
        ByteSequence values = ByteSequence.from("test_value".getBytes());

        // put the key-value
        kvClient.put(keys, keys).get();

        // get the CompletableFuture
        CompletableFuture<GetResponse> future = kvClient.get(keys);

        // get the value from CompletableFuture
        GetResponse getResponse = future.get();

        // delete the key
        kvClient.delete(keys).get();
    }
}
