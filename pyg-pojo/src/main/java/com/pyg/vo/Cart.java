package com.pyg.vo;

import java.io.Serializable;
import java.util.List;

import com.pyg.pojo.TbOrderItem;

public class Cart implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7180806779366004688L;

	private String sellerId;
	
	private String sellerName;
	
	private List<TbOrderItem> orderItemList;

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public List<TbOrderItem> getOrderItemList() {
		return orderItemList;
	}

	public void setOrderItemList(List<TbOrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}
	
	
	
}
