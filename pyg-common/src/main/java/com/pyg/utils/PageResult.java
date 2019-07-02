package com.pyg.utils;

import java.io.Serializable;
import java.util.List;

public class PageResult implements Serializable{
	
	//mybatis分页插件 pagehlper 插件
	//实现方式:
	//sql语句:
	//1,limit
	//2,count
	//分页插件:
	//配置插件: sqlMapConfig
	//导入插件jar文件
	
	
	//总记录数
	private Long total;
	
	public PageResult(Long total, List<?> rows) {
		this.total = total;
		this.rows = rows;
	}

	//总记录
	private List<?> rows;

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public List<?> getRows() {
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}
	
	

}
