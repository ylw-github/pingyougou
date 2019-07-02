package com.pyg.manager.service;

import java.util.List;

import com.pyg.pojo.TbBrand;

public interface BrandService {
	
	/**
	 * 需求:查询所有品牌数据
	 */
	public List<TbBrand> findAll();

}
