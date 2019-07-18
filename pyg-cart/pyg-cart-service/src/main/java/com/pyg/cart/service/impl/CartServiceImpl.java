package com.pyg.cart.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.iterators.ArrayListIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.pyg.cart.service.CartService;
import com.pyg.mapper.TbItemMapper;
import com.pyg.pojo.TbItem;
import com.pyg.pojo.TbOrderItem;
import com.pyg.vo.Cart;

@Service
public class CartServiceImpl implements CartService {

	// 注入redis模版对象
	@Autowired
	private RedisTemplate redisTemplate;

	// 注入商品mapper接口代理对象
	@Autowired
	private TbItemMapper itemMapper;

	/**
	 * 需求:查询redis购物车数据
	 * 
	 * @return
	 */
	public List<Cart> findRedisCartList(String username) {
		// 查询redis服务购物车列表
		List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps(
				"cartList").get(username);
		// 判断查询购物车数据是否有值
		if (cartList == null) {
			return new ArrayList<Cart>();
		}
		return cartList;
	}

	/**
	 * 需求:添加购物车数据
	 * 
	 * @param cartList
	 * @param itemId
	 * @param num
	 * @return PygResult
	 */
	public List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId,
			Integer num) {
		// 根据商品id查询商品数据
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		// 判断商品是否存在
		if (item == null) {
			throw new RuntimeException("商品不存在");
		}
		// 判断商品状态是否可用
		if (!item.getStatus().equals("1")) {
			throw new RuntimeException("商品不可用");
		}

		// 获取商家编号
		String sellerId = item.getSellerId();

		// 判断购物车列表中是否存在相同的商家
		Cart cart = this.isSameSeller(cartList, sellerId);

		// 判断是否有相同商家
		if (cart != null) {
			// 有相同商家
			// 获取此商家购物清单
			List<TbOrderItem> orderItemList = cart.getOrderItemList();

			// 判断是否存在相同的商品
			TbOrderItem orderItem = this.isSameItem(orderItemList, itemId);
			// 判断
			if (orderItem != null) {
				// 相同,数量相加
				orderItem.setNum(orderItem.getNum() + num);
				// 计算总价格
				double totalPrice = orderItem.getNum()
						* orderItem.getPrice().doubleValue();
				// 总价格变化
				orderItem.setTotalFee(new BigDecimal(totalPrice));
				// 判断如果商品数量为小于等于0
				if (orderItem.getNum() <= 0) {
					orderItemList.remove(orderItem);
				}
				// 判断
				if (orderItemList.size() == 0) {
					cartList.remove(cart);
				}
				// 放回购物车清单列表
				//orderItemList.add(orderItem);
			} else {
				// 不存在相同商品
				orderItem = new TbOrderItem();
				orderItem.setItemId(item.getId());
				orderItem.setSellerId(item.getSellerId());
				orderItem.setNum(num);
				orderItem.setPicPath(item.getImage());
				orderItem.setPrice(item.getPrice());
				orderItem.setTitle(item.getTitle());
				orderItem.setTotalFee(new BigDecimal(item.getPrice()
						.doubleValue() * num));
				// 把购物清单对象放入集合
				orderItemList.add(orderItem);
			}

			// 放入cart商家
			cart.setOrderItemList(orderItemList);

		} else {
			// 没有相同的商家
			cart = new Cart();
			// 设置商家
			cart.setSellerId(sellerId);
			cart.setSellerName(item.getSeller());
			// 新建购物清单
			List<TbOrderItem> orderItemList1 = this.createOrderItem(item, num);
			// 设置购物清单
			cart.setOrderItemList(orderItemList1);

		}
		// 把商家对象放入购物车集合
		cartList.add(cart);

		return cartList;
	}

	/**
	 * 需求:把商品添加到购物车清单
	 * 
	 * @param item
	 * @return
	 */
	private List<TbOrderItem> createOrderItem(TbItem item, Integer num) {
		// 创建集合对象List<TbOrderItem>,封装购物车数据
		List<TbOrderItem> orderItemList = new ArrayList<TbOrderItem>();
		// 创建对象
		TbOrderItem orderItem = new TbOrderItem();
		orderItem.setItemId(item.getId());
		orderItem.setSellerId(item.getSellerId());
		orderItem.setNum(num);
		orderItem.setPicPath(item.getImage());
		orderItem.setPrice(item.getPrice());
		orderItem.setTitle(item.getTitle());
		orderItem.setTotalFee(new BigDecimal(item.getPrice().doubleValue()
				* num));
		// 把购物清单对象放入集合
		orderItemList.add(orderItem);
		return orderItemList;
	}

	/**
	 * 需求:判断商家中是否存在相同的商品
	 * 
	 * @param orderItemList
	 * @param itemId
	 * @return
	 */
	private TbOrderItem isSameItem(List<TbOrderItem> orderItemList, Long itemId) {
		// 循环商品清单列表
		for (TbOrderItem tbOrderItem : orderItemList) {
			// 如果id相等,说明有相同的商品
			if (tbOrderItem.getItemId() == itemId.longValue()) {
				return tbOrderItem;
			}
		}
		return null;
	}

	/**
	 * 判断是否具有相同商家
	 * 
	 * @param cartList
	 * @param sellerId
	 * @return
	 */
	private Cart isSameSeller(List<Cart> cartList, String sellerId) {
		// 循环判断是否有相同商家
		for (Cart cart : cartList) {
			// 如果商家id相等,说明有相同的商家
			if (cart.getSellerId().equals(sellerId)) {
				return cart;
			}
		}
		return null;
	}

	/**
	 * 把购物车列表保存到redis购物车
	 * 
	 * @param cartList
	 * @param username
	 */
	public void saveCartListToRedisCart(List<Cart> cartList, String username) {
		// TODO Auto-generated method stub
		redisTemplate.boundHashOps("cartList").put(username, cartList);
	}

	/**
	 * 需求:合并购物车数据
	 * 
	 * @param username
	 * @param cartListStr
	 */
	public List<Cart> mergeCart(String username, String cartListStr) {
		// 根据用户名查询所有购物车数据
		List<Cart> cartList1 = (List<Cart>) redisTemplate.boundHashOps("cartList").get(username);
		// 把cookie购物车数据转换集合
		List<Cart> cartList2 = JSON.parseArray(cartListStr, Cart.class);
		
		//循环redis购物车
		for (Cart cart : cartList2) {
			//获取当前商家列表
			List<TbOrderItem> orderItemList = cart.getOrderItemList();
			//循环
			for (TbOrderItem tbOrderItem : orderItemList) {
				//返回合并后redis购物车集合
				cartList1 =	this.addGoodsToCartList(cartList1, tbOrderItem.getItemId(), tbOrderItem.getNum());
				
			}
		}
		
		return cartList1;

	}

}
