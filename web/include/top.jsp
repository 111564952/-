<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<nav class="top">
	<a href="${contextPath }"> <span
		style="color: #C40000; margin: 0px"
		class=" glyphicon glyphicon-home redColor"></span> 二手书首页
	</a> <span>喵，欢迎来二手书</span>
	<c:if test="${!empty user}">
		<a href="login.jsp">${user.name}</a>
		<a href="forelogout">退出</a>
	</c:if>

	<c:if test="${empty user}">
		<a href="login.jsp">请登录</a>
		<a href="register.jsp">免费注册</a>
	</c:if>

	<span class="pull-right"> <a href="forebought">我的订单</a> 
	<!-- <a href="foremy">我的页面</a> -->
	<a href="foregivebooks">我要捐书</a>
	<a href="forecart"> <span style="color: #C40000; margin: 0px"
			class=" glyphicon glyphicon-shopping-cart redColor">
			</span> 购物车<strong>${cartTotalItemNumber}</strong>件
	</a>
	<a href="#">客服服务</a>
	</span>
</nav>