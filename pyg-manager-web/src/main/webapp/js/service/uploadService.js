//服务层
app.service('uploadService', function($http) {

	// 文件上传
	this.uploadFile = function() {
		//定义表单数据对象
		var formData = new FormData();
		//向表单数据对象中添加文件
		formData.append('file',file.files[0]);
		
		return $http({
			method:'POST',
			url:'../shop/upload',
			data:formData,
			headers:{'Content-Type':undefined},//angularjs自动识别类型为multipart/formdata类型
			transformRequest: angular.identity
			});

	};

});
