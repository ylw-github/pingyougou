package com.pyg.manager.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pyg.mapper.TbSpecificationOptionMapper;
import com.pyg.mapper.TbTypeTemplateMapper;
import com.pyg.pojo.TbSpecificationOption;
import com.pyg.pojo.TbSpecificationOptionExample;
import com.pyg.pojo.TbTypeTemplate;
import com.pyg.pojo.TbTypeTemplateExample;
import com.pyg.pojo.TbTypeTemplateExample.Criteria;
import com.pyg.manager.service.TypeTemplateService;
import com.pyg.utils.PageResult;

/**
 * 服务实现层
 * 
 * @author Administrator
 * 
 */
@Service
public class TypeTemplateServiceImpl implements TypeTemplateService {

	@Autowired
	private TbTypeTemplateMapper typeTemplateMapper;

	/**
	 * 查询全部
	 */
	@Override
	public List<TbTypeTemplate> findAll() {
		return typeTemplateMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		Page<TbTypeTemplate> page = (Page<TbTypeTemplate>) typeTemplateMapper
				.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbTypeTemplate typeTemplate) {
		typeTemplateMapper.insert(typeTemplate);
	}

	/**
	 * 修改
	 */
	@Override
	public void update(TbTypeTemplate typeTemplate) {
		typeTemplateMapper.updateByPrimaryKey(typeTemplate);
	}

	/**
	 * 根据ID获取实体
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public TbTypeTemplate findOne(Long id) {
		return typeTemplateMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for (Long id : ids) {
			typeTemplateMapper.deleteByPrimaryKey(id);
		}
	}

	@Override
	public PageResult findPage(TbTypeTemplate typeTemplate, int pageNum,
			int pageSize) {
		PageHelper.startPage(pageNum, pageSize);

		TbTypeTemplateExample example = new TbTypeTemplateExample();
		Criteria criteria = example.createCriteria();

		if (typeTemplate != null) {
			if (typeTemplate.getName() != null
					&& typeTemplate.getName().length() > 0) {
				criteria.andNameLike("%" + typeTemplate.getName() + "%");
			}
			if (typeTemplate.getSpecIds() != null
					&& typeTemplate.getSpecIds().length() > 0) {
				criteria.andSpecIdsLike("%" + typeTemplate.getSpecIds() + "%");
			}
			if (typeTemplate.getBrandIds() != null
					&& typeTemplate.getBrandIds().length() > 0) {
				criteria.andBrandIdsLike("%" + typeTemplate.getBrandIds() + "%");
			}
			if (typeTemplate.getCustomAttributeItems() != null
					&& typeTemplate.getCustomAttributeItems().length() > 0) {
				criteria.andCustomAttributeItemsLike("%"
						+ typeTemplate.getCustomAttributeItems() + "%");
			}

		}

		Page<TbTypeTemplate> page = (Page<TbTypeTemplate>) typeTemplateMapper
				.selectByExample(example);
		return new PageResult(page.getTotal(), page.getResult());
	}
	
	//注入规格选项mapper接口代理对象
	@Autowired
	private TbSpecificationOptionMapper specificationOptionMapper;

	/**
	 * 需求: 查询模版中存储关键规格属性对应规格选项
	 * 请求: findSpecOptionListByTypeId
	 * 参数:模版id
	 * 返回值: List<Map>
	 */
	public List<Map> findSpecOptionListByTypeId(Long typeId) {
		// 根据模版id查询规格属性值
		TbTypeTemplate typeTemplate = typeTemplateMapper.selectByPrimaryKey(typeId);
		// 从模版中获取规格属性值
		String specIds = typeTemplate.getSpecIds();
		
		// [{"id":27,"text":"网络"},{"id":32,"text":"机身内存"}]
		// 把规格属性转换为json对象
		List<Map> specList = JSON.parseArray(specIds, Map.class);
		// 循环规格属性值,根据规格属性id查询规格选项
		for (Map map : specList) {
			//从规格属性中获取规格id
			Integer specId = (Integer)map.get("id");
			
			
			//创建example对象
			TbSpecificationOptionExample example = new TbSpecificationOptionExample();
			//创建criteria对象,设置查询参数
			com.pyg.pojo.TbSpecificationOptionExample.Criteria createCriteria = example.createCriteria();
			//设置外键规格id查询规格选项
			createCriteria.andSpecIdEqualTo(specId.longValue());
			//查询规格选项值(根据外键查询)
			List<TbSpecificationOption> specOptionList = specificationOptionMapper.selectByExample(example);
			
			//[{"id":27,"text":"网络","options":[{}{}]},{"id":32,"text":"机身内存","options":[{}{}]}]
			//把规格选项封装到map
			map.put("options", specOptionList);
			
		}
		return specList;
	}

}
