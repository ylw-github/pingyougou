//服务层
app.service('userService',function($http){
	    	
	//读取列表数据绑定到表单中
	this.findAll=function(){
		return $http.get('../user/findAll');		
	}
	//分页 
	this.findPage=function(page,rows){
		return $http.get('../user/findPage?page='+page+'&rows='+rows);
	}
	//查询实体
	this.findOne=function(id){
		return $http.get('../user/findOne?id='+id);
	}
	//增加 
	this.add=function(entity,smsCode){
		return  $http.post('../user/add?smsCode='+smsCode,entity );
	}
	//发送短信
	this.smsCode=function(smsCode){
		return  $http.get('../user/smsCode?phone='+smsCode);
	}
	
	
	//修改 
	this.update=function(entity){
		return  $http.post('../user/update',entity );
	}
	//删除
	this.dele=function(ids){
		return $http.get('../user/delete?ids='+ids);
	}
	//搜索
	this.search=function(page,rows,searchEntity){
		return $http.post('../user/search?page='+page+"&rows="+rows, searchEntity);
	}    	
});
