package ershoushu.servlet;

import java.util.List;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ershoushu.util.Page;
import ershoushu.bean.*;
/**
 * Servlet implementation class UserServlet
 */

public class UserServlet extends BaseBackServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public String add(HttpServletRequest request, HttpServletResponse response, Page page) {

		return null;
	}

	@Override
	public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
	
		return null;
	}

	@Override
	public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
		
		return null;
	}

	@Override
	public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
		
		return null;
	}

	@Override
	public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
		List<User> us = userDAO.list(page.getStart(),page.getCount());
		int totle=userDAO.getTotal();
		page.setTotal(totle);
		
		request.setAttribute("us", us);
        request.setAttribute("page", page);
         
        return "admin/listUser.jsp";
	}

	
}
