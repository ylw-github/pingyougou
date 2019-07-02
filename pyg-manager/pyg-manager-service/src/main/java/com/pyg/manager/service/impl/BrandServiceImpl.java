package com.pyg.manager.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.pyg.manager.service.BrandService;
import com.pyg.mapper.TbBrandMapper;
import com.pyg.pojo.TbBrand;
import com.pyg.pojo.TbBrandExample;
@Service
public class BrandServiceImpl implements BrandService{
	
	//注入mapper接口代理对象
	@Autowired
	private TbBrandMapper brandMapper;

	@Override
	public List<TbBrand> findAll() {
		//创建example对象
		TbBrandExample example = new TbBrandExample();
		// 查询
		List<TbBrand> list = brandMapper.selectByExample(example);
		return list;
	}

}
