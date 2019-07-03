package com.pyg.manager.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
	
	/**
	 * 需求:获取当前用户的登录信息
	 */
	@RequestMapping("/loadLoginName")
	public Map loadLoginName(){
		
		//获取用户名
		//使用安全框架获取用户登录名
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		//创建map对象
		Map map = new HashMap();
		//把用户名放入map
		map.put("loginName", username);
		
		return map;
		
	}

}
