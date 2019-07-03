//控制层 
app.controller('typeTemplateController', function($scope, $controller,
		typeTemplateService) {

	$controller('baseController', {
		$scope : $scope
	});// 继承

	// 读取列表数据绑定到表单中
	$scope.findAll = function() {
		typeTemplateService.findAll().success(function(response) {
			$scope.list = response;
		});
	}

	// 分页
	$scope.findPage = function(page, rows) {
		typeTemplateService.findPage(page, rows).success(function(response) {
			$scope.list = response.rows;
			$scope.paginationConf.totalItems = response.total;// 更新总记录数
		});
	}

	// 查询实体
	$scope.findOne = function(id) {
		typeTemplateService.findOne(id).success(function(response) {
			$scope.entity = response;
			//规格属性.把规格字符串转换层json对象
			$scope.entity.specIds = JSON.parse($scope.entity.specIds);
			//品牌数据
			$scope.entity.brandIds = JSON.parse($scope.entity.brandIds);
			//扩展属性
			$scope.entity.customAttributeItems = JSON.parse($scope.entity.customAttributeItems);
		});
	};

	// 保存
	$scope.save = function() {
		var serviceObject;// 服务层对象
		if ($scope.entity.id != null) {// 如果有ID
			serviceObject = typeTemplateService.update($scope.entity); // 修改
		} else {
			serviceObject = typeTemplateService.add($scope.entity);// 增加
		}
		serviceObject.success(function(response) {
			if (response.success) {
				// 重新查询
				$scope.reloadList();// 重新加载
			} else {
				alert(response.message);
			}
		});
	}

	// 批量删除
	$scope.dele = function() {
		// 获取选中的复选框
		typeTemplateService.dele($scope.selectIds).success(function(response) {
			if (response.success) {
				$scope.reloadList();// 刷新列表
				$scope.selectIds = [];
			}
		});
	}

	$scope.searchEntity = {};// 定义搜索对象

	// 搜索
	$scope.search = function(page, rows) {
		typeTemplateService.search(page, rows, $scope.searchEntity).success(
				function(response) {
					$scope.list = response.rows;
					$scope.paginationConf.totalItems = response.total;// 更新总记录数
				});
	};

	// 模版表存储数据格式:
	// 品牌:
	// [{"id":16,"text":"TCL"},{"id":13,"text":"长虹"},{"id":14,"text":"海尔"},{"id":19,"text":"创维"},{"id":21,"text":"康佳"},{"id":18,"text":"夏普"},{"id":17,"text":"海信"},{"id":20,"text":"东芝"},{"id":15,"text":"飞利浦"},{"id":22,"text":"LG"}]
	// 规格数据格式:
	// [{"id":33,"text":"电视屏幕尺寸"}]
	// 扩展属性:
	// [{"text":"内存大小"},{"text":"颜色"}]
	// 模拟品牌下拉框数据
	// 加载品牌下拉列表
	$scope.findBrandList = function() {
		// 调用服务层方法
		typeTemplateService.findBrandList().success(function(data) {
			// 下拉框数据封装
			$scope.brandList = {
				data : data
			};

		});
	};

	// 查询规格属性下拉列表
	$scope.findSpecOptionList = function() {
		// 调用service服务方法
		typeTemplateService.findSpecOptionList().success(function(data) {
			$scope.specList = {
				data : data
			};
		});
	};
	//新增规格行
	$scope.addTableRow = function(){
		$scope.entity.customAttributeItems.push({});
	}
	//删除扩展属性行
	$scope.deleTableRow = function(index){
		$scope.entity.customAttributeItems.splice(index,1);
	}
	// $scope.brandList =
	// {data:[{id:1,text:'联想'},{id:2,text:'华为'},{id:3,text:'小米'}]};

});
