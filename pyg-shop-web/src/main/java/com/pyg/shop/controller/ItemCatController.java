package com.pyg.shop.controller;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.pojo.TbItemCat;
import com.pyg.manager.service.ItemCatService;

import com.pyg.utils.PageResult;
import com.pyg.utils.PygResult;
/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/itemCat")
public class ItemCatController {

	@Reference(timeout=10000000)
	private ItemCatService itemCatService;
	
	
	@RequestMapping("/findAll")
	public List<TbItemCat> findAll(){
		return itemCatService.findAll();
	}
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public TbItemCat findOne(Long id){
		return itemCatService.findOne(id);		
	}
	
	

	
	/**
	 * 需求:根据父id查询子节点
	 * 请求:/itemCat/findItemCatByParentId?parentId=
	 * 参数:Long parentId
	 * 返回值:List<tbItemCat>
	 */
	@RequestMapping("/findItemCatByParentId")
	public List<TbItemCat> findItemCatByParentId(Long parentId){
		//调用远程service服务对象
		List<TbItemCat> list = itemCatService.findItemCatByParentId(parentId);
		return list;
	}
	
}
