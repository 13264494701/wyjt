package com.jxf.svc.security.shiro;

import java.io.Serializable;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;

public class CustomSessionIdGenerator extends JavaUuidSessionIdGenerator {

	static int count = 0;

    @Override
    public Serializable generateId(Session session) {
        //如果进行了基于 JWT 的用户认证，可以获取加密生成的 JWT 串并返回，<JWT, Session> 存入 Shiro 会话池
        // SessionId 即为 JWT 串，可通过 JWT 从会话池中获取相应的会话信息
        if (++count % 2 == 0)
            return String.valueOf(count);

        //如果不存在用户认证，随便返回一个值，但不能为 null
        return super.generateId(session);
    }

}
