
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>


<script>
$(function(){
	
	<c:if test="${!empty msg}">
	$("span.errorMessage").html("${msg}");
	$("div.registerErrorMessageDiv").css("visibility","visible");		
	</c:if>
	
	$(".registerForm").submit(function(){
		if(0==$("#name").val().length){
			$("span.errorMessage").html("请输入登录名");
			$("div.registerErrorMessageDiv").css("visibility","visible");			
			return false;
		}		
		if(0==$("#phone").val().length){
			$("span.errorMessage").html("请输入电话号码");
			$("div.registerErrorMessageDiv").css("visibility","visible");			
			return false;
		}		
		
		}		

		return true;
	});
})
</script>



<form method="post" action="forefondpassword" class="registerForm">


	<div class="registerDiv">
		<div class="registerErrorMessageDiv">
			<div class="alert alert-danger" role="alert">
				<button type="button" class="close" data-dismiss="alert"
					aria-label="Close"></button>
				<span class="errorMessage"></span>
			</div>
		</div>


		<table align="center" class="registerTable">
		<tbody>
			<tr>
				<div style="height: 50px;"></div>
				<div>
					<img  src="img/jindu/jindu2.png" class="imgjindu"/>
				</div>
				
				<td class="registerTip registerTableLeftTD">
					找回密码
				</td>
				<td></td>
			</tr>
			<tr>
				<td class="registerTableLeftTD">登陆名</td>
				<td class="registerTableRightTD"><input placeholder="会员名一旦设置成功，无法修改" name="name" id="name"> </td>
			</tr>
			<tr>
				<td class="registerTip registerTableLeftTD">请输入您的手机号码</td>
				<td class="registerTableRightTD">注：注册时所提交的手机号码</td>
			</tr>
			<tr>
				<td class="registerTableLeftTD">手机号码</td>
				<td class="registerTableRightTD"><input type="text" placeholder="手机号码" name="phone" id="phone"> </td>
			</tr>
			
			<tr>
				<td class="registerButtonTD" colspan="2">
					<a href="fondpasswordPage2.jsp"><button>下一步</button></a>
				</td>
			</tr>
		</tbody>
	</table>
	</div>
</form>