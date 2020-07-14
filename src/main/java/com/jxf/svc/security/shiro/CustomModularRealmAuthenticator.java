package com.jxf.svc.security.shiro;

import java.util.Collection;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import com.jxf.svc.security.shiro.realm.AdminAuthorizingRealm;
import com.jxf.svc.security.shiro.realm.UfangAuthorizingRealm;

/**
 * @author wo 
 * 自定义Authenticator
 * 登录验证->通过AuthenticationFilter传入的token确定使用哪个realm做验证
 * 
 */
public class CustomModularRealmAuthenticator extends ModularRealmAuthenticator {

    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        // 判断getRealms()是否返回为空
        assertRealmsConfigured();
        // 强制转换回自定义的CustomizedToken
        CustomUsernamePasswordToken customizedToken = (CustomUsernamePasswordToken) authenticationToken;
        // 登录类型
        CustomUsernamePasswordToken.UserType userType = customizedToken.getUserType();
        // 所有Realm
        Collection<Realm> realms = getRealms();
        for (Realm realm : realms) {
            if(userType.equals(CustomUsernamePasswordToken.UserType.admin)) {
            	if (realm instanceof AdminAuthorizingRealm) {
            		return doSingleRealmAuthentication(realm, customizedToken);
            	}           	 
            }else if(userType.equals(CustomUsernamePasswordToken.UserType.ufang)){
            	if (realm instanceof UfangAuthorizingRealm) {
            		return doSingleRealmAuthentication(realm, customizedToken);
            	}
            }
        }
        return null;
    }

}

