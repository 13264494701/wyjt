package com.jxf.test;


import com.jxf.svc.cache.RedisUtils;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

public class MemberServiceTest extends BaseJunit4Test {


    @Test // 标明是测试方法
    @Transactional // 标明此方法需使用事务
    @Rollback(false) // 标明使用完此方法后事务不回滚,true时为回滚
    public void insert() {
        Random random = new Random();
        int id = random.nextInt(9999);
        System.out.println(id);
        RedisUtils.put("loginInfo" + id, "key", "value");
        for (int i = 0; i < 5; i++) {
            String s = (String) RedisUtils.getHashKey("loginInfo" + id, "key");
            System.out.println(i + "|" + s);
            if (s != null) {
                break;
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
