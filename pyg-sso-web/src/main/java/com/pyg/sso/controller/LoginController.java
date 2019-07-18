package com.pyg.sso.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
	
	/**
	 * 需求:获取用户名称
	 * http://localhost:8084/login/loadLoginName
	 */
	@RequestMapping("/loadLoginName")
	public Map<String, String> loadLoginName(){
		
		//从安全框架中获取用户名
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		//创建map对象,封装用户名
		Map<String, String> map = new HashMap<String, String>();
		map.put("loginName", username);
		
		return map;
		
	}

}
