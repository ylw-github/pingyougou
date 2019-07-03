//服务层
app.service('typeTemplateService',function($http){
	    	
	//读取列表数据绑定到表单中
	this.findAll=function(){
		return $http.get('../typeTemplate/findAll');		
	}
	//分页 
	this.findPage=function(page,rows){
		return $http.get('../typeTemplate/findPage?page='+page+'&rows='+rows);
	}
	//查询实体
	this.findOne=function(id){
		return $http.get('../typeTemplate/findOne?id='+id);
	}
	//增加 
	this.add=function(entity){
		return  $http.post('../typeTemplate/add',entity );
	}
	//修改 
	this.update=function(entity){
		return  $http.post('../typeTemplate/update',entity );
	}
	//删除
	this.dele=function(ids){
		return $http.get('../typeTemplate/delete?ids='+ids);
	}
	//搜索
	this.search=function(page,rows,searchEntity){
		return $http.post('../typeTemplate/search?page='+page+"&rows="+rows, searchEntity);
	}  
	//查询品牌
	this.findBrandList = function(){
		return $http.get("../typeTemplate/findBrandList");
	}
	// 查询规格属性下拉列表
	this.findSpecOptionList = function(){
		return $http.get("../typeTemplate/findSpecOptionList");
	};
	
});
