package com.casino.modules.shiro.authc;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.casino.common.constant.CommonConstant;
import com.casino.modules.admin.common.entity.SysUser;
import com.casino.modules.admin.service.ISysUserService;
import com.casino.modules.shiro.authc.util.JwtUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class MyRealm extends AuthorizingRealm {
    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SysUser sysUser = null;
        String userid = null;

        if (principals != null) {
            sysUser = (SysUser) principals.getPrimaryPrincipal();
            userid = sysUser.getSeq();
        }

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        List<String> roles = null;
        String rolesStr = stringRedisTemplate.opsForValue().get(CommonConstant.PREFIX_USER_ROLE + userid);

        if (rolesStr != null) {
            roles = JSON.parseArray(rolesStr.toString(), String.class);
        }

        stringRedisTemplate.expire(CommonConstant.PREFIX_USER_ROLE + userid, CommonConstant.TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);

        info.setRoles(new HashSet<>(roles));

        String permissions = "sys:role:update2,sys:role:add,/sys/user/add";

        Set<String> permission = new HashSet<>(Arrays.asList(permissions.split(",")));
        Set<String> permissionSet = new HashSet<>();

        info.addStringPermissions(permissionSet);

        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        if (token == null) {
            throw new AuthenticationException("token is empty");
        }
        String userid = JwtUtil.getUserId(token);

        if (userid == null) {
            throw new AuthenticationException("token is expired");
        }

        QueryWrapper<SysUser> qw = new QueryWrapper<>();
        qw.eq("user_id", userid);
        SysUser sysUser = sysUserService.getOne(qw);

        if (sysUser == null) {
            throw new AuthenticationException("user is not exist");
        }

        return new SimpleAuthenticationInfo(sysUser, token, getName());
    }
}