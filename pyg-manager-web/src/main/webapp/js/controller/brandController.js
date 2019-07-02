//定义控制器
app.controller("brandController", function($scope,$controller,brandService) {
	
	//实现控制器继承
	//父域$scope传递给子域$scope
	$controller("baseController",{$scope:$scope});
	
	// 查询所有函数
	$scope.findAll = function() {
		// 使用内置服务发送请求
		brandService.findAll().success(function(data) {
			$scope.list = data;
		})

	};	
	
	// 分页
	$scope.findPage = function(page, rows) {
		brandService.findPage(page, rows).success(function(data) {
			$scope.list = data.rows;
			$scope.paginationConf.totalItems = data.total;// 更新总记录数
		});
	};

	// 添加函数
	$scope.add = function() {

		// 初始化返回对象
		var obj = null;
		// 判断是否是添加
		if ($scope.entity.id != null) {
			obj = brandService.update($scope.entity);
		} else {
			obj = brandService.add($scope.entity);
		}

		obj.success(function(data) {
			// 判断
			if (data.success) {
				// 刷新
				$scope.reloadList();
			} else {
				alert(data.message);
			}
		})

	};

	// 根据id查询品牌
	$scope.findOne = function(id) {
		// 发送请求
		brandService.findOne(id).success(function(data) {
			$scope.entity = data;
		})
	}

	// 初始化对象
	$scope.searchEntity = {};

	// 条件查询方法
	$scope.search = function(page, rows) {
		// 发送条件查询请求
		brandService.search(page, rows, $scope.searchEntity).success(
				function(data) {
					$scope.list = data.rows;
					$scope.paginationConf.totalItems = data.total;// 更新总记录数
				})

	}

	
	// 批量删除品牌数据
	$scope.dele = function() {
		// 发送删除请求
		brandService.dele($scope.selectIds).success(function(data) {
			// 判断是否删除成功
			if (data.success) {
				// 刷新列表
				$scope.reloadList();
			} else {
				alert("删除失败");
			}
		})
	};

})