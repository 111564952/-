<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div
	style="width: 100%; height: 70px; line-height: 90px; background-color: #c40000; border: 1px solid red;">
	<div
		style="margin-left: 70px; float: left; color: white; font-size: 30px;">
		<a href="my.jsp" style="color: #FFF;">我的页面</a>
	</div>
</div>

<a href="forerelease"><button style="background-color: #c40000; color: #ffffff ;">发布宝贝赚钱</button></a>
<div class="cartProductList">
	<table class="cartProductTable">
		<thead>
			<tr>
				<th>商品名称</th>
				<th>小标题</th>
				<th>卖书原因</th>
				<th width="120px">原价</th>
				<th width="120px">二手价格</th>
				<th width="120px">破损程度</th>
				<th width="120px">分类名称</th>
				<th width="120px">联系电话</th>
				<th class="operation">操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${sell_Products}" var="sell_Product" >
			<tr>
				<th>${sell_Product.name}</th>
				<th>${sell_Product.littname}</th>
				<th>${sell_Product.because}</th>
				<th width="120px">${sell_Product.oldPrice}</th>
				<th width="120px">${sell_Product.newPrice}</th>
				<th width="120px">${sell_Product.breakage}</th>
				<th width="120px">${sell_Product.category.name}</th>
				<th width="120px">${sell_Product.mobile}</th>
				<th class="operation">删除</th>
			</tr>
			</c:forEach>
		</tbody>

	</table>
</div>