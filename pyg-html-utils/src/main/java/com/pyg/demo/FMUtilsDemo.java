package com.pyg.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.jcraft.jsch.ChannelSftp;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FMUtilsDemo {

	/**
	 * 需求:测试freemarker入门 获取基本数据类型: string,数字类型.. 语法:${key} key是map的key
	 * 生成HTML页面三要素: 1,模版(首先创建模版) 2,数据 (模版需要的数据) 3,api代码(把数据设置到模版,生成HTML,写入磁盘)
	 * 
	 * @throws Exception
	 */
	@Test
	public void test01() throws Exception {
		// 创建freemarker核心配置对象
		Configuration cf = new Configuration(Configuration.getVersion());
		// 设置模版文件所在位置
		cf.setDirectoryForTemplateLoading(new File("F:\\template"));
		// 设置模版文件编码
		cf.setDefaultEncoding("UTF-8");

		// 读取模版文件,获取模版对象
		// 路径组合: F:\\template\\hello.ftl
		Template template = cf.getTemplate("hello.ftl");

		// 创建map对象,封装页面数据
		Map<String, Object> maps = new HashMap<String, Object>();
		// 封装数据
		// 模版页面需要什么数据,封装什么数据
		maps.put("name", "川普");
		maps.put("message", "赶紧下台吧");

		// 创建输出流对象,把文件写入磁盘
		Writer out = new FileWriter(new File("F:\\template\\out\\abc.html"));

		// 生成HTML文件
		template.process(maps, out);

		// 关闭
		out.close();

	}

	/**
	 * 需求:测试freemarker指令 assign指令
	 * 
	 * 
	 * @throws Exception
	 */
	@Test
	public void test02() throws Exception {
		// 创建freemarker核心配置对象
		Configuration cf = new Configuration(Configuration.getVersion());
		// 设置模版文件所在位置
		cf.setDirectoryForTemplateLoading(new File("F:\\template"));
		// 设置模版文件编码
		cf.setDefaultEncoding("UTF-8");

		// 读取模版文件,获取模版对象
		// 路径组合: F:\\template\\assain.ftl
		Template template = cf.getTemplate("assain.ftl");

		// 创建输出流对象,把文件写入磁盘
		Writer out = new FileWriter(new File("F:\\template\\out\\assain.html"));

		// 生成HTML文件
		template.process(null, out);

		// 关闭
		out.close();

	}

	/**
	 * 需求:freemarker判断指令if 语法: <#if condition> <#elseif conditon> <#else> </#if>
	 * 
	 * 
	 * @throws Exception
	 */
	@Test
	public void test03() throws Exception {
		// 创建freemarker核心配置对象
		Configuration cf = new Configuration(Configuration.getVersion());
		// 设置模版文件所在位置
		cf.setDirectoryForTemplateLoading(new File("F:\\template"));
		// 设置模版文件编码
		cf.setDefaultEncoding("UTF-8");

		// 读取模版文件,获取模版对象
		// 路径组合: F:\\template\\assain.ftl
		Template template = cf.getTemplate("myifelse.ftl");

		// 创建map对象,封装页面数据
		Map<String, Object> maps = new HashMap<String, Object>();
		// 封装数据
		// 模版页面需要什么数据,封装什么数据
		maps.put("flag", false);

		// 创建输出流对象,把文件写入磁盘
		Writer out = new FileWriter(new File("F:\\template\\out\\ifelse.html"));

		// 生成HTML文件
		template.process(maps, out);

		// 关闭
		out.close();

	}

	/**
	 * 需求:freemarker获取数据指令list 场景: List集合pList放入很多Person对象 jsp获取: <c:foreach
	 * item="{pList}" var="p" varStatus="person"> ${p.name} ${p.age} .......
	 * </c:foreach> ftl模版获取: <#list pList as p> 获取角标:${别名_index} ${p.name}
	 * ${p.age} ........ </#list>
	 * 
	 * 
	 * @throws Exception
	 */
	@Test
	public void test04() throws Exception {
		// 创建freemarker核心配置对象
		Configuration cf = new Configuration(Configuration.getVersion());
		// 设置模版文件所在位置
		cf.setDirectoryForTemplateLoading(new File("F:\\template"));
		// 设置模版文件编码
		cf.setDefaultEncoding("UTF-8");

		// 读取模版文件,获取模版对象
		// 路径组合: F:\\template\\list.ftl
		Template template = cf.getTemplate("list.ftl");

		// 创建list集合对象,封装person对象数据
		List<Person> pList = new ArrayList<Person>();
		// 创建person对象
		Person p1 = new Person();
		p1.setId("11111111111111");
		p1.setName("张无忌");
		p1.setAge(22);
		p1.setAddress("冰火岛");

		Person p2 = new Person();
		p2.setId("11111111111112");
		p2.setName("谢逊");
		p2.setAge(66);
		p2.setAddress("冰火岛");

		Person p3 = new Person();
		p3.setId("11111111111113");
		p3.setName("张翠山");
		p3.setAge(33);
		p3.setAddress("冰火岛");

		pList.add(p1);
		pList.add(p2);
		pList.add(p3);

		// 创建map对象,封装页面数据
		Map<String, Object> maps = new HashMap<String, Object>();
		// 封装数据
		// 模版页面需要什么数据,封装什么数据
		maps.put("pList", pList);

		File file = new File("F:\\template\\out\\list.html");
		// 创建输出流对象,把文件写入磁盘
		Writer out = new FileWriter(file);

		// 生成HTML文件
		template.process(maps, out);

		// 上传
		//InputStream in = new FileInputStream(file);

		//SftpUtil.upload("/data/images", in, "list.html",
				//SftpUtil.connect("192.168.66.66", 22, "root", "itcast"));

		// 关闭
		out.close();

	}

	/**
	 * 需求:freemarker测试内建函数 eval 功能:把json字符串转换成json对象
	 * 
	 * @throws Exception
	 */
	@Test
	public void test05() throws Exception {
		// 创建freemarker核心配置对象
		Configuration cf = new Configuration(Configuration.getVersion());
		// 设置模版文件所在位置
		cf.setDirectoryForTemplateLoading(new File("F:\\template"));
		// 设置模版文件编码
		cf.setDefaultEncoding("UTF-8");

		// 读取模版文件,获取模版对象
		// 路径组合: F:\\template\\list.ftl
		Template template = cf.getTemplate("eval.ftl");

		// 创建输出流对象,把文件写入磁盘
		Writer out = new FileWriter(new File("F:\\template\\out\\eval.html"));

		// 生成HTML文件
		template.process(null, out);

		// 关闭
		out.close();

	}

	/**
	 * 需求:freemarker测试内建函数 eval 功能:把json字符串转换成json对象
	 * 
	 * @throws Exception
	 */
	@Test
	public void test06() throws Exception {
		// 创建freemarker核心配置对象
		Configuration cf = new Configuration(Configuration.getVersion());
		// 设置模版文件所在位置
		cf.setDirectoryForTemplateLoading(new File("F:\\template"));
		// 设置模版文件编码
		cf.setDefaultEncoding("UTF-8");

		// 读取模版文件,获取模版对象
		// 路径组合: F:\\template\\date.ftl
		Template template = cf.getTemplate("date.ftl");

		// 创建map对象,封装页面数据
		Map<String, Object> maps = new HashMap<String, Object>();
		// 封装数据
		// 模版页面需要什么数据,封装什么数据
		maps.put("today", new Date());

		// 创建输出流对象,把文件写入磁盘
		Writer out = new FileWriter(new File("F:\\template\\out\\date.html"));

		// 生成HTML文件
		template.process(maps, out);

		// 关闭
		out.close();

	}

	/**
	 * 需求:freemarker数字类型数据处理 语法: 1,百分比格式 ${num?string.percent} 2,金额显示 ￥0.8888
	 * ${num?string.currency} 3,字符串显示格式 ${num?c}
	 * 
	 * @throws Exception
	 */
	@Test
	public void test07() throws Exception {
		// 创建freemarker核心配置对象
		Configuration cf = new Configuration(Configuration.getVersion());
		// 设置模版文件所在位置
		cf.setDirectoryForTemplateLoading(new File("F:\\template"));
		// 设置模版文件编码
		cf.setDefaultEncoding("UTF-8");

		// 读取模版文件,获取模版对象
		// 路径组合: F:\\template\\num.ftl
		Template template = cf.getTemplate("num.ftl");

		// 创建map对象,封装页面数据
		Map<String, Object> maps = new HashMap<String, Object>();
		// 封装数据
		// 模版页面需要什么数据,封装什么数据
		maps.put("num", 0.88888888);

		// 创建输出流对象,把文件写入磁盘
		Writer out = new FileWriter(new File("F:\\template\\out\\num.html"));

		// 生成HTML文件
		template.process(maps, out);

		// 关闭
		out.close();

	}

	/**
	 * 需求:处理freemarker空值情况 三种处理null值方式: 1,? ${name?default("默认值")} 2,!
	 * ${name!"默认值"} ${name!} 3,if <#if name??> ${name} </#if>
	 * 
	 * @throws Exception
	 */
	@Test
	public void test08() throws Exception {
		// 创建freemarker核心配置对象
		Configuration cf = new Configuration(Configuration.getVersion());
		// 设置模版文件所在位置
		cf.setDirectoryForTemplateLoading(new File("F:\\template"));
		// 设置模版文件编码
		cf.setDefaultEncoding("UTF-8");

		// 读取模版文件,获取模版对象
		// 路径组合: F:\\template\\assain.ftl
		Template template = cf.getTemplate("null.ftl");

		// 创建map对象,封装页面数据
		Map<String, Object> maps = new HashMap<String, Object>();
		// 封装数据
		// 模版页面需要什么数据,封装什么数据
		maps.put("name", "空值测试");

		File file = new File("F:\\template\\out\\null.html");

		// 创建输出流对象,把文件写入磁盘
		Writer out = new FileWriter(file);

		// 生成HTML文件
		template.process(maps, out);

		// 上传
		InputStream in = new FileInputStream(file);

		//SftpUtil.upload("/data/images", in, "null.html",
				//SftpUtil.connect("192.168.66.66", 22, "root", "itcast"));

		// 关闭
		out.close();

	}

	/**
	 * 文件上传测试:
	 */

}
