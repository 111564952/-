<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<title>${c.name}</title>
<div class="categoryPictureInProductPageDiv">
	<img class="categoryPictureInProductPage"
		src="img/category/${c.id}.jpg">
</div>
<div id="category">
	<div class="categoryPageDiv">
		<%--   <img src="img/category/${c.id}.jpg"> --%>
		<%@include file="sortBar.jsp"%>
		<%@include file="productsByCategory.jsp"%>
	</div>
</div>