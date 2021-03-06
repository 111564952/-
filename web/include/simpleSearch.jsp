<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div>
	<a href="${contextPath}"> <img id="simpleLogo" class="simpleLogo"
		src="img/site/simpleLogo.png" width="60px"height="50px">
	</a>

	<form action="foresearch" method="post">
		<div class="simpleSearchDiv pull-right">
			<input type="text" placeholder="呐喊  彷徨" name="keyword">
			<button class="searchButton" type="submit">搜书籍</button>
			<div class="searchBelow" style="position:relative;right:50px;width:380px;">
			<c:forEach items="${cs}" var="c" varStatus="st">
				<c:if test="${st.count>=8 and st.count<=11}">
					<span>
						<a href="forecategory?cid=${c.id}">
							 ${c.name}
						</a>
						<c:if test="${st.count!=11}">            
                            <span>|</span>             
                        </c:if>
					</span>
				</c:if>
			</c:forEach>
			
			</div>
		</div>
	</form>
	<div style="clear: both"></div>
</div>
