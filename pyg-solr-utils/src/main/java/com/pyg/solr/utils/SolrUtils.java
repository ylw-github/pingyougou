package com.pyg.solr.utils;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.pyg.mapper.TbItemMapper;
import com.pyg.pojo.TbItem;
import com.pyg.pojo.TbItemExample;
import com.pyg.pojo.TbItemExample.Criteria;

@Component
public class SolrUtils {

	// 注入商品mapper接口代理对象
	@Autowired
	private TbItemMapper itemMapper;
	
	//注入solr模版对象
	@Autowired
	private SolrTemplate solrTemplate;

	/**
	 * 需求:查询数据库数据,把数据导入索引库
	 * 
	 * @param args
	 */
	public void importData() {
		// 创建example对象
		TbItemExample example = new TbItemExample();
		// 设置参数:必须是已审核商品
		Criteria createCriteria = example.createCriteria();
		// 设置查询参数
		createCriteria.andStatusEqualTo("1");
		// 查询数据库
		List<TbItem> list = itemMapper.selectByExample(example);
		
		//导入规格数据
		//循环集合,获取规格
		for (TbItem tbItem : list) {
			String spec = tbItem.getSpec();
			//转换成map对象
			Map<String, String> specMap = (Map<String, String>) JSON.parse(spec);
			//把值添加动态域
			tbItem.setSpecMap(specMap);
		}
		
		//添加索引库
		solrTemplate.saveBeans(list);

		//提交
		solrTemplate.commit();

	}
	// java -jar xx.jar
	public static void main(String[] args) {
		// 加载spring配置文件
		ApplicationContext app = new ClassPathXmlApplicationContext(
				"classpath*:spring/*.xml");
		//获取SolrUtils对象
		SolrUtils solrUtils = app.getBean(SolrUtils.class);
		//调用导入索引库方法
		solrUtils.importData();
	}

}
