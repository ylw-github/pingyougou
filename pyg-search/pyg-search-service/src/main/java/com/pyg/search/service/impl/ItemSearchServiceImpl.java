package com.pyg.search.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.result.HighlightEntry.Highlight;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.web.bind.annotation.RequestBody;

import com.alibaba.dubbo.config.annotation.Service;
import com.pyg.pojo.TbItem;
import com.pyg.search.service.ItemSearchService;

@Service
public class ItemSearchServiceImpl implements ItemSearchService {

	// 注入solr模版对象
	@Autowired
	private SolrTemplate solrTemplate;

	/**
	 * 需求:根据前台页面传递搜索关键字进行搜索 $scope.searchMap = {"keywords":"华为"};
	 * 
	 * @param searchMap
	 * @return
	 */
	public Map<String, Object> searchList(Map<String, Object> searchMap) {
		// 创建返回值对象,封装结果
		Map<String, Object> maps = new HashMap<String, Object>();
		// 获取主查询条件
		String keywords = (String) searchMap.get("keywords");

		// 创建高亮查询SimpleHighlightQuery对象,封装所有查询条件
		SimpleHighlightQuery query = new SimpleHighlightQuery();

		// 创建criteria对象,封装查询参数
		// 指定复制域为查询字段
		Criteria criteria = null;

		// 1,主查询条件
		// 判断主查询条件是否为空
		if (keywords != null && !"".equals(keywords)) {
			// 添加条件
			criteria = new Criteria("item_keywords").is(keywords);
		} else {
			// 查询所有
			criteria = new Criteria().expression("*:*");
		}
		// 把条件添加query对象
		query.addCriteria(criteria);

		// 2,高亮设置
		// 创建高亮设置对象
		HighlightOptions highlightOptions = new HighlightOptions();
		// 指定设置高亮域字段
		highlightOptions.addField("item_title");
		// 设置前缀
		highlightOptions.setSimplePrefix("<font color='red'>");
		// 设置后缀
		highlightOptions.setSimplePostfix("</font>");

		// 设置高亮查询
		query.setHighlightOptions(highlightOptions);

		// 3,根据分类查询
		// 获取分类参数
		String category = (String) searchMap.get("category");
		// 判断分类值是否存在
		if (category != null && !"".equals(category)) {
			// 根据分类查询
			Criteria criteria2 = new Criteria("item_category").is(category);
			// 创建过滤对象
			FilterQuery filterQuery = new SimpleFilterQuery(criteria2);
			// 把过滤查询添加query对象
			query.addFilterQuery(filterQuery);
		}

		// 4,根据品牌过滤查询
		// 获取品牌参数
		String brand = (String) searchMap.get("brand");
		// 判断分类值是否存在
		if (brand != null && !"".equals(brand)) {
			// 根据分类查询
			Criteria criteria2 = new Criteria("item_brand").is(brand);
			// 创建过滤对象
			FilterQuery filterQuery = new SimpleFilterQuery(criteria2);
			// 把过滤查询添加query对象
			query.addFilterQuery(filterQuery);
		}

		// 5,根据规格查询
		// 获取规格对象
		Map<String, String> specMap = (Map<String, String>) searchMap
				.get("spec");
		// 获取规格值
		for (String key : specMap.keySet()) {
			String value = (String) specMap.get(key);
			// 根据分类查询
			Criteria criteria2 = new Criteria("item_spec_" + key).is(value);
			// 创建过滤对象
			FilterQuery filterQuery = new SimpleFilterQuery(criteria2);
			// 把过滤查询添加query对象
			query.addFilterQuery(filterQuery);
		}

		// 6,价格过滤查询
		// 获取价格值
		// 数据格式:0-500,500-1000....3000-*
		String price = (String) searchMap.get("price");
		// 判断分类值是否存在
		if (price != null && !"".equals(price)) {

			// 切割价格
			String[] prices = price.split("-");

			// 判断最低价格如何不是0
			if (prices[0] != "0") {
				// 根据分类查询
				Criteria criteria2 = new Criteria("item_price")
						.greaterThanEqual(prices[0]);
				// 创建过滤对象
				FilterQuery filterQuery = new SimpleFilterQuery(criteria2);
				// 把过滤查询添加query对象
				query.addFilterQuery(filterQuery);

			}

			// 判断最高价格不为*
			if (prices[1] != "*") {
				// 根据分类查询
				Criteria criteria2 = new Criteria("item_price")
						.lessThanEqual(prices[1]);
				// 创建过滤对象
				FilterQuery filterQuery = new SimpleFilterQuery(criteria2);
				// 把过滤查询添加query对象
				query.addFilterQuery(filterQuery);
			}

		}

		// 7,商品排序
		// 获取排序字段
		String sortField = (String) searchMap.get("sortField");
		// 获取排序类型: ASC DESC
		String sortValue = (String) searchMap.get("sort");
		// 判断搜索域字段是否为空
		if (sortField != null && !"".equals(sortField)) {
			// 根据域字段进行排序
			// 判断排序类型: ASC,DESC
			if (sortValue.equals("ASC")) {
				Sort sort = new Sort(Sort.Direction.ASC, "item_" + sortField);
				query.addSort(sort);
			}
			if (sortValue.equals("DESC")) {
				Sort sort = new Sort(Sort.Direction.DESC, "item_" + sortField);
				query.addSort(sort);
			}

		} else {
			// 默认排序: 销量 ,评价
			// 默认根据价格排序
			// 创建排序对象
			Sort sort = new Sort(Sort.Direction.ASC, "item_price");
			query.addSort(sort);

		}
		
		
		//8,分页查询
		//获取分页值
		//获取当前页
		Integer pageNo = (Integer) searchMap.get("pageNo");
		//获取每页显示条数
		Integer pageSize = (Integer) searchMap.get("pageSize");
		
		//判断当前页是否为空
		if(pageNo==null){
			pageNo=1;
		}
		//判断每页显示的条数是否为空
		if(pageSize==null){
			pageSize=30;
		}
		
		//计算查询起始页
		Integer startNo = (pageNo-1)*pageSize;
		//设置分页
		query.setOffset(startNo);
		query.setRows(pageSize);

		// 执行查询
		HighlightPage<TbItem> hpage = solrTemplate.queryForHighlightPage(query,
				TbItem.class);

		// 获取高亮
		List<TbItem> itemList = hpage.getContent();
		// 循环搜索商品集合,获取高亮
		for (TbItem tbItem : itemList) {
			// 获取高亮
			List<Highlight> highlights = hpage.getHighlights(tbItem);
			// 判断高亮是否存在
			if (highlights != null && highlights.size() > 0) {
				// 获取高亮
				Highlight highlight = highlights.get(0);
				// 获取高亮值[{}]
				List<String> snipplets = highlight.getSnipplets();
				// 设置高亮字段
				tbItem.setTitle(snipplets.get(0));
			}

		}

		// 封装结果
		maps.put("rows", hpage.getContent());
		//封装总页码
		maps.put("totalPages", hpage.getTotalPages());
		//封装总记录数
		maps.put("total", hpage.getTotalElements());
		return maps;
	}

}
