package com.pyg.search.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.search.service.ItemSearchService;

@RestController
public class ItemSearchController {
	
	//注入搜索服务对象
	@Reference(timeout=1000000000)
	private ItemSearchService itemSearchService;
	
	
	/**
	 * 需求:根据前台关键词,以及过滤条件查询索引库
	 * 参数:Map<String,Object>
	 * 返回值:Map
	 */
	@RequestMapping("/searchs")
	public Map<String, Object> searchList(@RequestBody Map<String, Object> searchMap){
		
		//调用远程搜索服务
		Map<String, Object> maps = itemSearchService.searchList(searchMap);
		
		return maps;
	}
	

}
