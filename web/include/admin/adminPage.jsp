<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	
<script>
$(function(){
	$("ul.pagination li.disabled a").click(function(){
		return false;
	});
});

</script>

<nav>
  <ul class="pagination">
<!--   首页超链： -->
<!--     当page.hasPreviouse为false的时候，为首页连接套用Bootstrap样式 disabled 变为不可点击状态-->
    <li <c:if test="${!page.hasPreviouse}">class="disabled"</c:if>>
      <a  href="?page.start=0${page.param}" aria-label="Previous" >
        <span aria-hidden="true">&laquo;</span>
      </a>
    </li>
<!-- 上一页超链： -->
    <li <c:if test="${!page.hasPreviouse}">class="disabled"</c:if>>
      <a  href="?page.start=${page.start-page.count}${page.param}" aria-label="Previous" >
        <span aria-hidden="true">&lsaquo;</span>
      </a>
    </li>    
<!-- 中间页 -->
    <c:forEach begin="0" end="${page.totalPage-1}" varStatus="status">
    
    	<c:if test="${status.count*page.count-page.start<=20 && status.count*page.count-page.start>=-10}">
		    <li <c:if test="${status.index*page.count==page.start}">class="disabled"</c:if>>
		    	<a  
		    	href="?page.start=${status.index*page.count}${page.param}"
		    	<c:if test="${status.index*page.count==page.start}">class="current"</c:if>
		    	>${status.count}</a>
		    </li>
		</c:if>
    </c:forEach>
<!-- 下一页超链： -->
    <li <c:if test="${!page.hasNext}">class="disabled"</c:if>>
      <a href="?page.start=${page.start+page.count}${page.param}" aria-label="Next">
        <span aria-hidden="true">&rsaquo;</span>
      </a>
    </li>
<!-- 最后一页 -->
    <li <c:if test="${!page.hasNext}">class="disabled"</c:if>>
      <a href="?page.start=${page.last}${page.param}" aria-label="Next">
        <span aria-hidden="true">&raquo;</span>
      </a>
    </li>
  </ul>
</nav>
