package com.pyg.cart.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pyg.cart.service.CartService;
import com.pyg.utils.CookieUtil;
import com.pyg.utils.PygResult;
import com.pyg.vo.Cart;

@RestController
@RequestMapping("/cart")
public class CartController {

	// 注入购物车服务对象
	@Reference(timeout=10000000)
	private CartService cartService;

	/**
	 * 需求:添加购物车
	 * 请求:/cart/addGoodsToCartList?itemId='+$scope.sku.id+'&num='+$scope.num
	 * 参数:Long itemId,Integer num 返回值:PygResult 业务: 1,查询购物车列表
	 * 1.1,如果用户此时未登录,查询cookie购物车列表 1.2,如果用户此时登录,查询redis购物车 2,判断购物车列表是否存在
	 * 2.1,如果不存在,创建购物车详情清单 2.2,如果购物车列表存在 2.2.1,判断是否存在相同的商品(添加的此商品是否属于当前商家)
	 * 2.2.1.1,属于,把商品添加到此商家购物车列表中--> 判断是否存在相同的商品,如果有,数量相加,否则直接添加
	 * 2.2.1.2,不属于商家商品,构造商品,添加即可
	 */
	@RequestMapping("addGoodsToCartList")
	@CrossOrigin(origins="http://item.pinyougou.com",allowCredentials="true")
	public PygResult addGoodsToCartList(String itemId, Integer num,
			HttpServletRequest request, HttpServletResponse response) {

		try {
			
			itemId = itemId.replace(",", "");
			
			// 获取用户登录身份信息
			String username = SecurityContextHolder.getContext()
					.getAuthentication().getName();

			// 查询购物车列表
			List<Cart> cartList = this.findCartList(request);

			// 添加购物车
			cartList = cartService.addGoodsToCartList(cartList, Long.parseLong(itemId), num);

			// 判断用户是否处于登录状态
			if (username.equals("anonymousUser")) {
				// 未登录
				CookieUtil.setCookie(request, response, "cartList",
						JSON.toJSONString(cartList), 46800, true);

			} else {
				// 登录
				cartService.saveCartListToRedisCart(cartList, username);
			}

			return new PygResult(true, "购物车添加成功");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new PygResult(false, "购物车添加失败");
		}

	}

	/**
	 * 需求:查询购物车列表
	 * 
	 * @return
	 */
	@RequestMapping("/findCartList")
	public List<Cart> findCartList(HttpServletRequest request) {
		// 判断用户此时是否处于登录状态
		// 获取用户名
		String username = SecurityContextHolder.getContext()
				.getAuthentication().getName();
		
		//定义集合
		List<Cart> cartList = null;
		// 查询cookie购物车数据
		String cartListStr = CookieUtil.getCookieValue(request, "cartList", true);
		// 登录
		if (!username.equals("anonymousUser")) {
			//判断cookie购物车不为空,合并
			if(StringUtils.isNotBlank(cartListStr)){
				//合并购物车
				cartList = 	cartService.mergeCart(username,cartListStr);
				
			}
			//登录
		   cartList = cartService.findRedisCartList(username);		
		   
		   return cartList;
			
		}	
		
		//未登录
		// 判断集合是否存在值
		if (StringUtils.isBlank(cartListStr)) {
			cartListStr = "[]";
		}
		// 把购物车字符串转换成对象
		cartList = JSON.parseArray(cartListStr, Cart.class);	
		
		return cartList;
	}

}
