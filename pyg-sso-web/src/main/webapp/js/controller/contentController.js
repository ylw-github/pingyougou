 //控制层 
app.controller('contentController' ,function($scope,contentService){	
	
	//怎么封装页面广告数据
	//分类:
	//大广告:
	//猜你喜欢
	//今日推荐
	//定义数组存储广告数据
	$scope.adList = [];
	
    //读取列表数据绑定到表单中  
	$scope.findContentListByCategoryId=function(categoryId){
		contentService.findContentListByCategoryId(categoryId).success(
			function(response){
				//封装不同区域广告数据
				$scope.adList[categoryId]=response;
			}			
		);
	};
	
	//从门户系统跳转到搜索系统,实现搜索
	$scope.search = function(){
		//向搜索系统发送请求
		location.href = "http://localhost:8083/search.html#?keywords="+$scope.keywords;
		
	};
	
	
});	
