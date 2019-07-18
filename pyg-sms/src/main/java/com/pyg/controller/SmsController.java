package com.pyg.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

@RestController
public class SmsController {

	// 注入jms模版对象
	@Autowired
	private JmsTemplate jmsTemplate;

	/**
	 * 需求:测试短信网关服务: 发送消息 队列:queue
	 */
	@RequestMapping("/sendSms")
	public void sendSms() {
		// 创建map对象
		Map<String, String> mapMessage = new HashMap<String, String>();
		// 放入消息
		// 手机号
		mapMessage.put("mobile", "18513652029");
		// 签名
		mapMessage.put("signName", "黑马");

		// 创建map
		Map<String, String> map = new HashMap<String, String>();
		map.put("code", "666666");

		mapMessage.put("number", JSON.toJSONString(map));

		// 给短信发送网关服务发送消息 pyg-sms
		jmsTemplate.convertAndSend("smsQueue",JSON.toJSONString(mapMessage));
	}

}
