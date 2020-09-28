package com.javatpoint.interceptor;   

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.errors.IntrusionException;
import org.owasp.esapi.errors.ValidationException;
import org.owasp.esapi.filters.SecurityWrapperRequest;
import com.javatpoint.dao.UserDao; 



import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;  
import org.springframework.web.bind.annotation.PathVariable;  
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RequestMethod;   
import org.springframework.web.bind.annotation.CrossOrigin;
public class LoginServletInterceptor implements HandlerInterceptor{
	@Autowired  
	UserDao dao1;
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		System.out.println("Pre-handle");
		
		
		if ("POST".equalsIgnoreCase(request.getMethod())) 
		{
			 System.out.println("Form was submitted.");
			 
			 

			    Enumeration requestParameters = request.getParameterNames();
			    System.out.println("requestParameters "+requestParameters);
			    HttpSession sessionsxist = request.getSession(false);

					    while (requestParameters.hasMoreElements()) 
					    {
					        String element = (String) requestParameters.nextElement();
					        String value = request.getParameter(element);
		
							        if (element != null && value != null) {
							        	System.out.println("param Name : " + element + " value: " + value);
				
							        }
							     
							        
							    	try
									{
							    		
											ESAPI.validator().getValidInput("HTTP parameter value: " + value, replaceCharForValidation(value), "uiParametersValidation", 150, true);
											  if (value.toLowerCase().contains("evalscript")) {
									              System.out.println("Vulnerable characters found in value of field : " + value);
									            }
											System.out.println("everything goes fine");
									}catch (ValidationException e) {
										
										System.out.println("Vulnerable characters found in ParameterMap for " +element);
										
										//request.setAttribute("csrfToken", dao1.GenToken(request, "formlogincsrfToken"));
										sessionsxist.invalidate();
										request.setAttribute("Errormsg","Vulnerable characters found in ParameterMap");
									
										request.getRequestDispatcher("/jsp/error.jsp").include(request, response);
								        return false;
								      } catch (IntrusionException e) {
								    	  
								    	  System.out.println("Error found at encoding stage in ParameterMap for " +element);
								    	  sessionsxist.invalidate();
								    	  request.setAttribute("Errormsg","Error found at encoding stage in ParameterMap.");
											//request.setAttribute("csrfToken", dao1.GenToken(request, "formlogincsrfToken"));
								    	  request.getRequestDispatcher("/jsp/error.jsp").include(request, response);
								    	  return false;
								      } 
									
									
									
							        
							        
							        
					    }
					    //return true;

			
		} 
		else {
			 System.out.println("It may be a GET request");
			// return true;
		}
		//response.setHeader("Content-Security-Policy", "default-src 'self'; img-src 'self'; style-src 'self' 'unsafe-inline'; font-src 'self'; script-src 'self' 'unsafe-inline'; connect-src 'self';");
		return true;
		
		
		
		
	}
	
	
	public void postHandle(HttpServletRequest request,	HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		System.out.println("Post-handle");
	}
	

	public void afterCompletion(HttpServletRequest request,	HttpServletResponse response, Object handler, Exception ex)	throws Exception {
		System.out.println("After completion handle");
		
		 response.setHeader("Access-Control-Allow-Origin", "*");
		
	}
	public String replaceCharForValidation(String paramValue) {
	    paramValue = paramValue.replaceAll("%", "~Per#");
	    paramValue = paramValue.replaceAll("\\\\", "/");
	    return paramValue;
	  }

}