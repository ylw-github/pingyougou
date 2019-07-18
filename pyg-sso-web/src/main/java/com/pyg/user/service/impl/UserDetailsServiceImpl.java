package com.pyg.user.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.pyg.pojo.TbSeller;

public class UserDetailsServiceImpl implements UserDetailsService {
	


	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		//定义角色封装集合
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		//添加角色
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));		
		//调用商家服务对象,查询商家密码
	
		return new User(username,"", authorities);
		
	}

}
