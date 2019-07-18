 //控制层 
app.controller('userController' ,function($scope,$controller   ,userService){
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		userService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		userService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		userService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=userService.update( $scope.entity ); //修改  
		}else{
			serviceObject=userService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		userService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		userService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//注入方法
	$scope.reg = function(){
		//验证2次密码是否一样
		if($scope.entity.password!=$scope.password){
			return;
		}
		//调用服务层,进行注册
		userService.add($scope.entity,$scope.smsCode).success(function(data){
			if(data.success){
				//跳转到登录页面
				location.href="login.html";
			}else{
				alert("注册失败");
			}
		})
	};
	
	//定义发送短信方法
	$scope.smsMessage = function(){
		//判断手机号码是否填写
		if($scope.entity.phone==null){
			alert("手机号必须填写");
			return;
		}
		//否则手机号填写
		//调用服务,发送请求
		userService.smsCode($scope.entity.phone).success(function(data){
			//判断
			if(data.success){
				//
				alert("发送短语成功");
			}else{
				//发送失败
				alert("发送短信失败");
			}
		});
		
	};
    
});	
