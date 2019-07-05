package com.pyg.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.pyg.pojo.TbGoods;
import com.pyg.pojo.TbGoodsDesc;
import com.pyg.pojo.TbItem;

public class Goods implements Serializable{
	
	//保存货品
	private TbGoods goods;
	
	//货品描述
	private TbGoodsDesc goodsDesc;
	
	//[{spec:{"网络":"电信2G"},price:999999,stockCount:0,status:'0',idDefault:'0'},{spec:{"网络":"联通2G"},price:999999,stockCount:0,status:'0',idDefault:'0'}]
	//商品列表
	private List<TbItem> itemList;


	public TbGoods getGoods() {
		return goods;
	}


	public void setGoods(TbGoods goods) {
		this.goods = goods;
	}


	public TbGoodsDesc getGoodsDesc() {
		return goodsDesc;
	}


	public void setGoodsDesc(TbGoodsDesc goodsDesc) {
		this.goodsDesc = goodsDesc;
	}


	public List<TbItem> getItemList() {
		return itemList;
	}


	public void setItemList(List<TbItem> itemList) {
		this.itemList = itemList;
	}


	
	
	
	

}
