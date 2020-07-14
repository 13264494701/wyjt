package com.jxf.svc.security.shiro;

import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;

import com.jxf.svc.security.Principal;
import com.jxf.svc.security.shiro.realm.AdminAuthorizingRealm;
import com.jxf.svc.security.shiro.realm.UfangAuthorizingRealm;

public class CustomModularRealmAuthorizer extends ModularRealmAuthorizer {
    @Override
    public boolean isPermitted(PrincipalCollection principals, String permission) {
        assertRealmsConfigured();
        Principal principal = (Principal) principals.getPrimaryPrincipal();
        //只有当前授权用户principal类型和realm类型对应，才去验证用户的权限是否满足，否则直接拒绝。避免遍历realms
        for (Realm realm : getRealms()) {
            if (!(realm instanceof Authorizer)) {continue;}
            if (principal.getUserType().equals(Principal.UserType.admin)) {
                if (realm instanceof AdminAuthorizingRealm) {
                    return ((AdminAuthorizingRealm) realm).isPermitted(principals, permission);
                }
            }
            if (principal.getUserType().equals(Principal.UserType.ufang)) {
                if (realm instanceof UfangAuthorizingRealm) {
                    return ((UfangAuthorizingRealm) realm).isPermitted(principals, permission);
                }
            }

        }
        return false;
    }
}
