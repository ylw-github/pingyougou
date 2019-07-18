package com.pyg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

@SpringBootApplication
public class MyApplication {
	
	//入口函数
	public static void main(String[] args) {
		//入口
		SpringApplication.run(MyApplication.class, args);
	}
	
	

}
