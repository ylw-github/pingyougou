package com.pyg.manager.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pyg.mapper.TbSpecificationMapper;
import com.pyg.mapper.TbSpecificationOptionMapper;
import com.pyg.pojo.TbSpecification;
import com.pyg.pojo.TbSpecificationExample;
import com.pyg.pojo.TbSpecificationExample.Criteria;
import com.pyg.pojo.TbSpecificationOption;
import com.pyg.pojo.TbSpecificationOptionExample;
import com.pyg.manager.service.SpecificationService;
import com.pyg.utils.PageResult;
import com.pyg.vo.Specification;

/**
 * 服务实现层
 * 
 * @author Administrator
 * 
 */
@Service
public class SpecificationServiceImpl implements SpecificationService {

	@Autowired
	private TbSpecificationMapper specificationMapper;

	// 注入规格选项mapper接口代理对象
	@Autowired
	private TbSpecificationOptionMapper specificationOptionMapper;

	/**
	 * 查询全部
	 */
	@Override
	public List<TbSpecification> findAll() {
		return specificationMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		Page<TbSpecification> page = (Page<TbSpecification>) specificationMapper
				.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(Specification specification) {
		// 保存规格
		// 获取规格对象
		TbSpecification tbSpecification = specification.getTbSpecification();
		// 添加,返回主键值,保存规格选项
		specificationMapper.insert(tbSpecification);
		// 获取规格选项集合
		List<TbSpecificationOption> specList = specification
				.getSpecificationList();
		// 遍历规格集合,实现保存
		for (TbSpecificationOption tbSpecificationOption : specList) {
			// 设置外键
			tbSpecificationOption.setSpecId(tbSpecification.getId());
			// 保存
			specificationOptionMapper.insertSelective(tbSpecificationOption);
		}
	}

	/**
	 * 修改
	 */
	@Override
	public void update(Specification specification) {
		// 修改规格表数据
		// 获取规格对象
		TbSpecification tbSpecification = specification.getTbSpecification();
		// 更新
		specificationMapper.updateByPrimaryKeySelective(tbSpecification);

		// 更新规格选项数据
		// 获取规格选项对象
		List<TbSpecificationOption> list = specification.getSpecificationList();

		// 根据外键删除规格选项值
		// 创建example对象
		TbSpecificationOptionExample example = new TbSpecificationOptionExample();
		// 创建criteria对象
		com.pyg.pojo.TbSpecificationOptionExample.Criteria createCriteria = example
				.createCriteria();
		// 设置查询参数
		createCriteria.andSpecIdEqualTo(tbSpecification.getId());

		// 执行删除操作
		specificationOptionMapper.deleteByExample(example);

		// 循环规格选项集合
		for (TbSpecificationOption tbSpecificationOption : list) {
			//设置外键
			tbSpecificationOption.setSpecId(tbSpecification.getId());
			// 再插入规格数据
			specificationOptionMapper.insertSelective(tbSpecificationOption);
		}

	}

	/**
	 * 根据ID获取实体
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Specification findOne(Long id) {
		// 创建一个结果集包装类对象
		Specification specification = new Specification();
		// 查询规格数据
		TbSpecification tbSpecification = specificationMapper
				.selectByPrimaryKey(id);
		// 设置规格对象
		specification.setTbSpecification(tbSpecification);

		// 查询规格选项
		// 根据外键查询
		// 创建example对象
		TbSpecificationOptionExample example = new TbSpecificationOptionExample();
		// 创建criteria对象
		com.pyg.pojo.TbSpecificationOptionExample.Criteria createCriteria = example
				.createCriteria();
		// 设置查询参数
		createCriteria.andSpecIdEqualTo(id);
		// 执行查询
		List<TbSpecificationOption> list = specificationOptionMapper
				.selectByExample(example);
		// 把结果集合添加到包装对象
		specification.setSpecificationList(list);

		return specification;
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for (Long id : ids) {
			//先删除规格数据			
			specificationMapper.deleteByPrimaryKey(id);
			//在删除规格选项数据
			// 创建example对象
			TbSpecificationOptionExample example = new TbSpecificationOptionExample();
			// 创建criteria对象
			com.pyg.pojo.TbSpecificationOptionExample.Criteria createCriteria = example
					.createCriteria();
			// 设置查询参数
			createCriteria.andSpecIdEqualTo(id);
			//根据外键删除
			specificationOptionMapper.deleteByExample(example);
		}
	}

	@Override
	public PageResult findPage(TbSpecification specification, int pageNum,
			int pageSize) {
		PageHelper.startPage(pageNum, pageSize);

		TbSpecificationExample example = new TbSpecificationExample();
		Criteria criteria = example.createCriteria();

		if (specification != null) {
			if (specification.getSpecName() != null
					&& specification.getSpecName().length() > 0) {
				criteria.andSpecNameLike("%" + specification.getSpecName()
						+ "%");
			}

		}

		Page<TbSpecification> page = (Page<TbSpecification>) specificationMapper
				.selectByExample(example);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 需求:查询规格属性值,加载下拉列表
	 * 参数:无
	 * 返回值:List<Map>
	 * 方法:findSpecOptionList();
	 */
	public List<Map> findSpecOptionList() {
		// 调用mapper接口方法即可
		List<Map> list = specificationMapper.findSpecOptionList();
		return list;
	}

}
