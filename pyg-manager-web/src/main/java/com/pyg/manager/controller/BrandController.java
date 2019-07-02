package com.pyg.manager.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.manager.service.BrandService;
import com.pyg.pojo.TbBrand;
import com.pyg.utils.PageResult;
import com.pyg.utils.PygResult;

@RestController
@RequestMapping("/brand")
public class BrandController {
	
	//注入远程服务对象
	@Reference(timeout=10000000,retries=2)
	private BrandService brandService;
	
	/**
	 * 需求:查询所有品牌数据
	 */
	@RequestMapping("/findAll")
	public List<TbBrand> findAll(){
		//调用远程服务对象方法
		List<TbBrand> list = brandService.findAll();
		
		return list;
	}
	
	/**
	 * 需求:分页展示品牌列表
	 * 参数:Integer page,Integer rows
	 * 返回值:分页包装类对象PageResult
	 */
	@RequestMapping("/findPage")
	public PageResult findPage(Integer page,Integer rows){
		//调用远程服务对象
		PageResult result = brandService.findPage(page, rows);
		return result;
	}
	
	/**
	 * 需求:添加品牌数据
	 * 参数:TbBrand brand
	 * 返回值:PygResult
	 * 
	 */
	@RequestMapping("/add")
	public PygResult add(@RequestBody TbBrand brand){
		//调用远程服务
		PygResult result = brandService.add(brand);
		return result;
	}
	
	/**
	 * 需求:根据id查询品牌数据
	 * 请求:../brand/findOne?id=?
	 * 参数:Long id
	 * 返回值:TbBrand
	 */
	@RequestMapping("/findOne")
	public TbBrand findOne(Long id){
		//调用远程服务
		TbBrand brand = brandService.findOne(id);
		return brand;
	}
	
	/**
	 * 需求:更新品牌数据
	 * 请求:/brand/update
	 * 参数:TbBrand brand
	 * 返回值:PygResult
	 */
	@RequestMapping("/update")
	public PygResult update(@RequestBody TbBrand brand){
		//调用远程服务
		PygResult result = brandService.update(brand);
		return result;
		
	}
	
	/**
	 * 需求:品牌条件查询
	 * 参数:TbBrand brand
	 * 返回值:PygResult
	 */
	@RequestMapping("search")
	public PageResult search(@RequestBody TbBrand brand,
			@RequestParam(defaultValue="1") Integer page,
			@RequestParam(defaultValue="10") Integer rows){
		//调用服务层条件查询方法
		PageResult result = brandService.findBrandByPage(brand, page, rows);
		return result;
	}

	
	/**
	 * 需求: 批量删除品牌数据
	 * 请求: dele?ids=??
	 * 参数: Long[] id
	 * 返回值:PygResult
	 */
	@RequestMapping("/dele")
	public PygResult dele(Long[] ids){
		//调用远程service方法
		PygResult result = brandService.deleteIds(ids);
		return result;
	}
}
