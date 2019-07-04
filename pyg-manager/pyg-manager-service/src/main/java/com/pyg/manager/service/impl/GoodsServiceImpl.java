package com.pyg.manager.service.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pyg.manager.service.GoodsService;
import com.pyg.mapper.TbGoodsDescMapper;
import com.pyg.mapper.TbGoodsMapper;
import com.pyg.pojo.TbGoods;
import com.pyg.pojo.TbGoodsDesc;
import com.pyg.pojo.TbGoodsExample;
import com.pyg.pojo.TbGoodsExample.Criteria;
import com.pyg.utils.PageResult;
import com.pyg.utils.PygResult;
import com.pyg.vo.Goods;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
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
		Page<TbGoods> page=   (Page<TbGoods>) goodsMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	
	//注入货品描述mapper接口代理对象
	@Autowired
	private TbGoodsDescMapper goodsDescMapper;
	
	/**
	 * 增加
	 */
	@Override
	public PygResult add(Goods goods) {
		
		try {
			//保存货品表数据
			//获取货品对象
			TbGoods tbGoods = goods.getGoods();
			//保存货品,返回主键
			goodsMapper.insertSelective(tbGoods);		
			//再保存货品描述表
			//获取货品描述对象
			TbGoodsDesc goodsDesc = goods.getGoodsDesc();
			//设置外键
			goodsDesc.setGoodsId(tbGoods.getId());
			//保存
			goodsDescMapper.insertSelective(goodsDesc);
			//在保存商品表
			
			//保存成功
			return new PygResult(true, "保存成功");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new PygResult(false,"保存失败");
		}
		
		
	
		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbGoods goods){
		goodsMapper.updateByPrimaryKey(goods);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbGoods findOne(Long id){
		return goodsMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			goodsMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbGoodsExample example=new TbGoodsExample();
		Criteria criteria = example.createCriteria();
		
		if(goods!=null){			
						if(goods.getSellerId()!=null && goods.getSellerId().length()>0){
				criteria.andSellerIdLike("%"+goods.getSellerId()+"%");
			}
			if(goods.getGoodsName()!=null && goods.getGoodsName().length()>0){
				criteria.andGoodsNameLike("%"+goods.getGoodsName()+"%");
			}
			if(goods.getAuditStatus()!=null && goods.getAuditStatus().length()>0){
				criteria.andAuditStatusLike("%"+goods.getAuditStatus()+"%");
			}
			if(goods.getIsMarketable()!=null && goods.getIsMarketable().length()>0){
				criteria.andIsMarketableLike("%"+goods.getIsMarketable()+"%");
			}
			if(goods.getCaption()!=null && goods.getCaption().length()>0){
				criteria.andCaptionLike("%"+goods.getCaption()+"%");
			}
			if(goods.getSmallPic()!=null && goods.getSmallPic().length()>0){
				criteria.andSmallPicLike("%"+goods.getSmallPic()+"%");
			}
			if(goods.getIsEnableSpec()!=null && goods.getIsEnableSpec().length()>0){
				criteria.andIsEnableSpecLike("%"+goods.getIsEnableSpec()+"%");
			}
			if(goods.getIsDelete()!=null && goods.getIsDelete().length()>0){
				criteria.andIsDeleteLike("%"+goods.getIsDelete()+"%");
			}
	
		}
		
		Page<TbGoods> page= (Page<TbGoods>)goodsMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}
	
}
