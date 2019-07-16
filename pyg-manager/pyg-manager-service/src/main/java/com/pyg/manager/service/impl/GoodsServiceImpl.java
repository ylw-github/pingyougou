package com.pyg.manager.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQObjectMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pyg.manager.service.GoodsService;
import com.pyg.mapper.TbBrandMapper;
import com.pyg.mapper.TbGoodsDescMapper;
import com.pyg.mapper.TbGoodsMapper;
import com.pyg.mapper.TbItemCatMapper;
import com.pyg.mapper.TbItemMapper;
import com.pyg.mapper.TbSellerMapper;
import com.pyg.pojo.TbBrand;
import com.pyg.pojo.TbGoods;
import com.pyg.pojo.TbGoodsDesc;
import com.pyg.pojo.TbGoodsExample;
import com.pyg.pojo.TbGoodsExample.Criteria;
import com.pyg.pojo.TbItem;
import com.pyg.pojo.TbItemCat;
import com.pyg.pojo.TbItemExample;
import com.pyg.pojo.TbSeller;
import com.pyg.utils.PageResult;
import com.pyg.utils.PygResult;
import com.pyg.vo.Goods;

/**
 * 服务实现层
 * 
 * @author Administrator
 * 
 */
@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private TbGoodsMapper goodsMapper;

	/**
	 * 查询全部
	 */
	@Override
	public List<TbGoods> findAll() {
		return goodsMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	// 注入货品描述mapper接口代理对象
	@Autowired
	private TbGoodsDescMapper goodsDescMapper;

	// 注入品牌mapper接口代理对象
	@Autowired
	private TbBrandMapper brandMapper;

	// 注入分类mapper接口代理对象
	@Autowired
	private TbItemCatMapper itemCatMapper;

	// 注入商品mapper接口代理对象
	@Autowired
	private TbItemMapper itemMapper;

	// 注入商家mapper接口代理对象
	@Autowired
	private TbSellerMapper sellerMapper;

	/**
	 * 增加
	 */
	@Override
	public PygResult add(Goods goods) {

		try {
			// 保存货品表数据
			// 获取货品对象
			TbGoods tbGoods = goods.getGoods();
			// 保存货品,返回主键
			goodsMapper.insertSelective(tbGoods);
			// 再保存货品描述表
			// 获取货品描述对象
			TbGoodsDesc goodsDesc = goods.getGoodsDesc();
			// 设置外键
			goodsDesc.setGoodsId(tbGoods.getId());
			// 保存
			goodsDescMapper.insertSelective(goodsDesc);
			// 抽取保存方法
			saveItemList(goods.getItemList(), tbGoods, goodsDesc);

			// 保存成功
			return new PygResult(true, "保存成功");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new PygResult(false, "保存失败");
		}

	}

	private void saveItemList(List<TbItem> itemList, TbGoods tbGoods,
			TbGoodsDesc goodsDesc) {
		// TODO Auto-generated method stub
		// 通过货品获取品牌数据
		TbBrand brand = brandMapper.selectByPrimaryKey(tbGoods.getBrandId());

		// 查询分类对象
		TbItemCat itemCat = itemCatMapper.selectByPrimaryKey(tbGoods
				.getCategory3Id());

		// 查询店铺名称
		TbSeller seller = sellerMapper
				.selectByPrimaryKey(tbGoods.getSellerId());

		Map map1 = null;
		// 从商品描述对象中获取图片数据
		String itemImages = goodsDesc.getItemImages();
		// 判断描述对象中图片是否存在
		if (itemImages != null && !"".equals(itemImages)) {
			// [{"color":"白色","url":"http://192.168.25.133/group1/M00/00/00/wKgZhVmNXEWAWuHOAAjlKdWCzvg949.jpg"},{"color":"黑色","url":"http://192.168.25.133/group1/M00/00/00/wKgZhVmNXEuAB_ujAAETwD7A1Is158.jpg"},{"color":"蓝色","url":"http://192.168.25.133/group1/M00/00/00/wKgZhVmNXFWANtjTAAFa4hmtWek619.jpg"}]
			List<Map> images = JSON.parseArray(itemImages, Map.class);
			// 获取第一个对象
			map1 = images.get(0);
		}

		// 判断是否启用规格
		if ("1".equals(tbGoods.getIsEnableSpec())) {
			// 在保存商品sku列表
			// 获取商品sku列表对象
			// 循环sku列表对象
			for (TbItem item : itemList) {

				// 定义商品名称
				// 华为 移动4G,16G
				String skuName = "";

				// 从商品表中获取规格属性选项
				String spec = item.getSpec();
				// 把规格属性转换成map对象
				// 获取spec对象值
				// {spec:{"网络":"电信2G"}
				Map<String, Object> specMap = JSON.parseObject(spec);

				// 循环specmap对象
				for (String key : specMap.keySet()) {
					// 组合商品名称
					skuName += specMap.get(key);

				}
				// 设置货品相关属性值
				item.setGoodsId(tbGoods.getId());
				// 组合商品名称
				item.setTitle(tbGoods.getGoodsName() + skuName);

				item.setImage((String) map1.get("url"));

				// 保存品牌
				item.setBrand(brand.getName());

				//
				item.setCategory(itemCat.getName());
				// 分类id
				item.setCategoryid(itemCat.getId());
				// 设置买家id
				item.setSellerId(tbGoods.getSellerId());

				// 设置买家名称
				item.setSeller(seller.getNickName());

				// 设置时间
				Date date = new Date();
				item.setCreateTime(date);
				item.setUpdateTime(date);

				// 保存
				itemMapper.insertSelective(item);
			}

		} else {

			// 创建商品对象,保存商品数据
			TbItem item = new TbItem();

			item.setGoodsId(tbGoods.getId());
			// 设置商品标题
			item.setTitle(tbGoods.getGoodsName());
			//
			// 保存品牌
			item.setBrand(brand.getName());
			//
			item.setCategory(itemCat.getName());
			// 分类id
			item.setCategoryid(itemCat.getId());
			// 设置买家id
			item.setSellerId(tbGoods.getSellerId());
			// 设置买家名称
			item.setSeller(seller.getNickName());
			// 状态
			// 是否默认
			// 设置时间
			Date date = new Date();
			item.setCreateTime(date);
			item.setUpdateTime(date);

			// 保存
			itemMapper.insertSelective(item);

		}
	}

	/**
	 * 修改
	 */
	@Override
	public void update(Goods goods) {

		// 初始化商品状态
		goods.getGoods().setAuditStatus("0");
		// 修改商品对象
		goodsMapper.updateByPrimaryKey(goods.getGoods());
		// 修改商品描述对象
		goodsDescMapper.updateByPrimaryKey(goods.getGoodsDesc());

		// 先删除商品数据
		// 根据外键删除
		// 创建example对象
		TbItemExample example = new TbItemExample();
		// 创建criteria对象
		com.pyg.pojo.TbItemExample.Criteria createCriteria = example
				.createCriteria();
		// 设置参数,根据外键删除
		createCriteria.andGoodsIdEqualTo(goods.getGoods().getId());

		// 删除操作
		itemMapper.deleteByExample(example);

		// 修改tb_item商品数据
		// 获取商品列表
		List<TbItem> itemList = goods.getItemList();

		saveItemList(itemList, goods.getGoods(), goods.getGoodsDesc());

	}

	/**
	 * 根据ID获取实体
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Goods findOne(Long id) {
		// 创建包装类对象
		Goods goods = new Goods();
		// 查询商品对象
		TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
		// 查询商品描述信息
		TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(tbGoods
				.getId());
		// 把商品对象设置包装类对象
		goods.setGoods(tbGoods);
		goods.setGoodsDesc(tbGoodsDesc);

		// 根据goodsid查询商品数据
		// 创建商品表example对象
		TbItemExample example = new TbItemExample();
		// 创建criteria对象
		com.pyg.pojo.TbItemExample.Criteria createCriteria = example
				.createCriteria();
		// 设置查询参数
		createCriteria.andGoodsIdEqualTo(tbGoods.getId());

		// 执行查询
		List<TbItem> list = itemMapper.selectByExample(example);

		// 把商品集合设置包装类对象
		goods.setItemList(list);

		return goods;
	}

	// 注入消息发送模版
	@Autowired
	private JmsTemplate jmsTemplate;

	/**
	 * 批量删除
	 */
	@Override
	public void delete(String[] ids) {
		for (String id : ids) {
			// 根据id查询商品对象
			TbGoods tbGoods = goodsMapper
					.selectByPrimaryKey(Long.parseLong(id));
			tbGoods.setIsDelete(null);
			// 更新
			goodsMapper.updateByPrimaryKey(tbGoods);

		}
		
		// 发送消息
		jmsTemplate.convertAndSend(ids);
		
		
	}

	@Override
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);

		TbGoodsExample example = new TbGoodsExample();
		Criteria criteria = example.createCriteria();

		if (goods != null) {
			if (goods.getSellerId() != null && goods.getSellerId().length() > 0) {
				criteria.andSellerIdEqualTo(goods.getSellerId());
			}
			if (goods.getGoodsName() != null
					&& goods.getGoodsName().length() > 0) {
				criteria.andGoodsNameLike("%" + goods.getGoodsName() + "%");
			}
			if (goods.getAuditStatus() != null
					&& goods.getAuditStatus().length() > 0) {
				criteria.andAuditStatusLike("%" + goods.getAuditStatus() + "%");
			}
			if (goods.getIsMarketable() != null
					&& goods.getIsMarketable().length() > 0) {
				criteria.andIsMarketableLike("%" + goods.getIsMarketable()
						+ "%");
			}
			if (goods.getCaption() != null && goods.getCaption().length() > 0) {
				criteria.andCaptionLike("%" + goods.getCaption() + "%");
			}
			if (goods.getSmallPic() != null && goods.getSmallPic().length() > 0) {
				criteria.andSmallPicLike("%" + goods.getSmallPic() + "%");
			}
			if (goods.getIsEnableSpec() != null
					&& goods.getIsEnableSpec().length() > 0) {
				criteria.andIsEnableSpecLike("%" + goods.getIsEnableSpec()
						+ "%");
			}
			// 查询不为空
			criteria.andIsDeleteIsNotNull();
		}

		Page<TbGoods> page = (Page<TbGoods>) goodsMapper
				.selectByExample(example);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 需求:更新商品状态.
	 * 
	 * @param ids
	 * @param status
	 * @return 当前商品上架,发送消息,同步索引库.
	 */
	public PygResult updataGoodsStatus(String[] ids, String status) {
		try {
			// 循环数组ids
			for (String id : ids) {
				// 根据id把商品对象查询处理
				TbGoods tbGoods = goodsMapper.selectByPrimaryKey(Long
						.parseLong(id));
				// 修改状态
				tbGoods.setAuditStatus(status);

				// 修改
				goodsMapper.updateByPrimaryKeySelective(tbGoods);
				
			}
			//传递消息 spu id
			jmsTemplate.convertAndSend(ids);
			// 修改成功
			return new PygResult(true, "修改成功");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new PygResult(false, "修改失败");
		}
	}

}
