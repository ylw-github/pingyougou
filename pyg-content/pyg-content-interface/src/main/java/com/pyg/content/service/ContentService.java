package com.pyg.content.service;
import java.util.List;
import com.pyg.pojo.TbContent;

import com.pyg.utils.PageResult;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface ContentService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbContent> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum,int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbContent content);
	
	
	/**
	 * 修改
	 */
	public void update(TbContent content);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbContent findOne(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long [] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbContent content, int pageNum,int pageSize);
	/**
	 * 需求:根据分类id查询不同区域的广告内容信息
	 * 参数:Long categoryId
	 * 业务思想:
	 * 页面广告被分为不同的类型,不同类型的广告通过不同的分类id进行查询.
	 * 大广告分类id:1
	 * 今日推荐分类id:2
	 * 猜你喜欢分类id:3
	 * 返回值:List<TbContent>
	 */
	public List<TbContent> findContentListByCategoryId(Long categoryId);
}
