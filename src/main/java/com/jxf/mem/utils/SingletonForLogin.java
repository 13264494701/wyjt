package com.jxf.mem.utils;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

/**
 * 连接池单例	一键登录
 * @author gaob
 **/
public class SingletonForLogin {
    private SingletonForLogin() {
    }

    public static OkHttpClient getInstance() {
        return Singleton.INSTANCE.getInstance();
    }

    private static enum Singleton {
        INSTANCE;
        private OkHttpClient singleton;

        private Singleton() {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(5L, TimeUnit.SECONDS);
            builder.readTimeout(5L, TimeUnit.SECONDS);
            builder.writeTimeout(5L, TimeUnit.SECONDS);
            ConnectionPool connectionPool = new ConnectionPool(50, 60, TimeUnit.SECONDS);
            builder.connectionPool(connectionPool);
            singleton = builder.build();
        }

        public OkHttpClient getInstance() {
            return singleton;
        }
    }
}
