package com.pyg.solrj;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.HighlightEntry.Highlight;
import org.springframework.data.solr.core.query.result.HighlightPage;
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
		// 创建封装条件对象
		Criteria criteria = new Criteria("item_keywords").is("番茄");
		criteria.contains("16");
		// 把条件添加查询对象
		query.addCriteria(criteria);

		ScoredPage<TbItem> page = solrTemplate
				.queryForPage(query, TbItem.class);
		// 获取记录
		List<TbItem> content = page.getContent();
		System.out.println(content);
	}

	// 测试
	// 删除索引库
	@Test
	public void deleteIndex() {
		// 删除所有
		Query query = new SimpleQuery("*:*");
		 solrTemplate.delete(query);
		// 根据id删除索引
		//solrTemplate.deleteById(100000000000L + "");
		solrTemplate.commit();

	}

	// 测试
	// 高亮查询
	@Test
	public void findSolrIndexWithHighlight() {
		//创建高亮查询对象
		SimpleHighlightQuery query = new SimpleHighlightQuery();
		//条件查询
		Criteria criteria = new Criteria("item_title").is("番茄");
		//AND条件
		criteria.contains("16");
		
		//把条件添加到查询对象中
		query.addCriteria(criteria);
		
		//创建高亮对象,添加高亮操作
		HighlightOptions highlightOptions = new HighlightOptions();
		highlightOptions.addField("item_title");
		highlightOptions.setSimplePrefix("<font color='red'>");
		highlightOptions.setSimplePostfix("</font>");
		
		//设置高亮查询
		query.setHighlightOptions(highlightOptions);
		
		//执行查询
		HighlightPage<TbItem> highlightPage = solrTemplate.queryForHighlightPage(query, TbItem.class);
		
		//获取查询总记录数
		long totalElements = highlightPage.getTotalElements();
		System.out.println("查询总记录数:"+totalElements);
		//获取总记录
		List<TbItem> list = highlightPage.getContent();
		
		//循环集合对象
		for (TbItem tbItem : list) {
			
			//获取高亮
			List<Highlight> highlights = highlightPage.getHighlights(tbItem);
			Highlight highlight = highlights.get(0);
			List<String> snipplets = highlight.getSnipplets();
			System.out.println("高亮字段:"+snipplets);
		}

	}

}
