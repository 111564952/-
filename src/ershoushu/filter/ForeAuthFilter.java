package ershoushu.filter;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import ershoushu.bean.User;

public class ForeAuthFilter implements Filter {

	
	public void destroy() {
		// TODO Auto-generated method stub
	}


	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String contextPath=request.getServletContext().getContextPath();
        
        String[] noNeedAuthPage = new String[]{//准备字符串数组 noNeedAuthPage，存放哪些不需要登录也能访问的路径
              
        		"homepage",
                "checkLogin",
                "register",
                "register2",
                "fondpassword",
                "fondpassword2",
                "loginAjax",
                "login",
                "product",
                "category",
                "search"};
        String uri = request.getRequestURI();//获取uri
        uri =StringUtils.remove(uri, contextPath);//去掉前缀/tmall
        if(uri.startsWith("/fore")&&!uri.startsWith("/foreServlet")){//如果访问的地址是/fore开头，又不是/foreServlet
            String method = StringUtils.substringAfterLast(uri,"/fore" );//取出fore后面的字符串，比如是forecart,那么就取出cart
            if(!Arrays.asList(noNeedAuthPage).contains(method)){// 判断cart是否是在noNeedAuthPage 
                User user =(User) request.getSession().getAttribute("user");//从session中取出"user"对象
                if(null==user){
                    response.sendRedirect("login.jsp");//如果不在，那么就需要进行是否登录验证
                    return;
                }
                // 否则就正常执行
            }
        }
        
		chain.doFilter(request, response);
	}

	public void init(FilterConfig fConfig) throws ServletException {
	
	}

}
