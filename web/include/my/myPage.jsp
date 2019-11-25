<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>


<div
	style="width: 100%; height: 70px; line-height: 90px; background-color: #c40000; border: 1px solid red;">
	<div
		style="margin-left: 70px; float: left; color: white; font-size: 30px;">
		<a href="my.jsp" style="color: #FFF;">我的页面</a>
	</div>
</div>
<link href="css/mypage.css" rel="stylesheet">
<div class="myWithCarousel">
	<div style="position: relative">
		<div class="myMenu">
			<div class="mainMy">全部功能</div>
			<div class="eachMy">

				<a href="forecart"> 我的购物车 </a>
			</div>
			<div class="eachMy">
				<a href="forebought"> 已买到的宝贝 </a>
			</div>
			<div class="eachMy">
				<a href="#nowhere"> 已卖出的宝贝 </a>
			</div>
			<div class="eachMy">
				<a href="forestore"> 我的店铺 </a>
			</div>
		</div>
	</div>
	<div style="position: relative; left: 0; top: 0;">
		<div class="myhead">
			<div class="boughtDiv">
				<div class="orderListItem">
					<table oid="946" orderstatus="waitReview"
						class="orderListItemTable">
						<tbody>
							<tr class="orderListItemFirstTR">
								<td colspan="2"><b>用户图片</b> <span>用户名</span></td>
								<td colspan="2"><a href="#nowhere">我的地址</a></td>
								<td colspan="1"><a href="#nowhere">我要卖书</a></td>
								<td class="orderItemDeleteTD"><a href="#nowhere">联系我们</a></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<div class="boughtDiv">
				<div class="orderType">
					<div class="selectedOrderType">
						<a orderStatus="all" href="#nowhere">所有订单</a>
					</div>
					<div>
						<a orderStatus="waitPay" href="#nowhere">待付款</a>
					</div>
					<div>
						<a orderStatus="waitDelivery" href="#nowhere">待发货</a>
					</div>
					<div>
						<a orderStatus="waitConfirm" href="#nowhere">待收货</a>
					</div>
					<div>
						<a orderStatus="waitReview" href="#nowhere" class="noRightborder">待评价</a>
					</div>
					<div class="orderTypeLastOne">
						<a class="noRightborder">&nbsp;</a>
					</div>
				</div>
				<div style="clear: both"></div>
				<div class="orderListTitle">
					<table class="orderListTitleTable">
						<tr>
							<td>宝贝</td>
							<td width="100px">单价</td>
							<td width="100px">数量</td>
							<td width="120px">实付款</td>
							<td width="100px">交易操作</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
	</div>

</div>