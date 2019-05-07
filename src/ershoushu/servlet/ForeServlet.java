package ershoushu.servlet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.web.util.HtmlUtils;

import ershoushu.bean.Category;
import ershoushu.bean.Order;
import ershoushu.bean.OrderItem;
import ershoushu.bean.Product;
import ershoushu.bean.ProductImage;
import ershoushu.bean.PropertyValue;
import ershoushu.bean.Review;
import ershoushu.bean.User;
import ershoushu.comparator.ProductAllComparator;
import ershoushu.comparator.ProductDateComparator;
import ershoushu.comparator.ProductPriceComparator;
import ershoushu.comparator.ProductReviewComparator;
import ershoushu.comparator.ProductSaleCountComparator;
import ershoushu.dao.CategoryDAO;
import ershoushu.dao.OrderDAO;
import ershoushu.dao.OrderItemDAO;
import ershoushu.dao.ProductDAO;
import ershoushu.dao.ProductImageDAO;
import ershoushu.util.Page;

/**
 * Servlet implementation class ForeServlet
 */
public class ForeServlet extends BaseForeServlet {
	private static final long serialVersionUID = 1L;

	public String home(HttpServletRequest request, HttpServletResponse response, Page page) {
		List<Category> cs = new CategoryDAO().list();
		new ProductDAO().fill(cs);
		new ProductDAO().fillByRow(cs);
		request.setAttribute("cs", cs);
		return "home.jsp";
	}

	public String register(HttpServletRequest request, HttpServletResponse response, Page page) {

		String name = request.getParameter("name");
		String password = request.getParameter("password");
		name = HtmlUtils.htmlEscape(name);
		System.out.println(name);
		boolean exist = userDAO.isExist(name);

		if (exist) {
			request.setAttribute("msg", "用户名已经被使用,不能使用");
			return "register.jsp";
		}

		User user = new User();
		user.setName(name);
		user.setPassword(password);
		System.out.println(user.getName());
		System.out.println(user.getPassword());
		userDAO.add(user);

		return "@registerSuccess.jsp";
	}

	public String login(HttpServletRequest request, HttpServletResponse response, Page page) {
		String name = request.getParameter("name");
		name = HtmlUtils.htmlEscape(name);
		String password = request.getParameter("password");

		User user = userDAO.get(name, password);

		if (null == user) {
			request.setAttribute("msg", "账号密码错误");
			return "login.jsp";
		}
		request.getSession().setAttribute("user", user);
		return "@forehome";
	}

	public String logout(HttpServletRequest request, HttpServletResponse response, Page page) {
		request.getSession().removeAttribute("user");
		return "@forehome";
	}

	public String product(HttpServletRequest request, HttpServletResponse response, Page page) {
		int pid = Integer.parseInt(request.getParameter("pid"));
		Product p = productDAO.get(pid);
		// 根据对象p，获取这个产品对应的单个图片集合
		// 根据对象p，获取这个产品对应的详情图片集合
		List<ProductImage> productSingleImages = productImageDAO.list(p, ProductImageDAO.type_single);
		List<ProductImage> productDetailImages = productImageDAO.list(p, ProductImageDAO.type_detail);
		p.setProductSingleImages(productSingleImages);
		p.setProductDetailImages(productDetailImages);
		// 获取产品的所有属性值
		List<PropertyValue> pvs = propertyValueDAO.list(p.getId());
		// 获取产品对应的所有的评价
		List<Review> reviews = reviewDAO.list(p.getId());
		// 设置产品的销量和评价数量
		productDAO.setSaleAndReviewNumber(p);
		// 把上述取值放在request属性上
		request.setAttribute("reviews", reviews);

		request.setAttribute("p", p);
		request.setAttribute("pvs", pvs);
		return "product.jsp";
	}

	public String checkLogin(HttpServletRequest request, HttpServletResponse response, Page page) {
		User user = (User) request.getSession().getAttribute("user");
		if (null != user)
			return "%success";
		return "%fail";
	}

	// ajax异步传输用户登录判断
	public String loginAjax(HttpServletRequest request, HttpServletResponse response, Page page) {
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		User user = userDAO.get(name, password);

		if (null == user) {
			return "%fail";
		}
		request.getSession().setAttribute("user", user);
		return "%success";
	}

	// 排序商品栏
	public String category(HttpServletRequest request, HttpServletResponse response, Page page) {
		int cid = Integer.parseInt(request.getParameter("cid"));
		Category c = categoryDAO.get(cid);
		new ProductDAO().fill(c);// 获得分类商品
		new ProductDAO().setSaleAndReviewNumber(c.getProducts());// 获得商品评价，销量

		String sort = request.getParameter("sort");
		if (null != sort) {
			switch (sort) {
			case "review":
				Collections.sort(c.getProducts(), new ProductReviewComparator());
				break;
			case "date":
				Collections.sort(c.getProducts(), new ProductDateComparator());
				break;

			case "saleCount":
				Collections.sort(c.getProducts(), new ProductSaleCountComparator());
				break;

			case "price":
				Collections.sort(c.getProducts(), new ProductPriceComparator());
				break;

			case "all":
				Collections.sort(c.getProducts(), new ProductAllComparator());
				break;
			}
		}
		request.setAttribute("c", c);
		return "category.jsp";
	}

	// 搜索栏方法
	public String search(HttpServletRequest request, HttpServletResponse response, Page page) {
		String keyword = request.getParameter("keyword");
		List<Product> ps = new ProductDAO().search(keyword, 0, 20);// 搜索包含keyword的前二十个商品
		productDAO.setSaleAndReviewNumber(ps);
		request.setAttribute("ps", ps);
		return "searchResult.jsp";
	}

	public String buyone(HttpServletRequest request, HttpServletResponse response, Page page) {
		int pid = Integer.parseInt(request.getParameter("pid"));// 获取参数pid
		int num = Integer.parseInt(request.getParameter("num"));// 获取参数num
		Product p = productDAO.get(pid);// 根据pid获取产品对象p
		int oiid = 0;

		User user = (User) request.getSession().getAttribute("user");// 从session中获取用户对象user
		boolean found = false;
		/* 新增订单项OrderItem */
		// 如果已经存在这个产品对应的OrderItem，并且还没有生成订单，即还在购物车中。 那么就应该在对应的OrderItem基础上，调整数量
		List<OrderItem> ois = orderItemDAO.listByUser(user.getId());// 基于用户对象user，查询没有生成订单的订单项集合
		// 遍历这个集合
		for (OrderItem oi : ois) {
			if (oi.getProduct().getId() == p.getId()) {// 如果产品是一样的话，就进行数量追加
				oi.setNumber(oi.getNumber() + num);
				orderItemDAO.update(oi);
				found = true;
				oiid = oi.getId();// 获取这个订单项的 id
				break;
			}
		}
		if (!found) {// 如果不存在对应的OrderItem,那么就新增一个订单项OrderItem
			OrderItem oi = new OrderItem();// 生成新的订单项
			oi.setUser(user);// 设置数量，用户和产品
			oi.setNumber(num);
			oi.setProduct(p);
			orderItemDAO.add(oi);// 插入到数据库
			oiid = oi.getId();// 获取这个订单项的 id
		}
		return "@forebuy?oiid=" + oiid;// 基于这个订单项id客户端跳转到结算页面/forebuy
	}

//	1. 通过getParameterValues获取参数oiid
//	为什么这里要用getParameterValues试图获取多个oiid，而不是getParameter仅仅获取一个oiid? 
//	因为根据购物流程环节与表关系，结算页面还需要显示在购物车中选中的多条OrderItem数据，所以为了兼容从购物车页面跳转过来的需求，
//	要用getParameterValues获取多个oiid
//	2. 准备一个泛型是OrderItem的集合ois
//	3. 根据前面步骤获取的oiids，从数据库中取出OrderItem对象，并放入ois集合中
//	4. 累计这些ois的价格总数，赋值在total上
//	5. 把订单项集合放在session的属性 "ois" 上
//	6. 把总价格放在 request的属性 "total" 上
//	7. 服务端跳转到buy.jsp
	public String buy(HttpServletRequest request, HttpServletResponse response, Page page) {
		String[] oiids = request.getParameterValues("oiid");
		List<OrderItem> ois = new ArrayList<OrderItem>();
		float total = 0;

		for (String strid : oiids) {
			int oiid = Integer.parseInt(strid);
			OrderItem oi = orderItemDAO.get(oiid);
			total += oi.getProduct().getPromotePrice() * oi.getNumber();
			ois.add(oi);
		}
		request.getSession().setAttribute("ois", ois);
		request.setAttribute("total", total);
		return "buy.jsp";
	}

//	上一步访问地址/foreaddCart导致ForeServlet.addCart()方法被调用
//	addCart()方法和立即购买中的 ForeServlet.buyone()步骤做的事情是一样的，区别在于返回不一样
//	1. 获取参数pid
//	2. 获取参数num
//	3. 根据pid获取产品对象p
//	4. 从session中获取用户对象user
//
//	接下来就是新增订单项OrderItem， 新增订单项要考虑两个情况
//	a. 如果已经存在这个产品对应的OrderItem，并且还没有生成订单，即还在购物车中。 那么就应该在对应的OrderItem基础上，调整数量
//	a.1 基于用户对象user，查询没有生成订单的订单项集合
//	a.2 遍历这个集合
//	a.3 如果产品是一样的话，就进行数量追加
//	a.4 获取这个订单项的 id
//
//	b. 如果不存在对应的OrderItem,那么就新增一个订单项OrderItem
//	b.1 生成新的订单项
//	b.2 设置数量，用户和产品
//	b.3 插入到数据库
//	b.4 获取这个订单项的 id
//
//	与ForeServlet.buyone() 客户端跳转到结算页面不同的是， 最后返回字符串"success"，表示添加成功
	public String addCart(HttpServletRequest request, HttpServletResponse response, Page page) {
		int pid = Integer.parseInt(request.getParameter("pid"));
		Product p = productDAO.get(pid);
		int num = Integer.parseInt(request.getParameter("num"));

		User user = (User) request.getSession().getAttribute("user");
		boolean found = false;

		List<OrderItem> ois = orderItemDAO.listByUser(user.getId());
		for (OrderItem oi : ois) {
			if (oi.getProduct().getId() == p.getId()) {
				oi.setNumber(oi.getNumber() + num);
				orderItemDAO.update(oi);
				found = true;
				break;
			}
		}

		if (!found) {
			OrderItem oi = new OrderItem();
			oi.setUser(user);
			oi.setNumber(num);
			oi.setProduct(p);
			orderItemDAO.add(oi);
		}
		return "%success";
	}

//	访问地址/forecart导致ForeServlet.cart()方法被调用
//	1. 通过session获取当前用户
//	所以一定要登录才访问，否则拿不到用户对象
//	2. 获取被这个用户关联的订单项集合 ois
//	3. 把ois放在request中
//	4. 服务端跳转到cart.jsp
	public String cart(HttpServletRequest request, HttpServletResponse response, Page page) {
		User user = (User) request.getSession().getAttribute("user");
		List<OrderItem> ois = orderItemDAO.listByUser(user.getId());
		request.setAttribute("ois", ois);
		return "cart.jsp";
	}

//	点击增加或者减少按钮后，根据 cartPage.jsp 中的js代码，会通过Ajax访问/forechangeOrderItem路径，导致ForeServlet.changeOrderItem()方法被调用
//	1. 判断用户是否登录
//	2. 获取pid和number
//	3. 遍历出用户当前所有的未生成订单的OrderItem
//	4. 根据pid找到匹配的OrderItem，并修改数量后更新到数据库
//	5. 返回字符串"success"
	public String changeOrderItem(HttpServletRequest request, HttpServletResponse response, Page page) {
		User user = (User) request.getSession().getAttribute("user");
		if (null == user)
			return "%fail";

		int pid = Integer.parseInt(request.getParameter("pid"));
		int number = Integer.parseInt(request.getParameter("number"));
		List<OrderItem> ois = orderItemDAO.listByUser(user.getId());
		for (OrderItem oi : ois) {
			if (oi.getProduct().getId() == pid) {
				oi.setNumber(number);
				orderItemDAO.update(oi);
				break;
			}

		}
		return "%success";
	}

//	点击删除按钮后，根据 cartPage.jsp 中的js代码，会通过Ajax访问/foredeleteOrderItem路径，导致ForeServlet.deleteOrderItem方法被调用
//	1. 判断用户是否登录
//	2. 获取oiid
//	3. 删除oiid对应的OrderItem数据
//	4. 返回字符串"success"
	public String deleteOrderItem(HttpServletRequest request, HttpServletResponse response, Page page) {
		User user = (User) request.getSession().getAttribute("user");
		if (null == user)
			return "%fail";
		int oiid = Integer.parseInt(request.getParameter("oiid"));
		orderItemDAO.delete(oiid);
		return "%success";
	}

//	提交订单访问路径 /forecreateOrder, 导致ForeServlet.createOrder 方法被调用
//	1. 从session中获取user对象
//	2. 获取地址，邮编，收货人，用户留言等信息
//	3. 根据当前时间加上一个4位随机数生成订单号
//	4. 根据上述参数，创建订单对象
//	5. 把订单状态设置为等待支付
//	6. 加入到数据库
//	7. 从session中获取订单项集合 ( 在结算功能的ForeServlet.buy() 13行，订单项集合被放到了session中 )
//	8. 遍历订单项集合，设置每个订单项的order，并更新到数据库
//	9. 统计本次订单的总金额
//	10. 客户端跳转到确认支付页forealipay，并带上订单id和总金额
	public String createOrder(HttpServletRequest request, HttpServletResponse response, Page page) {
		User user = (User) request.getSession().getAttribute("user");
		List<OrderItem> ois = (List<OrderItem>) request.getSession().getAttribute("ois");
		if (ois.isEmpty())
			return "@login.jsp";

		String address = request.getParameter("address");
		String post = request.getParameter("post");
		String receiver = request.getParameter("receiver");
		String mobile = request.getParameter("mobile");
		String userMessage = request.getParameter("userMessage");

		Order order = new Order();
		String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + RandomUtils.nextInt(10000);

		order.setOrderCode(orderCode);
		order.setAddress(address);
		order.setPost(post);
		order.setReceiver(receiver);
		order.setMobile(mobile);
		order.setUserMessage(userMessage);
		order.setCreateDate(new Date());
		order.setUser(user);
		order.setStatus(OrderDAO.waitPay);

		orderDAO.add(order);
		float total = 0;
		for (OrderItem oi : ois) {
			oi.setOrder(order);
			orderItemDAO.update(oi);
			total += oi.getProduct().getPromotePrice() * oi.getNumber();
		}
		return "@forealipay?oid=" + order.getId() + "&total=" + total;
	}

	public String alipay(HttpServletRequest request, HttpServletResponse response, Page page) {
		return "alipay.jsp";
	}

	public String payed(HttpServletRequest request, HttpServletResponse response, Page page) {
		int oid = Integer.parseInt(request.getParameter("oid"));
		Order order = orderDAO.get(oid);
		order.setStatus(OrderDAO.waitDelivery);
		order.setPayDate(new Date());
		new OrderDAO().update(order);
		request.setAttribute("o", order);
		return "payed.jsp";
	}

	public String bought(HttpServletRequest request, HttpServletResponse response, Page page) {
		User user = (User) request.getSession().getAttribute("user");
		List<Order> os = orderDAO.list(user.getId(), OrderDAO.delete);

		orderItemDAO.fill(os);

		request.setAttribute("os", os);

		return "bought.jsp";
	}

	public String confirmPay(HttpServletRequest request, HttpServletResponse response, Page page) {
		int oid = Integer.parseInt(request.getParameter("oid"));
		Order o = orderDAO.get(oid);
		orderItemDAO.fill(o);
		request.setAttribute("o", o);
		return "confirmPay.jsp";
	}

	public String orderConfirmed(HttpServletRequest request, HttpServletResponse response, Page page) {
		int oid = Integer.parseInt(request.getParameter("oid"));
		Order o = orderDAO.get(oid);
		o.setStatus(OrderDAO.waitReview);
		o.setConfirmDate(new Date());
		orderDAO.update(o);
		return "orderConfirmed.jsp";
	}

	public String deleteOrder(HttpServletRequest request, HttpServletResponse response, Page page) {
		int oid = Integer.parseInt(request.getParameter("oid"));
		Order o = orderDAO.get(oid);
		o.setStatus(OrderDAO.delete);
		orderDAO.update(o);
		return "%success";
	}

//	通过点击评价按钮，来到路径/forereview，导致ForeServlet.review()方法被调用
//	1. ForeServlet.review()
//	1.1 获取参数oid
//	1.2 根据oid获取订单对象o
//	1.3 为订单对象填充订单项
//	1.4 获取第一个订单项对应的产品,因为在评价页面需要显示一个产品图片，那么就使用这第一个产品的图片了
//	1.5 获取这个产品的评价集合
//	1.6 为产品设置评价数量和销量
//	1.7 把产品，订单和评价集合放在request上
//	1.8 服务端跳转到 review.jsp
	public String review(HttpServletRequest request, HttpServletResponse response, Page page) {
		int oid = Integer.parseInt(request.getParameter("oid"));
		Order o = orderDAO.get(oid);
		orderItemDAO.fill(o);
		Product p = o.getOrderItems().get(0).getProduct();
		List<Review> reviews = reviewDAO.list(p.getId());
		productDAO.setSaleAndReviewNumber(p);
		request.setAttribute("p", p);
		request.setAttribute("o", o);
		request.setAttribute("reviews", reviews);
		return "review.jsp";
	}

//	在评价产品页面点击提交评价，就把数据提交到了/foredoreview路径，导致ForeServlet.doreview方法被调用
//	1. ForeServlet.doreview()
//	1.1 获取参数oid
//	1.2 根据oid获取订单对象o
//	1.3 修改订单对象状态
//	1.4 更新订单对象到数据库
//	1.5 获取参数pid
//	1.6 根据pid获取产品对象
//	1.7 获取参数content (评价信息)
//	1.8 对评价信息进行转义，道理同注册ForeServlet.register()
//	1.9 从session中获取当前用户
//	1.10 创建评价对象review
//	1.11 为评价对象review设置 评价信息，产品，时间，用户
//	1.12 增加到数据库
//	1.13.客户端跳转到/forereview： 评价产品页面，并带上参数showonly=true
//	2. reviewPage.jsp
//	在reviewPage.jsp中，当参数showonly==true，那么就显示当前产品的所有评价信息
	public String doreview(HttpServletRequest request, HttpServletResponse response, Page page) {
		int oid = Integer.parseInt(request.getParameter("oid"));
		Order o = orderDAO.get(oid);
		o.setStatus(OrderDAO.finish);
		orderDAO.update(o);
		int pid = Integer.parseInt(request.getParameter("pid"));
		Product p = productDAO.get(pid);
		String content = request.getParameter("content");
		content = HtmlUtils.htmlEscape(content);
		User user = (User) request.getSession().getAttribute("user");
		Review review = new Review();
		review.setContent(content);
		review.setProduct(p);
		review.setCreateDate(new Date());
		review.setUser(user);
		reviewDAO.add(review);

		return "@forereview?oid="+oid+"&showonly=true";
	}
}
