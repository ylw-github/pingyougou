package com.pyg.cart.service;

import java.util.List;

import com.pyg.utils.PygResult;
import com.pyg.vo.Cart;

public interface CartService {
	/**
	 * 需求:查询redis购物车数据
	 * @return
	 */
	List<Cart> findRedisCartList(String username);
	/**
	 * 需求:添加购物车数据
	 * @param cartList
	 * @param itemId
	 * @param num
	 * @return PygResult
	 */
	List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num);
	/**
	 * 把购物车列表保存到redis购物车
	 * @param cartList
	 * @param username
	 */
	void saveCartListToRedisCart(List<Cart> cartList, String username);
	/**
	 * 需求:合并购物车数据
	 * @param username
	 * @param cartListStr
	 */
	List<Cart> mergeCart(String username, String cartListStr);

}
