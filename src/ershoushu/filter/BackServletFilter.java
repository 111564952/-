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
		uri =StringUtils.remove(uri, contextPath);//uri=admin_category_list
		if (uri.startsWith("/admin_")) {//如果以“/admin”开头
			String servletPath = StringUtils.substringBetween(uri, "_", "_")+ "Servlet";//取出两个“——”之间的category
			String method = StringUtils.substringAfterLast(uri,"_" );//取出方法名list
            request.setAttribute("method", method);//将list字符串存入"method"
			req.getRequestDispatcher("/" + servletPath).forward(request, response);//拼接字符串“/categoryServlet”跳转
			return;
		}
		chain.doFilter(request, response);//否则就chain.doFilter将请求转发给过滤器链下一个filter 
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
		
	}

}
