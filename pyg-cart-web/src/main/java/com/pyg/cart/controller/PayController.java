package com.pyg.cart.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pay.service.WeixinPayService;

@RestController
@RequestMapping("/pay")
public class PayController {
	
	//注入支付service接口对象
	@Reference
	private WeixinPayService weixinPayService;
	
	/**
	 * 需求:生成本地支付二维码
	 */
	@RequestMapping("/createNative")
	public Map<String, Object> createNative(){
		Map map = weixinPayService.createNative("111111111", "1");
		return map;
	}

}
