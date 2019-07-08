package com.pyg.content.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pyg.content.service.ContentService;
import com.pyg.mapper.TbContentMapper;
import com.pyg.pojo.TbContent;
import com.pyg.pojo.TbContentExample;
import com.pyg.pojo.TbContentExample.Criteria;
import com.pyg.utils.PageResult;

/**
 * 服务实现层
 * 
 * @author Administrator
 * 
 */
@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;

	/**
	 * 查询全部
	 */
	@Override
	public List<TbContent> findAll() {
		return contentMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		Page<TbContent> page = (Page<TbContent>) contentMapper
				.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbContent content) {
		//清空缓存
		redisTemplate.boundHashOps("index_cache").delete(content.getCategoryId()+"");
		contentMapper.insert(content);
	}

	/**
	 * 修改
	 * 1,分类id也发生了变化
	 * 2,id没法发生变化
	 */
	@Override
	public void update(TbContent content) {
		//根据当前id查询广告对象
		TbContent tbContent = contentMapper.selectByPrimaryKey(content.getId());		
		//清空缓存
		redisTemplate.boundHashOps("index_cache").delete(tbContent.getCategoryId()+"");
		contentMapper.updateByPrimaryKey(content);
	}

	/**
	 * 根据ID获取实体
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public TbContent findOne(Long id) {
		return contentMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for (Long id : ids) {
			//查询广告内容对象
			TbContent content = contentMapper.selectByPrimaryKey(id);
			redisTemplate.boundHashOps("index_cache").delete(content.getCategoryId()+"");
			contentMapper.deleteByPrimaryKey(id);
		}
	}

	@Override
	public PageResult findPage(TbContent content, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);

		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();

		if (content != null) {
			if (content.getTitle() != null && content.getTitle().length() > 0) {
				criteria.andTitleLike("%" + content.getTitle() + "%");
			}
			if (content.getUrl() != null && content.getUrl().length() > 0) {
				criteria.andUrlLike("%" + content.getUrl() + "%");
			}
			if (content.getPic() != null && content.getPic().length() > 0) {
				criteria.andPicLike("%" + content.getPic() + "%");
			}
			if (content.getContent() != null
					&& content.getContent().length() > 0) {
				criteria.andContentLike("%" + content.getContent() + "%");
			}
			if (content.getStatus() != null && content.getStatus().length() > 0) {
				criteria.andStatusLike("%" + content.getStatus() + "%");
			}

		}

		Page<TbContent> page = (Page<TbContent>) contentMapper
				.selectByExample(example);
		return new PageResult(page.getTotal(), page.getResult());
	}

	//注入redis模版对象
	@Autowired
	private RedisTemplate redisTemplate;
	
	/**
	 * 需求:根据分类id查询不同区域的广告内容信息
	 * 参数:Long categoryId
	 * 业务思想:
	 * 页面广告被分为不同的类型,不同类型的广告通过不同的分类id进行查询.
	 * 大广告分类id:1
	 * 今日推荐分类id:2
	 * 猜你喜欢分类id:3
	 * 返回值:List<TbContent>
	 * 查询业务:
	 * 1,查询有效的广告
	 * 2,广告排序
	 * 广告数据加载添加缓存
	 * 添加缓存原因:
	 * 门户系统并发压力较高,频繁读取数据库的广告数据,对数据库造成很大压力
	 * 为了减轻数据库压力,添加缓存.
	 * 同时也是为了提高查询效率
	 * 缓存服务器: redis服务器
	 * 数据结构:hash数据结构
	 * key:不同导航页缓存 : index_cache  food_cache  
	 * field:categoryId (页面不同区域缓存)
	 * value:缓存数据值
	 * 业务流程:
	 * 1,先查询redis缓存服务器
	 * 2,如果缓存中没有数据,查询数据库,同时把查询的数据放入缓存
	 * 3,如果缓存中有数据,直接返回,不再查询数据库.
	 */
	public List<TbContent> findContentListByCategoryId(Long categoryId) {
		
		
		
		try {
			//先查询缓存
			List<TbContent>  adList = (List<TbContent>) redisTemplate.boundHashOps("index_cache").get(categoryId+"");
			//判断缓存数据是否存在
			if(adList!=null && adList.size()>0){
				return adList;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// 创建广告内容表example对象
		TbContentExample example = new TbContentExample();
		// 创建criteria对象
		Criteria createCriteria = example.createCriteria();
		// 设置查询参数
		// 外键
		createCriteria.andCategoryIdEqualTo(categoryId);
		// 查询有效广告
		createCriteria.andStatusEqualTo("1");
		// 设置排序字段
		example.setOrderByClause("sort_order");
		
		//执行查询
		List<TbContent> list = contentMapper.selectByExample(example);
		
		//添加缓存数据
		redisTemplate.boundHashOps("index_cache").put(categoryId+"",list);
				
		return list;
	}

}
