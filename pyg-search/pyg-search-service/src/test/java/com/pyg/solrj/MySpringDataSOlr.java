package com.pyg.solrj;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.pyg.pojo.TbItem;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext-solr.xml")
public class MySpringDataSOlr {

	// 注入solr模版对象
	@Autowired
	private SolrTemplate solrTemplate;

	// 测试
	// 向索引库添加数据
	@Test
	public void addIndex() {
		// 创建商品对象
		TbItem item = new TbItem();
		//
		item.setId(100000000000L);
		item.setGoodsId(10101010101L);
		item.setTitle("番茄手机16G 5");
		item.setPrice(new BigDecimal(10000000));
		item.setBrand("番茄");
		item.setCategory("手机");

		// 写入数据到索引库
		solrTemplate.saveBean(item);
		// 提交
		solrTemplate.commit();

	}

	// 测试
	// 根据id查询
	@Test
	public void findIndexByID() {

		TbItem item = solrTemplate.getById(100000000000L, TbItem.class);
		System.out.println(item);

	}

	// 测试
	// 分页查询
	@Test
	public void findIndexByPage() {

		// 创建solrQuery对象,封装条件
		Query query = new SimpleQuery("*:*");
		// 设置分页条件
		// 设置分页查询起始位置
		query.setOffset(0);
		// 设置分页查询每页显示条数
		query.setRows(10);

		// 执行查询
		ScoredPage<TbItem> page = solrTemplate
				.queryForPage(query, TbItem.class);

		// 获取分页记录
		List<TbItem> list = page.getContent();
		// 获取总记录数
		long totalElements = page.getTotalElements();
		System.out.println(totalElements + "====" + list);

	}

	// 测试
	// 条件查询
	@Test
	public void findIndexByCritera() {

		// 创建solrQuery对象,封装条件
		Query query = new SimpleQuery("*:*");
		//创建封装条件对象
		Criteria criteria = new Criteria("item_keywords").is("番茄");
		criteria.contains("16");
		//把条件添加查询对象
		query.addCriteria(criteria);
		
		ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
		//获取记录
		List<TbItem> content = page.getContent();
		System.out.println(content);
	}

}
