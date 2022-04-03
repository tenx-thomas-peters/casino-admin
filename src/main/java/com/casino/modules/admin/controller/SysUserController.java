package com.casino.modules.admin.controller;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.casino.modules.shiro.authc.util.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.casino.common.utils.UUIDGenerator;
import com.casino.modules.admin.common.entity.SysUser;
import com.casino.modules.admin.service.ISysUserService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/sys")
public class SysUserController {

    @Autowired
    ISysUserService sysUserService;

    @GetMapping(value = "/registerPage")
    public String registerPage(
            Model model,
            @ModelAttribute("sysUser") SysUser sysUser,
            HttpServletRequest request) {
        try {
            model.addAttribute("sysUser", sysUser);
        } catch (Exception e) {
            log.error("url: /registerPage --- method: registerPage --- error: " + e.toString());
        }

        return "views/admin/user/register";
    }

    @RequestMapping(value = "/register")
    public String register(Model model, @ModelAttribute("sysUser") SysUser sysUser, HttpServletRequest request) {
        try {
            if (StringUtils.isNotBlank(sysUser.getUserId())) {
                QueryWrapper<SysUser> qw = new QueryWrapper<>();
                qw.eq("user_id", sysUser.getUserId());
                SysUser oldMember = sysUserService.getOne(qw);

                if (oldMember != null) {
                    if (oldMember != null) {
                        model.addAttribute("resType", "error");
                        model.addAttribute("msg", "User already existed!");
                        return "views/admin/user/register";
                    }
                } else {
					sysUser.setSeq(UUIDGenerator.generate());

					MessageDigest md = MessageDigest.getInstance("MD5");
					String md5Pwd = Base64.encodeBase64String(md.digest(sysUser.getPassword().getBytes()));
					sysUser.setPassword(md5Pwd);

                    if (sysUserService.save(sysUser)) {
                        model.addAttribute("resType", "success");
                        model.addAttribute("msg", "Register success!");
                        return "views/admin/user/login";
                    } else {
                        model.addAttribute("resType", "error");
                        model.addAttribute("msg", "Register failed!");
                        return "views/admin/user/register";
                    }
                }
            }
        } catch (Exception e) {
            log.error("url: /register --- method: register --- error: " + e.toString());
        }

        return "views/admin/user/login";
    }

    @ResponseBody
    @RequestMapping(value = "/signin")
    public Map<String, String> signin(
            @RequestParam("loginUserID") String loginUserID,
            @RequestParam("loginUserPwd") String loginUserPwd,
            HttpServletRequest request) {
        Map<String, String> result = new HashMap<String, String>();
        try {
            QueryWrapper<SysUser> qw = new QueryWrapper<>();
            qw.eq("user_id", loginUserID);
			SysUser sysUser = sysUserService.getOne(qw);

            if (sysUser != null) {
				MessageDigest md = MessageDigest.getInstance("MD5");
				String md5Pwd = Base64.encodeBase64String(md.digest(loginUserPwd.getBytes()));

				if (org.apache.commons.lang.StringUtils.equals(sysUser.getPassword(), md5Pwd)) {
                    String token = JwtUtil.sign(loginUserID, loginUserPwd);
                    Subject subject = SecurityUtils.getSubject();
                    subject.getSession().setAttribute("token", token);

                    result.put("res", "success");
                    result.put("msg", "login Success!");
                } else {
                    result.put("res", "error");
                    result.put("msg", "Password Incorrect!");
                }
                return result;
            } else {
                result.put("res", "error");
                result.put("msg", "User not existed!");
                return result;
            }
        } catch (Exception e) {
            log.error("url: /signin --- method: signin --- error: " + e.toString());
        }

        return result;
    }
}
