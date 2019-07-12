//控制层 
app.controller('searchController', function($scope, $location, searchService) {

	// 对searchMap进行初始化工作
	// 封装页面向后台传递所有搜索参数
	$scope.searchMap = {
		"keywords" : "",
		"category" : "",
		"brand" : "",
		"spec" : {},
		"price" : "",
		"sort" : "",
		"sortField" : "",
		"pageNo" : 1,
		"pageSize" : 30
	};
	// 主要用来封装主查询条件
	// 接受参数,向搜索系统发送搜索请求
	$scope.loadkeywords = function() {
		// 接受页面参数
		$scope.searchMap.keywords = $location.search()["keywords"];
		// 调用搜索服务
		searchService.searchList($scope.searchMap).success(function(data) {
			// 回调函数结果
			$scope.searchResult = data;
		});
	};

	// 定义条件搜索方法
	$scope.searchList = function() {
		// 调用service
		searchService.searchList($scope.searchMap).success(function(data) {
			// 回调函数结果
			$scope.searchResult = data;
		});
	};
	
	//定义添加过滤条件函数
	//点击:addFilterCondition('category','手机')
	$scope.addFilterCondition = function(key,value){
		//判断过滤条件类型
		if(key=="category" || key=="brand" || key=="price" ){
			$scope.searchMap[key]=value;
		}else{
			$scope.searchMap.spec[key]=value;
		}
		
		//调用搜索方法
		$scope.searchList();
		
	}
	
	//撤销查询选项
	$scope.removeSearchItem = function(key){
		//判断
		if(key=="category" || key=="brand" || key=="price"){
			$scope.searchMap[key]="";
		}else{
			delete $scope.searchMap.spec[key];
		}
		//调用搜索方法
		$scope.searchList();
		
	};
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

});
