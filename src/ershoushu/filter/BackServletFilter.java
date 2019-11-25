package ershoushu.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
/**
 * 
 * @author 11156
 *
 */
public class BackServletFilter implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request=(HttpServletRequest) req;
		HttpServletResponse response=(HttpServletResponse) res;
		
		String contextPath=request.getContextPath();//是为了解决相对路径的问题，可返回站点的根路径。/ershoushu
		String uri=request.getRequestURI();//取出访问的uri: /ershoushu/admin_category_list
		System.out.println(uri);
		uri =StringUtils.remove(uri, contextPath);//uri=admin_category_list
		System.out.println(contextPath);
		System.out.println(uri);
		System.out.println("进入管理员1");
		if (uri.startsWith("/admin_")) {//如果以“/admin”开头
			System.out.println(uri);
			String servletPath = StringUtils.substringBetween(uri, "_", "_")+ "Servlet";//取出两个“——”之间的category
			System.out.println(servletPath);
			String method = StringUtils.substringAfterLast(uri,"_" );//取出方法名list
			System.out.println(method);
            request.setAttribute("method", method);//将list字符串存入"method"
            System.out.println("结束前");
			req.getRequestDispatcher("/" + servletPath).forward(request, response);//拼接字符串“/categoryServlet”跳转
			System.out.println("结束");
			return;
		}
		System.out.println("进入管理员");
		chain.doFilter(request, response);//否则就chain.doFilter将请求转发给过滤器链下一个filter 
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
		
	}

}
