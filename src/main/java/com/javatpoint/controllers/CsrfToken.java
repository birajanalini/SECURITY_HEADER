package com.javatpoint.controllers;   
import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.WebServlet;

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
import org.springframework.stereotype.Component;

public class CsrfToken {
    private Logger logger = Logger.getLogger(CsrfToken.class);
    public String setToken(HttpServletRequest request, String TokenName, String TokenValue)
    {
        
        HttpSession sessionsxist = request.getSession(false);
        sessionsxist.setAttribute("TokenName", TokenValue);
        return null;
    }
    public String getToken(HttpServletRequest request, String TokenName)
    {
       
         HttpSession sessionsxist = request.getSession(false);
        String TokenValue = sessionsxist.getAttribute("TokenName").toString();
        return TokenValue;
    }

    public String csrfTokenValidate(String TokenName, String FormTokenValue)
    {       
        
      //  HttpSession sessionsxist = request.getSession(false);
      //  String TokenValue = sessionsxist.getAttribute(TokenName).toString();
        String TokenValue= "1q2w3e4r"; 
        logger.info("csrf class FormTokenValue "+FormTokenValue);
        logger.info("csrf class TokenName "+TokenName);
        if(TokenName.equals("formlogincsrfToken"))
        {
        	//FormTokenValue=FormTokenValue+TokenName;
        }
        else
        {
        	//FormTokenValue=FormTokenValue+TokenName+"jesession";
        }
        
        
           if(FormTokenValue.equals(TokenValue)) 
           {
               return "true";
           }else 
        	   
           {
               return "false";
           }
    }

    public String GenToken(HttpServletRequest request, String TokenName)
    {
        
        String TokenValue="1q2w3e4r";  
      
             
               
        HttpSession sessionsxist = request.getSession(false);
        sessionsxist.setAttribute(TokenName, TokenValue);
        return TokenValue;

    }
    
   
    
    
}