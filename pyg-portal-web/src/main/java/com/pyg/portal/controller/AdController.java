package com.pyg.portal.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.content.service.ContentService;
import com.pyg.pojo.TbContent;

@RestController
@RequestMapping("/ad")
public class AdController {
	
	//注入广告服务对象
	@Reference(timeout=10000000)
	private ContentService contentService;

	/**
	 * 需求:根据分类id查询不同区域的广告内容信息
	 * 参数:Long categoryId
	 * 业务思想:
	 * 页面广告被分为不同的类型,不同类型的广告通过不同的分类id进行查询.
	 * 大广告分类id:1
	 * 今日推荐分类id:2
	 * 猜你喜欢分类id:3
	 * 返回值:List<TbContent>
	 * 查询业务:
	 * 1,查询有效的广告
	 * 2,广告排序
	 */
	@RequestMapping("/findContentListByCategoryId")
	public List<TbContent> findContentListByCategoryId(Long categoryId){
		//调用远程服务方法
		List<TbContent> list = contentService.findContentListByCategoryId(categoryId);
		return list;
		
	}
}
