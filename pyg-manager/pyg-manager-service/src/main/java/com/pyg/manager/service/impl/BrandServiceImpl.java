package com.pyg.manager.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pyg.manager.service.BrandService;
import com.pyg.mapper.TbBrandMapper;
import com.pyg.pojo.TbBrand;
import com.pyg.pojo.TbBrandExample;
import com.pyg.pojo.TbBrandExample.Criteria;
import com.pyg.utils.PageResult;
import com.pyg.utils.PygResult;
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

	/**
	 * 需求:分页展示品牌列表
	 * 参数:Integer page,Integer rows
	 * 返回值:分页包装类对象PageResult
	 */
	public PageResult findPage(Integer page, Integer rows) {
		//创建example对象
		TbBrandExample example = new TbBrandExample();
		// 使用插件设置分页
		PageHelper.startPage(page, rows);
		//查询品牌数据
		//List={page[total,pagesie],list}
		List<TbBrand> list = brandMapper.selectByExample(example);
		//创建pageINfo对象,获取查询分页数据
		PageInfo<TbBrand> pageInfo = new PageInfo<TbBrand>(list);
		
		return new PageResult(pageInfo.getTotal(), list);
	}

	/**
	 * 需求:添加品牌数据
	 * 参数:TbBrand brand
	 * 返回值:PygResult
	 * 
	 */
	public PygResult add(TbBrand brand) {
		try {
			//保存品牌数据
			brandMapper.insertSelective(brand);
			//保存成功
			return new PygResult(true, "保存成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//保存成功
			return new PygResult(false, "保存失败");
		}
	}

	/**
	 * 需求:根据id查询品牌数据
	 * 参数:Long id
	 * 返回值:TbBrand
	 */
	public TbBrand findOne(Long id) {
		//根据id查询品牌数据
		TbBrand brand = brandMapper.selectByPrimaryKey(id);
		return brand;
	}

	/**
	 * 需求:更新品牌数据
	 * 参数:TbBrand brand
	 * 返回值:PygResult
	 */
	public PygResult update(TbBrand brand) {
		// TODO Auto-generated method stub
		try {
			brandMapper.updateByPrimaryKeySelective(brand);
			//保存成功
			return new PygResult(true, "修改成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//保存成功
			return new PygResult(false, "修改失败");
		}
	}

	/**
	 * 需求:品牌条件查询
	 * 参数:TbBrand brand
	 * 返回值:PygResult
	 */
	public PageResult findBrandByPage(TbBrand brand, Integer page, Integer rows) {
		// 创建example对象
		TbBrandExample example = new TbBrandExample();
		//创建criteria对象
		Criteria createCriteria = example.createCriteria();
		//设置参数
		//判断参数是否存在
		if(brand.getName()!=null && !"".equals(brand.getName())){
			//模糊查询
			createCriteria.andNameLike("%"+brand.getName()+"%");
		}
		if(brand.getFirstChar()!=null && !"".equals(brand.getFirstChar())){
			createCriteria.andFirstCharEqualTo(brand.getFirstChar());
		}
		
		//查询之前,必须设置分页
		PageHelper.startPage(page,rows);
		
		//执行查询
		List<TbBrand> list = brandMapper.selectByExample(example);
		//创建PageINfo,获取分页数据
		PageInfo<TbBrand> pageInfo = new PageInfo<TbBrand>(list);
		//返回分页包装对象
		return new PageResult(pageInfo.getTotal(), list);
	}

	/**
	 * 需求: 批量删除品牌数据
	 * 参数: Long[] id
	 * 返回值:PygResult
	 */
	public PygResult deleteIds(Long[] ids) {
		try {
			// 循环需要删除id
			for (Long id : ids) {
				//单独删除一个对象
				brandMapper.deleteByPrimaryKey(id);
				
			}
			//删除成功
			return new PygResult(true, "删除成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//删除失败
			return new PygResult(false, "删除失败");
		}
	}

	/**
	 * 需求:查询所有品牌
	 * 参数:无
	 * 返回值:List<Map>
	 */
	public List<Map> findBrandWithTemplate() {
		// 查询所有品牌
		List<Map> list = brandMapper.findBrandWithTemplate();
		
		return list;
	}

}
