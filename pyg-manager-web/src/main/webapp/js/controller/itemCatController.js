//控制层 
app.controller('itemCatController', function($scope, $controller,
		itemCatService) {

	$controller('baseController', {
		$scope : $scope
	});// 继承

	// 读取列表数据绑定到表单中
	$scope.findAll = function() {
		itemCatService.findAll().success(function(response) {
			$scope.list = response;
		});
	}

	// 分页
	$scope.findPage = function(page, rows) {
		itemCatService.findPage(page, rows).success(function(response) {
			$scope.list = response.rows;
			$scope.paginationConf.totalItems = response.total;// 更新总记录数
		});
	}

	// 查询实体
	$scope.findOne = function(id) {
		itemCatService.findOne(id).success(function(response) {
			$scope.entity = response;
		});
	}

	// 保存
	$scope.save = function() {
		var serviceObject;// 服务层对象
		if ($scope.entity.id != null) {// 如果有ID
			serviceObject = itemCatService.update($scope.entity); // 修改
		} else {
			
			//给保存对象赋值
			$scope.entity.parentId = $scope.parentId;
			//保存
			serviceObject = itemCatService.add($scope.entity);// 增加
		}
		serviceObject.success(function(response) {
			if (response.success) {
				// 重新查询
				//调用查询下级方法
				$scope.findItemCatByParentId($scope.parentId);
			} else {
				alert(response.message);
			}
		});
	}

	// 批量删除
	$scope.dele = function() {
		// 获取选中的复选框
		itemCatService.dele($scope.selectIds).success(function(response) {
			if (response.success) {
				$scope.reloadList();// 刷新列表
				$scope.selectIds = [];
			}
		});
	}

	$scope.searchEntity = {};// 定义搜索对象

	// 搜索
	$scope.search = function(page, rows) {
		itemCatService.search(page, rows, $scope.searchEntity).success(
				function(response) {
					$scope.list = response.rows;
					$scope.paginationConf.totalItems = response.total;// 更新总记录数
				});
	};

	//定义父节点id变量,记录父节点值
	$scope.parentId = 0;
	
	// 根据父id查询子节点
	$scope.findItemCatByParentId = function(parentId) {
		debugger
		
		//查询下级节点给父节点赋值,记录每一次节点变化父节点
		$scope.parentId = parentId;
		
		// 调用商品分类service服务
		itemCatService.findItemCatByParentId(parentId).success(function(data) {
			$scope.itemCatList = data;
		});
	};

	// 定义标识,记录菜单节点级别
	// 默认一级菜单
	$scope.grade = 1;

	// 定义方法,级别加1操作
	$scope.setGrade = function(value) {
		$scope.grade = value;
	}

	// 根据父id查询子节点,记录每一级节点对象
	$scope.selectCatList = function(p_entity) {
		// 如果是第一级菜单
		if ($scope.grade == 1) {

			$scope.entity_1 = null;
			$scope.entity_2 = null;
		}

		// 如果是第二级菜单
		if ($scope.grade == 2) {
			$scope.entity_1 = p_entity;
			$scope.entity_2 = null;
		}
		
		// 如果是第三级菜单
		if ($scope.grade == 3) {
			$scope.entity_2 = p_entity;
		};
		
		//调用查询下级方法
		$scope.findItemCatByParentId(p_entity.id);
	}
});
