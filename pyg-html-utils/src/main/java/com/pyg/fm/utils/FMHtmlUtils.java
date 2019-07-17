package com.pyg.fm.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.pyg.mapper.TbGoodsDescMapper;
import com.pyg.mapper.TbGoodsMapper;
import com.pyg.mapper.TbItemCatMapper;
import com.pyg.mapper.TbItemMapper;
import com.pyg.pojo.TbGoods;
import com.pyg.pojo.TbGoodsDesc;
import com.pyg.pojo.TbGoodsExample;
import com.pyg.pojo.TbItem;
import com.pyg.pojo.TbItemCat;
import com.pyg.pojo.TbItemCatExample;
import com.pyg.pojo.TbItemExample;
import com.pyg.pojo.TbItemExample.Criteria;
import com.pyg.utils.FMUtils;

@Component
public class FMHtmlUtils {

	//注入mapper接口代理对象
	@Autowired
	private TbGoodsMapper goodsMapper;
	
	//注入商品描述mapper接口代理对象
	@Autowired
	private TbGoodsDescMapper goodsDescMapper;
	
	//注入tbitemMapper接口代理对象
	@Autowired
	private TbItemMapper itemMapper;
	
	//注入分类接口代理对象
	@Autowired
	private TbItemCatMapper itemCatMapper;
	
	
	
	/**
	 * 需求:静态页面生成初始化工作:把数据库所有的页面全部批量生成html
	 * 生成html页面
	 * 三要素:
	 * 1,模版文件(把html改造成ftl文件)
	 * 2,数据(查询所有商品数据)
	 * 3,freemarker api 生成HTML页面
	 * @throws Exception
	 */
	public void genHtml() throws Exception {
		
		//创建example对象
		TbGoodsExample goodsExample = new TbGoodsExample();
		//创建criteria对象
		com.pyg.pojo.TbGoodsExample.Criteria createCriteria2 = goodsExample.createCriteria();
		//设置查询参数
		createCriteria2.andCategory1IdIsNotNull();
		createCriteria2.andCategory2IdIsNotNull();
		createCriteria2.andCategory3IdIsNotNull();
		
		//查询
		List<TbGoods> list = goodsMapper.selectByExample(goodsExample);
		//循环spu列表数据
		for (TbGoods tbGoods : list) {
			//根据id查询商品描述数据
			TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(tbGoods.getId());
			//查询sku信息
			//创建example对象
			TbItemExample example = new TbItemExample();
			//创建criteria对象
			Criteria createCriteria = example.createCriteria();
			//设置查询参数
			createCriteria.andGoodsIdEqualTo(tbGoods.getId());
			//执行查询
			List<TbItem> itemList = itemMapper.selectByExample(example);
			
			//根据分类id查询分类名称
			//设置
			//查询第一级分类名称
			TbItemCat itemcat1 = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory1Id());
			
			//判断如何为空
			if(itemcat1==null){
				continue;
			}
			
			//查询第二级分类名称
			TbItemCat itemcat2 = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory2Id());
			
			
			//查询第三级分类名称
			TbItemCat itemcat3 = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id());
			
					
			// 创建map对象
			Map<String, Object> maps = new HashMap<String, Object>();
			maps.put("itemList",itemList);
			maps.put("goodsDesc", tbGoodsDesc);
			
			//创建
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("itemCat1", itemcat1.getName());
			map.put("itemCat2", itemcat2.getName());
			map.put("itemCat3", itemcat3.getName());
			//封装分类
			maps.put("map", map);
			
			//封装商品数据
			maps.put("goods", tbGoods);
			
			// 创建工具类对象
			FMUtils fUtils = new FMUtils();
			// 调用生成页面方法
			fUtils.ouputFile("item.ftl", tbGoods.getId()+".html", maps);
			
			
		}
		
		

	}

	public static void main(String[] args) throws Exception {
		// 加载spring配置文件
		ApplicationContext app = new ClassPathXmlApplicationContext(
				"classpath*:spring/*.xml");
		// 获取对象
		FMHtmlUtils htmlUtils = app.getBean(FMHtmlUtils.class);

		// 调用批量生成html静态页面方法
		htmlUtils.genHtml();
	}
}
