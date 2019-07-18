package com.pyg.order.service;
import java.util.List;

import com.pyg.pojo.TbOrder;
import com.pyg.pojo.TbPayLog;
import com.pyg.utils.PageResult;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface OrderService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbOrder> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum,int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbOrder order);
	
	
	/**
	 * 修改
	 */
	public void update(TbOrder order);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbOrder findOne(Long id);
	
	
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
	public PageResult findPage(TbOrder order, int pageNum,int pageSize);
	
	/**
	 * 从缓存中提取日志对象
	 * @param userId
	 * @return
	 */
	public TbPayLog searchPayLogFromRedis(String userId);
	
	/**
	 * 修改订单状态 
	 * @param out_trade_no
	 * @param transaction_id
	 * @return
	 */
	public void updateOrderStatus(String out_trade_no,String transaction_id);
	
}
