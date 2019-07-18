<!DOCTYPE html>
<html>

<head>
	<meta charset="utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=7; IE=EDGE">
	<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
	<title>产品详情页</title>
	 <link rel="icon" href="assets/img/favicon.ico">

    <link rel="stylesheet" type="text/css" href="css/webbase.css" />
    <link rel="stylesheet" type="text/css" href="css/pages-item.css" />
    <link rel="stylesheet" type="text/css" href="css/pages-zoom.css" />
    <link rel="stylesheet" type="text/css" href="css/widget-cartPanelView.css" />
    
    <script type="text/javascript" src="plugins/angularjs/angular.min.js"> </script>
    <script type="text/javascript" src="js/base.js"> </script>
    <script type="text/javascript" src="js/controller/itemController.js"> </script>
    
    <script>
        var skuList=[
           
           <#list itemList as item>
            {
              id:'${item.id}',
              title:'${item.title}',
              price:${item.price},
              status:'${item.status}',
              isDefault:'${item.isDefault}',
              spec:${item.spec}            
            },
           </#list>
        
        ];
        
    </script>
    
</head>

<body ng-app="pyg" ng-controller="itemController" ng-init="num=1;loadDefaultSku()" >
<#--转换图片列表变量-->
<#assign imageList=goodsDesc.itemImages?eval >
<#--转换扩展属性变量-->
<#assign customAttributeList=goodsDesc.customAttributeItems?eval >
<#--转换规格变量-->
<#assign specificationList=goodsDesc.specificationItems?eval >

<#include 'head.ftl'>

	<div class="py-container">
		<div id="item">
			<div class="crumb-wrap">
				<ul class="sui-breadcrumb">
					<li>
						<a href="#">${map.itemCat1}</a>
					</li>
					<li>
						<a href="#">${map.itemCat2}</a>
					</li>
					<li>
						<a href="#">${map.itemCat3}</a>
					</li>
					
				</ul>
			</div>
			<!--product-info-->
			<div class="product-info">
				<div class="fl preview-wrap">
					<!--放大镜效果-->
					<div class="zoom">
						<!--默认第一个预览-->
						<div id="preview" class="spec-preview">
							<span class="jqzoom">
							  <#if (imageList?size>0) >
							     <img jqimg="${imageList[0].url}" src="${imageList[0].url}" width="400px" height="400px"/>
							  </#if>
							</span>
						</div>
						<!--下方的缩略图-->
						<div class="spec-scroll">
							<a class="prev">&lt;</a>
							<!--左右按钮-->
							<div class="items">
								<ul>
								   <#list imageList as item>
									   <li><img src="${item.url}" bimg="${item.url}" onmousemove="preview(this)" /></li>
								   </#list>
								</ul>
							</div>
							<a class="next">&gt;</a>
						</div>
					</div>
				</div>
				<div class="fr itemInfo-wrap">
					<div class="sku-name">
						<h4>{{sku.title}} </h4>
					</div>
					<div class="news"><span>${goods.caption}</span></div>
					<div class="summary">
						<div class="summary-wrap">
							<div class="fl title">
								<i>价　　格</i>
							</div>
							<div class="fl price">
								<i>¥</i>
								<em>{{sku.price}}</em>
								<span>降价通知</span>
							</div>
							<div class="fr remark">
								<i>累计评价</i><em>612188</em>
							</div>
						</div>
						<div class="summary-wrap">
							<div class="fl title">
								<i>促　　销</i>
							</div>
							<div class="fl fix-width">
								<i class="red-bg">加价购</i>
								<em class="t-gray">满999.00另加20.00元，或满1999.00另加30.00元，或满2999.00另加40.00元，即可在购物车换
购热销商品</em>
							</div>
						</div>
					</div>
					<div class="support">
						<div class="summary-wrap">
							<div class="fl title">
								<i>支　　持</i>
							</div>
							<div class="fl fix-width">
								<em class="t-gray">以旧换新，闲置手机回收  4G套餐超值抢  礼品购</em>
							</div>
						</div>
						<div class="summary-wrap">
							<div class="fl title">
								<i>配 送 至</i>
							</div>
							<div class="fl fix-width">
								<em class="t-gray">满999.00另加20.00元，或满1999.00另加30.00元，或满2999.00另加40.00元，即可在购物车换购热销商品</em>
							</div>
						</div>
					</div>
					<div class="clearfix choose">
						<div id="specification" class="summary-wrap clearfix">
							<#list specificationList as specification>
							 <dl>
								<dt>
									<div class="fl title">
									<i>${specification.attributeName}</i>
								</div>
								</dt>
								
								<#list specification.attributeValue as item>
								  <dd><a class="{{isSelected('${specification.attributeName}','${item}')?'selected':''}}" 
								  ng-click="selectSpecification('${specification.attributeName}','${item}')" >${item} <span title="点击取消选择">&nbsp;</span></a></dd>
								</#list>
							  </dl>
							</#list>
							
						</div>
						
						
						
						
						
						
						
						
						
						<div class="summary-wrap">
							<div class="fl title">
								<div class="control-group">
									<div class="controls">
										<input autocomplete="off" type="text" value="{{num}}" minnum="1" class="itxt" />
										<a href="javascript:void(0)" ng-click="addNum(1)" class="increment plus">+</a>
										<a href="javascript:void(0)" ng-click="addNum(-1)" class="increment mins">-</a>
									</div>
								</div>
							</div>
							<div class="fl">
								<ul class="btn-choose unstyled">
									<li ng-if="sku.status=='1'">
									   
										<a ng-click="addGoodsToCartList()" target="_blank" class="sui-btn  btn-danger addshopcar">加入购物车</a>
									</li>
								</ul>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!--product-detail-->
			
			<#include 'left.ftl' >
				</div>
				<div class="fr detail">
					<div class="clearfix fitting">
						<h4 class="kt">选择搭配</h4>
						<div class="good-suits">
							<div class="fl master">
								<div class="list-wrap">
									<div class="p-img">
										<img src="img/_/l-m01.png" />
									</div>
									<em>￥5299</em>
									<i>+</i>
								</div>
							</div>
							<div class="fl suits">
								<ul class="suit-list">
									<li class="">
										<div id="">
											<img src="img/_/dp01.png" />
										</div>
										<i>Feless费勒斯VR</i>
										<label data-toggle="checkbox" class="checkbox-pretty">
    <input type="checkbox"><span>39</span>
  </label>
									</li>
									<li class="">
										<div id=""><img src="img/_/dp02.png" /> </div>
										<i>Feless费勒斯VR</i>
										<label data-toggle="checkbox" class="checkbox-pretty">
    <input type="checkbox"><span>50</span>
  </label>
									</li>
									<li class="">
										<div id=""><img src="img/_/dp03.png" /></div>
										<i>Feless费勒斯VR</i>
										<label data-toggle="checkbox" class="checkbox-pretty">
    <input type="checkbox"><span>59</span>
  </label>
									</li>
									<li class="">
										<div id=""><img src="img/_/dp04.png" /></div>
										<i>Feless费勒斯VR</i>
										<label data-toggle="checkbox" class="checkbox-pretty">
    <input type="checkbox"><span>99</span>
  </label>
									</li>
								</ul>
							</div>
							<div class="fr result">
								<div class="num">已选购0件商品</div>
								<div class="price-tit"><strong>套餐价</strong></div>
								<div class="price">￥5299</div>
								<button class="sui-btn  btn-danger addshopcar">加入购物车</button>
							</div>
						</div>
					</div>
					<div class="tab-main intro">
						<ul class="sui-nav nav-tabs tab-wraped">
							<li class="active">
								<a href="#one" data-toggle="tab">
									<span>商品介绍</span>
								</a>
							</li>
							<li>
								<a href="#two" data-toggle="tab">
									<span>规格与包装</span>
								</a>
							</li>
							<li>
								<a href="#three" data-toggle="tab">
									<span>售后保障</span>
								</a>
							</li>
							<li>
								<a href="#four" data-toggle="tab">
									<span>商品评价</span>
								</a>
							</li>
							<li>
								<a href="#five" data-toggle="tab">
									<span>手机社区</span>
								</a>
							</li>
						</ul>
						<div class="clearfix"></div>
						<div class="tab-content tab-wraped">
							<div id="one" class="tab-pane active">
								<ul class="goods-intro unstyled">
								   <#list customAttributeList as item>
								      <#if item.value??>
									     <li>${item.text}：${item.value}</li>
									  </#if>
								   </#list>
								</ul>
								<div class="intro-detail">
									${goodsDesc.introduction!}
								</div>
							</div>
							<div id="two" class="tab-pane">
								<p>${goodsDesc.packageList!}</p>
							</div>
							<div id="three" class="tab-pane">
								<p>${goodsDesc.saleService!}</p>
							</div>
							<div id="four" class="tab-pane">
								<p>商品评价</p>
							</div>
							<div id="five" class="tab-pane">
								<p>手机社区</p>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!--like-->
			<#include 'like.ftl'>
			
		</div>
	</div>
	<!-- 底部栏位 -->
<#include 'foot.ftl'>
</body>

</html>