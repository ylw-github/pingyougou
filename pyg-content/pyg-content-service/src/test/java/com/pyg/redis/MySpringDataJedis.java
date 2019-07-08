package com.pyg.redis;

import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext-jedis.xml")
public class MySpringDataJedis {

	// 注入springdataredis模版对象
	@Autowired
	private RedisTemplate redisTemplate;

	// 测试1: 设置string类型值
	// String
	@Test
	public void test01() {
		redisTemplate.boundValueOps("username").set("张三丰");
	}

	// 测试2: 获取string类型值
	// String
	@Test
	public void test02() {
		String username = (String) redisTemplate.boundValueOps("username")
				.get();
		System.out.println(username);
	}

	// 测试3: 删除string类型值
	// String
	@Test
	public void test03() {
		redisTemplate.delete("username");

	}

	// 测试4: set数据类型添加操作
	// Set集合
	@Test
	public void test04() {
		// 添加值
		redisTemplate.boundSetOps("user").add("张三丰修炼九阳圣功");

	}

	// 测试5: 获取set集合类型数据
	// Set集合
	@Test
	public void test05() {
		// 添加值
		Set members = redisTemplate.boundSetOps("user").members();
		for (Object object : members) {
			System.out.println(object);
		}

	}

	// 测试6: 删除set集合类型数据
	// Set集合
	@Test
	public void test06() {
		// 添加值
		redisTemplate.delete("user");

	}

	// 测试7: list集合数据类型添加
	// list集合
	@Test
	public void test07() {
		// 添加值
		redisTemplate.boundListOps("mylist").rightPush("猪八戒");
		redisTemplate.boundListOps("mylist").rightPush("孙悟空");

	}

	// 测试8: list集合数据类型获取
	// list集合
	@Test
	public void test08() {
		// 添加值
		String name = (String) redisTemplate.boundListOps("mylist").leftPop();
		System.out.println("从左边出栈:" + name);

	}

	// 测试9: hash集合数据类型添加
	// list集合
	@Test
	public void test09() {
		// 添加值
		redisTemplate.boundHashOps("itcast").put("a", "张飞");
		redisTemplate.boundHashOps("itcast").put("b", "关羽");

	}

	// 测试10: hash集合数据类型获取
	// list集合
	@Test
	public void test10() {
		// 添加值
		redisTemplate.boundHashOps("index_cache").delete("1");
		
		

	}

}
