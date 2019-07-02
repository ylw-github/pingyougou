package com.pyg.manager.controller;
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

	@Reference
	private ItemCatService itemCatService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbItemCat> findAll(){			
		return itemCatService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return itemCatService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param itemCat
	 * @return
	 */
	@RequestMapping("/add")
	public PygResult add(@RequestBody TbItemCat itemCat){
		try {
			itemCatService.add(itemCat);
			return new PygResult(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new PygResult(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param itemCat
	 * @return
	 */
	@RequestMapping("/update")
	public PygResult update(@RequestBody TbItemCat itemCat){
		try {
			itemCatService.update(itemCat);
			return new PygResult(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new PygResult(false, "修改失败");
		}
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
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public PygResult delete(Long [] ids){
		try {
			itemCatService.delete(ids);
			return new PygResult(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new PygResult(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param brand
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbItemCat itemCat, int page, int rows  ){
		return itemCatService.findPage(itemCat, page, rows);		
	}
	
}
