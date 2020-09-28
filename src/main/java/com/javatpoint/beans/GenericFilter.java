package com.javatpoint.beans;   
import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.WebServlet;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;  
import org.springframework.web.bind.annotation.PathVariable;  
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RequestMethod;   
import com.javatpoint.beans.user;  
import com.javatpoint.dao.UserDao;  
import org.apache.log4j.Logger;  
import com.javatpoint.controllers.CsrfToken;
import com.javatpoint.controllers.EmpController;

@Component
@Order(1)
public class GenericFilter extends HttpFilter{
    
	private Logger logger = Logger.getLogger(GenericFilter.class);
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws IOException, ServletException {
       // log.info("New request at date: {}", new Date());
    	logger.info("Entered into the filter class");

        chain.doFilter(request, response);
        return;
    }

}