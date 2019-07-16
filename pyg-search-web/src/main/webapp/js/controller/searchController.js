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
		"sort" : "ASC",
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
			// 调用函数
			buildPageLabel();
		});
	};

	// 定义条件搜索方法
	$scope.searchList = function() {
		// 调用service
		searchService.searchList($scope.searchMap).success(function(data) {
			// 回调函数结果
			$scope.searchResult = data;
			// 调用函数
			buildPageLabel();
		});
	};

	// 定义函数
	buildPageLabel = function() {
		// 新增分页栏属性
		$scope.pageLabel=[];
		// 得到最后页码
		var maxPageNo= $scope.searchResult.totalPages;
		// 开始页码
		var firstPage=1;
		// 截止页码
		var lastPage=maxPageNo;
		// 如果总页数大于 5 页,显示部分页码
		if($scope.searchResult.totalPages> 5){ 
			// 如果当前页小于等于 3
			if($scope.searchMap.pageNo<=3){		
				// 前 5 页
				lastPage=5; 
			}else if(
				// 如果当前页大于等于最大页码-2
					$scope.searchMap.pageNo>=lastPage-2 ){
				// 后 5 页
				firstPage= maxPageNo-4; 
			
			}else{ 
				// 显示当前页为中心的 5 页
				firstPage=$scope.searchMap.pageNo-2;
				lastPage=$scope.searchMap.pageNo+2;
			}
		}
		// 循环产生页码标签
		for(var i=firstPage;i<=lastPage;i++){
			$scope.pageLabel.push(i);
		}
					
	}
		


	// 定义添加过滤条件函数
	// 点击:addFilterCondition('category','手机')
	$scope.addFilterCondition = function(key, value) {
		// 清空主查询
		$scope.searchMap.keywords = "";
		// 判断过滤条件类型
		if (key == "category" || key == "brand" || key == "price") {
			$scope.searchMap[key] = value;
		} else {
			$scope.searchMap.spec[key] = value;
		}

		// 调用搜索方法
		$scope.searchList();

	}

	// 撤销查询选项
	$scope.removeSearchItem = function(key) {
		// 判断
		if (key == "category" || key == "brand" || key == "price") {
			$scope.searchMap[key] = "";
		} else {
			delete $scope.searchMap.spec[key];
		}
		// 调用搜索方法
		$scope.searchList();

	};

	// 排序函数方法
	$scope.sortSearch = function(sortField, sortValue) {
		// 把参数赋值到参数map中
		$scope.searchMap.sortField = sortField;
		$scope.searchMap.sort = sortValue;

		// 调用搜索方法
		$scope.searchList();
	};

	// 定义分页查询函数
	$scope.queryForPage = function(pageNo) {
		// 判断当前页<1,或者总页码数<1
		if (pageNo < 1 || pageNo > $scope.searchResult.totalPages) {
			return;
		}
		// 封装参数
		$scope.searchMap.pageNo = parseInt(pageNo);
		// 调用查询方法
		$scope.searchList();
	}

});
