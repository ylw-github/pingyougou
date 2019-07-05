//控制层 
app
		.controller(
				'goodsController',
				function($scope, $controller, $location, goodsService,
						itemCatService, uploadService, typeTemplateService) {

					$controller('baseController', {
						$scope : $scope
					});// 继承

					// 读取列表数据绑定到表单中
					$scope.findAll = function() {
						goodsService.findAll().success(function(response) {
							$scope.list = response;
						});
					}

					// 分页
					$scope.findPage = function(page, rows) {
						goodsService
								.findPage(page, rows)
								.success(
										function(response) {
											$scope.list = response.rows;
											$scope.paginationConf.totalItems = response.total;// 更新总记录数
										});
					}

					// 查询实体
					$scope.findOne = function() {

						// 接受参数
						// 即可接受ng-click事件参数值
						// 也可接受其他页面跳转传递的参数
						var id = $location.search()["id"];
						// 判断页面传递参数是否存在
						if (id == null) {
							return;
						}
						// 根据id查询
						goodsService
								.findOne(id)
								.success(
										function(response) {
											$scope.entity = response;
											// 回显描述信息
											editor
													.html($scope.entity.goodsDesc.introduction);
											// 回显图片属性
											$scope.entity.goodsDesc.itemImages = JSON
													.parse($scope.entity.goodsDesc.itemImages);
											// 回显扩展属性
											$scope.entity.goodsDesc.customAttributeItems = JSON
													.parse($scope.entity.goodsDesc.customAttributeItems);
											// 回显规格属性
											$scope.entity.goodsDesc.specificationItems = JSON
													.parse($scope.entity.goodsDesc.specificationItems);
											//回显规格属性选项组合sku
											for(var i = 0; i<$scope.entity.itemList.length;i++){
												$scope.entity.itemList[i].spec = JSON.parse($scope.entity.itemList[i].spec);
											}
										});
					}

					// 保存
					$scope.save = function() {
						var serviceObject;// 服务层对象
						if ($scope.entity.goods.id != null) {// 如果有ID
							serviceObject = goodsService.update($scope.entity); // 修改
						} else {

							// 获取富文本编辑器数据
							$scope.entity.goodsDesc.introduction = editor
									.html();
							// 保存
							serviceObject = goodsService.add($scope.entity);// 增加
						}
						serviceObject.success(function(response) {
							if (response.success) {
								// 清空对象
								$scope.entity = {};
								// 清空富文本编辑器
								editor.html('');
							} else {
								alert(response.message);
							}
						});
					}

					// 批量删除
					$scope.dele = function() {
						// 获取选中的复选框
						goodsService.dele($scope.selectIds).success(
								function(response) {
									if (response.success) {
										$scope.reloadList();// 刷新列表
										$scope.selectIds = [];
									}
								});
					}

					$scope.searchEntity = {};// 定义搜索对象

					// 搜索
					$scope.search = function(page, rows) {
						goodsService
								.search(page, rows, $scope.searchEntity)
								.success(
										function(response) {
											$scope.list = response.rows;
											$scope.paginationConf.totalItems = response.total;// 更新总记录数
										});
					};

					// 定义查询节点方法
					// 先查询顶级节点,默认父id=0
					// 查询一级节点
					$scope.findItemCat1List = function() {
						// 调用服务方法
						itemCatService.findItemCatByParentId(0).success(
								function(data) {

									// 赋值
									$scope.itemCat1List = data;

								});
					};

					// 查询二级级联节点
					// $watch: 监控属性值变化,从而查询二级节点
					$scope.$watch('entity.goods.category1Id', function(
							newValue, oldValue) {
						// 使用新变化的值查询子节点
						// newValue就是分类id
						// 调用服务方法
						itemCatService.findItemCatByParentId(newValue).success(
								function(data) {

									// 赋值
									$scope.itemCat2List = data;

								});
					});

					// 查询三级级联节点
					// $watch: 监控属性值变化,从而查询二级节点
					$scope.$watch('entity.goods.category2Id', function(
							newValue, oldValue) {
						// 使用新变化的值查询子节点
						// newValue就是分类id
						// 调用服务方法
						itemCatService.findItemCatByParentId(newValue).success(
								function(data) {

									// 赋值
									$scope.itemCat3List = data;

								});
					});

					// 监控第三级节点,查询模版id
					// $watch: 监控属性值变化
					$scope
							.$watch(
									'entity.goods.category3Id',
									function(newValue, oldValue) {
										// 使用新变化的值查询子节点
										// newValue就是分类id
										// 调用服务方法
										itemCatService
												.findOne(newValue)
												.success(
														function(data) {
															$scope.entity.goods.typeTemplateId = data.typeId;
														});
									});

					// 监控模版id变化,查询模版数据
					$scope
							.$watch(
									'entity.goods.typeTemplateId',
									function(newValue, oldValue) {
										// 查询模版对象值
										// 调用模版服务
										typeTemplateService
												.findOne(newValue)
												.success(
														function(data) {

															// 绑定模版对象
															$scope.typeTemplate = data;
															// 获取模版品牌数据
															$scope.typeTemplate.brandIds = JSON
																	.parse($scope.typeTemplate.brandIds);

															// 判断
															// 判断当id不存在时才赋值(不是修改)
															if ($location
																	.search()["id"] == null) {
																// 获取模版中扩展属性
																$scope.entity.goodsDesc.customAttributeItems = JSON
																		.parse($scope.typeTemplate.customAttributeItems);

															}

														});

										// 查询模版中存储关键规格属性对应规格选项
										// 调用模版服务方法
										typeTemplateService
												.findSpecOptionListByTypeId(
														newValue)
												.success(function(data) {
													$scope.specList = data;
												});

									});

					// 文件上传
					$scope.uploadFile = function() {
						// 调用服务层
						uploadService.uploadFile().success(function(data) {
							// 是否上传成功
							if (data.success) {
								// 把图片地址获取
								$scope.imageEntity.url = data.message;
							} else {
								// 上传失败
								alert("上传失败");
							}
						});
					};

					// 定义初始化对象
					$scope.entity = {
						goods : {},
						goodsDesc : {
							itemImages : [],
							specificationItems : []
						}
					};
					// 保存图片操作
					// 保存图片:只需要利用数据双向绑定原理,获取到图片上传数据,把数据绑定到需要保存entity实体即可
					$scope.add_image_entity = function() {
						// 把图片对象数据添加到商品描述对象
						$scope.entity.goodsDesc.itemImages
								.push($scope.imageEntity);
					}

					// 删除图片对象
					$scope.removeImageEntity = function(index) {
						$scope.entity.goodsDesc.itemImages.splice(index, 1);
					}

					// 定义选中规格属性事件
					$scope.updateSpecAttribute = function($event, text, name) {
						// 获取实体中规格选项值
						// [{"attributeName":"网络","attributeValue":["移动3G"]}]
						var specList = $scope.entity.goodsDesc.specificationItems;

						// 循环规格选项值
						for (var i = 0; i < specList.length; i++) {
							// 判断选择的是那个属性
							if (specList[i].attributeName == text) {
								// 判断是否选中事件
								if ($event.target.checked) {
									// [{"attributeName":"网络","attributeValue":["移动3G","联通4G"]}]
									// 把规格选项推送到规格选项数据结构中
									specList[i].attributeValue.push(name);
								} else {
									// [{"attributeName":"网络","attributeValue":["移动3G"]}]
									// 取消事件
									specList[i].attributeValue.splice(
											specList[i].attributeValue
													.indexOf(name), 1);
								}

								return;
							}

						}
						// 如果商品描述中规格属性没值,把选择中值推送到集合中
						// 第一次点击规格属性选项值
						// [{"attributeName":"网络","attributeValue":["移动3G"]}]
						specList.push({
							attributeName : text,
							attributeValue : [ name ]
						});
					};

					// 定义函数,封装规格选项组合成商品最小销售单元
					$scope.createSkuItemList = function() {

						// 初始化规格数据组合
						$scope.entity.itemList = [ {
							spec : {},
							price : 999999,
							stockCount : 0,
							status : '0',
							isDefault : '0'
						} ];
						// 获取选中规格属性值
						// [{"attributeName":"网络","attributeValue":["电信2G","联通2G"]},{"attributeName":"机身内存","attributeValue":["16G","64G"]}]
						var specList = $scope.entity.goodsDesc.specificationItems;

						// 循环规格属性值,组合sku最小销售单元商品数据
						for (var i = 0; i < specList.length; i++) {
							// 第一次循环:$scope.entity.itemList =
							// [{spec:{"网络":"电信2G"},price:999999,stockCount:0,status:'0',idDefault:'0'},{spec:{"网络":"联通2G"},price:999999,stockCount:0,status:'0',idDefault:'0'}]
							// 添加一列
							$scope.entity.itemList = addColumn(
									$scope.entity.itemList,
									specList[i].attributeName,
									specList[i].attributeValue);

						}

					};

					addColumn = function(list, name, columnValues) {

						var newList = [];

						// 第一次循环数据:[{spec:{},price:999999,stockCount:0,status:'0',idDefault:'0'}];
						// 第二次循环数据:[{spec:{"网络":"电信2G"},price:999999,stockCount:0,status:'0',idDefault:'0'},{spec:{"网络":"联通2G"},price:999999,stockCount:0,status:'0',idDefault:'0'}]
						// 循环list集合数据 2
						for (var i = 0; i < list.length; i++) {

							// 第一次循环第一个对象:{spec:{},price:999999,stockCount:0,status:'0',idDefault:'0'}
							// 第二次循环第一个对象:{spec:{"网络":"电信2G"},price:999999,stockCount:0,status:'0',idDefault:'0'}
							// 获取一个旧的对象
							var oldRow = list[i];
							// 第一次循环:columnValues:["电信2G","联通2G"]
							// 第二次循环:columnValues:["16G","64G"]
							// 第二个循环
							for (var j = 0; j < columnValues.length; j++) {
								// 第一次克隆:{spec:{},price:999999,stockCount:0,status:'0',idDefault:'0'}
								// 第二次克隆:
								// {spec:{"网络":"电信2G"},price:999999,stockCount:0,status:'0',idDefault:'0'}
								// 深克隆操作,新创建一行数据
								var newRow = JSON.parse(JSON.stringify(oldRow));
								// {spec:{"网络":"电信2G",:"机身内存":"4G"},price:999999,stockCount:0,status:'0',idDefault:'0'}
								newRow.spec[name] = columnValues[j];

								// j:循环第一次:{spec:{"网络":"电信2G"},price:999999,stockCount:0,status:'0',idDefault:'0'}
								// j:循环第二次:{spec:{"网络":"联通2G"},price:999999,stockCount:0,status:'0',idDefault:'0'}
								// 推送集合
								newList.push(newRow);
							}
						}
						// [{spec:{"网络":"电信2G"},price:999999,stockCount:0,status:'0',idDefault:'0'},{spec:{"网络":"联通2G"},price:999999,stockCount:0,status:'0',idDefault:'0'}]
						return newList;

					};

					// 定义状态字符串数组
					$scope.state = [ "未审核", "已审核", "审核未通过", "关闭" ];
					// 定义封装分类名称数组
					$scope.itemCatList = [];
					// 查询商品分类
					$scope.findAllCatList = function() {

						// 调用分类服务
						itemCatService.findAll().success(function(data) {
							// 循环返回分类数据集合
							for (var i = 0; i < data.length; i++) {
								$scope.itemCatList[data[i].id] = data[i].name;
							}

						});

					};

					// 定义检测规格选项是否选中的函数
					// 选中:return true
					// 未选中: return false
					$scope.isAttributeChecked = function(specName, value) {
						// 获取商品规格属性值
						var specList = $scope.entity.goodsDesc.specificationItems;
						// 循环规格属性
						for (var i = 0; i < specList.length; i++) {
							// 判断选择的属性属于那个属性分类
							if (specList[i].attributeName == specName) {
								// 判断规格选项是否包含此时选中选项值
								if (specList[i].attributeValue.indexOf(value) >= 0) {

									return true;

								}
							}							

						}
						
						return false;

					};

				});
