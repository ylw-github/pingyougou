//服务层
app.service('payService',function($http){
	    	
	//本地支付
	this.createNative=function(){
	return $http.get('../pay/createNative');
	}
	
});
