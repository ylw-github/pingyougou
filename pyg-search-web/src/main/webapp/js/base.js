//定义模块
var app = angular.module("pyg", []);
//定义过滤器
//此过滤器作用:把文本格式数据变成html
app.filter("trustHtml",["$sce",function($sce){
	return function(data) {
		//把文本数据转换成html识别
		return $sce.trustAsHtml(data);
	}
}]);