<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<a href="${contextPath}"> <img id="logo" src="img/site/logo.gif"
	class="logo" width="200px">
</a>
<form action="foresearch" method="post">
	<div class="searchDiv" style="position: relative;">
		<input name="keyword" type="text" placeholder="计算机与科学 ">
		<button type="submit">搜索</button>
		<div class="searchBelow">
			<c:forEach items="${cs}" var="c" varStatus="st">
				<c:if test="${st.count>=5 and st.count<=8}">
					<span> <a href="forecategory?cid=${c.id}"> ${c.name} </a> <c:if
							test="${st.count!=8}">
							<span>|</span>
						</c:if>
					</span>
				</c:if>
			</c:forEach>
		</div>
		<div style="position: relative; left: 550px; bottom: 60px;">
			<span>
				<button class="btn"
					style="background-color: #FFF; color: #c40000; border: 1px solid #DDD; width: 30%;">
					<span style="font-size: 14px; color: #C40000; margin: 0px"
						class=" glyphicon glyphicon-shopping-cart redColor"> </span><a
						href="forecart"> 购物车</a> ${cartTotalItemNumber}
				</button>

			</span><span>

				<button class="btn"
					style="background-color: #FFF; color: #c40000; border: 1px solid #DDD; width: 30%;">
					<a href="forebought"> 我的订单</a>
				</button>

			</span>

		</div>
	</div>

</form>
<style>
}
</style>