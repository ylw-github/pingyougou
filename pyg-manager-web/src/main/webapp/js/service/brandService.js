//抽取服务
app.service("brandService", function($http) {
	// 查询所有
	this.findAll = function() {
		return $http.get("../brand/findAll");
	};
	// 分页查询
	this.findPage = function(page, rows) {
		return $http.get('../brand/findPage?page=' + page + '&rows=' + rows);
	};

	// 添加
	this.add = function(entity) {
		return $http.post("../brand/add", entity);
	}

	// 修改
	this.update = function(entity) {
		return $http.post("../brand/update", entity);
	}

	// 根据id查询
	this.findOne = function(id) {
		return $http.get("../brand/findOne?id=" + id);
	};
	// 条件查询
	this.search = function(page, rows, searchEntity) {
		return $http.post("../brand/search?page=" + page + "&rows=" + rows,
				searchEntity);
	}
	// 删除
	this.dele = function(selectIds) {
		return $http.get("../brand/dele?ids=" + selectIds);
	}
});
