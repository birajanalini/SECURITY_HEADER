package com.javatpoint.controllers;   
// Basic Java
import java.util.List;
import org.apache.log4j.Logger;  
import java.lang.*;
import java.sql.SQLException;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.FilterChain;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.RequestWrapper;
// Spring FrameWork
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;  
import org.springframework.web.bind.annotation.PathVariable;  
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RequestMethod;   
import org.springframework.web.bind.annotation.CrossOrigin;
//Application Related
import com.javatpoint.beans.user;

import com.javatpoint.dao.UserDao;  
import org.apache.log4j.Logger;
@Controller  
@CrossOrigin(origins ="http://172.168.15.173:8080")
//@CrossOrigin(origins ="*")
public class LoginServlet extends HttpServlet {  
    private static final FilterChain FilterChain = null;
	@Autowired  
    UserDao dao1; //will inject dao from xml file  
	private Logger logger = Logger.getLogger(LoginServlet.class);
	CsrfToken CsrfToken;
	
	

    @RequestMapping("/LoginServlet")  
    protected void doPost(HttpServletRequest request, HttpServletResponse response, Model m)
    throws ServletException, IOException, SQLException{  
        PrintWriter out=response.getWriter();  
        
      
        
		String uri = request.getScheme() + "://" +   // "http" + "://
		request.getServerName() +       // "myhost"
		":" +                           // ":"
		request.getServerPort() +       // "8080"
		request.getContextPath();
logger.info("uri is "+uri);
logger.info("testing1");

        HttpSession sessionsxist = request.getSession(false);
        String name="";
		
		

        if (sessionsxist != null)
        {
        	 name = (String)sessionsxist.getAttribute("sessionuser");
        	 //out.print("place1");
         	//System.out.println("name "+name);
             if (name != null)
             {
            	// out.print("place2");
	             
				 //System.out.println("session exist");
				 logger.info("testing2");
				 //response.setHeader("Access-Control-Allow-Origin", "*");
	        	 response.sendRedirect(uri);
             }
             else
             {
            	 String UserEmail = request.getParameter("username");
            	// out.print("UserEmail "+UserEmail);
            	 
            	 if (UserEmail != null)
            	 {

					//check csrf token first 
					
				logger.info("fetch token "+request.getParameter("FormToken").toString());
					//Boolean ValidationFlag= dao1.csrfTokenValidate(request,"formlogincsrfToken", request.getParameter("FormToken").toString());
					String ValidationFlag= dao1.csrfTokenValidate(request, "formlogincsrfToken", request.getParameter("FormToken").toString());
					if(ValidationFlag=="false")
					{
						request.setAttribute("Errormsg","Security Violation");
						request.setAttribute("csrfToken", dao1.GenToken(request, "formlogincsrfToken"));
						//response.addHeader("Access-Control-Allow-Origin", "*");
						request.getRequestDispatcher("/jsp/login.jsp").include(request, response);

					}
					else
					{

						logger.info("validated with csrf");
				
    			             user udetails=dao1.getUserDetails(UserEmail);  
    			            // out.print("udetails "+udetails);
    			             if (udetails == null)
    			             { 
    			            	//out.print("place3");
								 //out.print("Invalid credentials");
								 
								 request.setAttribute("Errormsg","Invalid Credentials");
								 request.setAttribute("csrfToken", dao1.GenToken(request, "formlogincsrfToken"));
								 //response.addHeader("Access-Control-Allow-Origin", "*");
    			                 request.getRequestDispatcher("/jsp/login.jsp").include(request, response);  
    			                 out.close();
    			             	
    			             	
    			             }
    			             else
    			             {
    			            	 //out.print("place4");
    			            	 String Fetchuser = udetails.getUserName();
    				             String FetchPass = udetails.getPassword();
    				             String UserPass = request.getParameter("pass").toString();  
    				
    				     		        
    				     		if (UserPass.equals(FetchPass))
    				     		{
    				     			// out.print("successfull login");
    				                HttpSession session = request.getSession();
    				                session.setAttribute("sessionuser", Fetchuser);
									session.setAttribute("sessionuserId", udetails.getId());
									logger.info("testing3");
									// response.setHeader("Access-Control-Allow-Origin", "*");
    				                 response.sendRedirect(uri);
    				              //  request.getRequestDispatcher("/").include(request,response);
    				
    				     		}
    				     		else
    				     		{
    				     			 //out.print("place6");
    				                // out.print("Invalid Password");
									 sessionsxist.invalidate();
									 request.setAttribute("Errormsg","Invalid Credentials");
									 request.setAttribute("csrfToken", dao1.GenToken(request, "formlogincsrfToken"));
									 //response.addHeader("Access-Control-Allow-Origin", "*");
    				                 request.getRequestDispatcher("/jsp/login.jsp").include(request, response);  
    				               out.close();
    				     		}
							 }
					}		 
    			            
            	 }  	
            	 else
            	 {
            		 //out.print("place7");
            		   //  out.print("Please login first");
						 sessionsxist.invalidate();
						 request.setAttribute("Errormsg","Please login first");
						 request.setAttribute("csrfToken", dao1.GenToken(request, "formlogincsrfToken"));
						 //response.addHeader("Access-Control-Allow-Origin", "*");
    	                 request.getRequestDispatcher("/jsp/login.jsp").include(request, response);  
    	                 out.close();
            	 }
    			     		       

            	 
             }
        }
        else
        {
        	
        	 String UserEmail = request.getParameter("username");
        	 //out.print("UserEmail "+UserEmail);
        	 
        	 if (UserEmail != null)
        	 {

				//check csrf token first 
					
				logger.info("fetch token "+request.getParameter("FormToken").toString());
				//Boolean ValidationFlag= dao1.csrfTokenValidate(request, "formlogincsrfToken", request.getParameter("FormToken").toString());
				String ValidationFlag= dao1.csrfTokenValidate(request, "formlogincsrfToken", request.getParameter("FormToken").toString());
				if(ValidationFlag=="false")
				{
					request.setAttribute("Errormsg","Security Violation");
					request.setAttribute("csrfToken", dao1.GenToken(request, "formlogincsrfToken"));
					//response.addHeader("Access-Control-Allow-Origin", "*");
					request.getRequestDispatcher("/jsp/login.jsp").include(request, response);

				}
				else
				{
					logger.info("validated with csrf");
			             user udetails=dao1.getUserDetails(UserEmail);  
			           //  out.print("udetails "+udetails);
			             if (udetails == null)
			             { 
			            	// out.print("place3 1");
						    //out.print("Invalid credentials");
							request.setAttribute("Errormsg","Invalid Credentials");
							request.setAttribute("csrfToken", dao1.GenToken(request, "formlogincsrfToken"));
							//response.addHeader("Access-Control-Allow-Origin", "*");
			                 request.getRequestDispatcher("/jsp/login.jsp").include(request, response);  
			                 out.close();
			             	
			             	
			             }
			             else
			             {
			            	// out.print("place4");
			            	 String Fetchuser = udetails.getUserName();
				             String FetchPass = udetails.getPassword();
				             String UserPass = request.getParameter("pass").toString();  
				
				     		        
				     		if (UserPass.equals(FetchPass))
				     		{
				     			// out.print("place5");
				                HttpSession session = request.getSession();
				                session.setAttribute("sessionuser", Fetchuser);
								session.setAttribute("sessionuserId", udetails.getId());
								logger.info("testing4");
								 //response.setHeader("Access-Control-Allow-Origin", "*");
				                 response.sendRedirect(uri);
				              //  request.getRequestDispatcher("/").include(request,response);
				
				     		}
				     		else
				     		{
				     			// out.print("place6");
				                // out.print("Invalid credentials");
								// sessionsxist.invalidate();
				     			 request.setAttribute("Errormsg","Invalid Credentials");
								request.setAttribute("csrfToken", dao1.GenToken(request, "formlogincsrfToken"));
								
				                 request.getRequestDispatcher("/jsp/login.jsp").include(request, response);  
				               out.close();
				     		}
						 }
						 
				}		 
			            
        	 }  	
        	 else
        	 {
        		 //out.print("place7");
        		   //  out.print("Please login first");
					 //sessionsxist.invalidate();
        		 	 request.setAttribute("Errormsg","Please login first");
					 request.setAttribute("csrfToken", dao1.GenToken(request, "formlogincsrfToken"));
					 //response.addHeader("Access-Control-Allow-Origin", "*");
	                 request.getRequestDispatcher("/jsp/login.jsp").include(request, response);  
	                 out.close();
        	 }
			     		       
			
			         
           
        }

        //out.print("place8");
       

	}  
	
	@RequestMapping(value="/Register", method=RequestMethod.POST)

	public void Register(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("usr") user usr)throws ServletException, IOException, SQLException 

{
	
	usr.setUserName(request.getParameter("fullname").toString());
	usr.setEmail(request.getParameter("email").toString());
	usr.setPhone(request.getParameter("co.code").toString()+request.getParameter("phnumber").toString());
	usr.setDesignation(request.getParameter("designation").toString());
	usr.setPassword(request.getParameter("password").toString());



		dao1.registerUser(usr);  
		
		//m.addAttribute("msg","Registered");
	   // return "redirect:../BIRAJA/jsp/Register.jsp";//will redirect to login page
	 //  return "Register";
		request.setAttribute("Successmsg","You have successfully registered, login here");
		
	//	request.getRequestDispatcher("/jsp/Register.jsp").include(request, response);  
	request.setAttribute("csrfToken", dao1.GenToken(request, "formlogincsrfToken"));
	//response.addHeader("Access-Control-Allow-Origin", "*");
		request.getRequestDispatcher("/jsp/login.jsp").include(request, response); 
	

}
	
	@RequestMapping(value="/Logout")
	
	public void Logout(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("usr") user usr)throws ServletException, IOException, SQLException 

	{
		HttpSession sessionsxist = request.getSession(false);
		 sessionsxist.invalidate();
		 request.setAttribute("Successmsg","Thanks for using TEMENOS");
		 request.setAttribute("csrfToken", dao1.GenToken(request, "formlogincsrfToken"));
		 //response.addHeader("Access-Control-Allow-Origin", "*");
		 request.getRequestDispatcher("/jsp/login.jsp").include(request, response); 
	}


}  