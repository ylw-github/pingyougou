package com.pyg.search.service;

import java.util.Map;

public interface ItemSearchService {
	/**
	 * 需求:根据前台页面传递搜索关键字进行搜索
	 * @param searchMap
	 * @return
	 */
	public Map<String, Object> searchList(Map<String, Object> searchMap);

}
