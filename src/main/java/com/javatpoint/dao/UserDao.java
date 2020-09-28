package com.javatpoint.dao;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;  
import java.util.List;  
import org.springframework.jdbc.core.BeanPropertyRowMapper;  
import org.springframework.jdbc.core.JdbcTemplate;  
import org.springframework.jdbc.core.RowMapper;  
import com.javatpoint.beans.user;  

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.WebServlet;
import org.apache.log4j.Logger;  
public class UserDao extends HttpServlet {  
JdbcTemplate template;  
private Logger logger = Logger.getLogger(UserDao.class);
  
public void setTemplate(JdbcTemplate template) {  
    this.template = template;  
}  

public String getPassword(String email) throws SQLException{  
    ResultSet[] fetchdata;
   String sql="SELECT UserName, Password FROM `user` WHERE Email=?";  
    fetchdata =  template.queryForObject(sql, new Object[]{email},ResultSet[].class);
    //String fetchpass = fetchdata.getString(1)+" "+fetchdata.getString(2);
    String fetchpass = fetchdata[1]+" "+fetchdata[2];
   
return fetchpass;
	
}  

public user getUserDetails(String email){ 

        try {
                    
        String sql="select * from user where Email=?";  
        return template.queryForObject(sql, new Object[]{email},new BeanPropertyRowMapper<user>(user.class));
          
        }
                
        catch(Exception ex) {
            
            return null;


        }

}

public int registerUser(user usr)
{
    String sql="insert into user(UserName,Email,Password,Phone,Designation) values('"+usr.getUserName()+"','"+usr.getEmail()+"','"+usr.getPassword()+"','"+usr.getPhone()+"','"+usr.getDesignation()+"')";  
 
   return template.update(sql);  
}

public user getUserById(int Id)
{
	  String sql="select * from user where id=?";  
	   return template.queryForObject(sql, new Object[]{Id},new BeanPropertyRowMapper<user>(user.class));

}


public int UpdateProfile(user usr, int sessionuserId){  
    String sql="update user set UserName='"+usr.getUserName()+"', Email='"+usr.getEmail()+"',Phone='"+usr.getPhone()+"',Designation='"+usr.getDesignation()+"' where id='"+sessionuserId+"'";  
    return template.update(sql);  
}  

public String GenToken(HttpServletRequest request, String TokenName)
    {
        HttpSession session = request.getSession(false);
        
        
        String TokenValue="1q2w3e4r"; 
        String STokenValue="";
        if(TokenName.equals("formlogincsrfToken"))
        {
        	 STokenValue=TokenValue+TokenName;
        }
        else
        {
        	 STokenValue=TokenValue+TokenName+session.getId();
        }          
               
       HttpSession sessionsxist = request.getSession();
       sessionsxist.setAttribute(TokenName, STokenValue);
      
        return STokenValue;

    }

    public String csrfTokenValidate(HttpServletRequest request, String TokenName, String FormTokenValue)
    {       
        
       HttpSession sessionsxist = request.getSession(false);
       String TokenValue = sessionsxist.getAttribute(TokenName).toString();
        //String TokenValue= "1q2w3e4r"; 
        logger.info("csrf class FormTokenValue "+FormTokenValue);
        logger.info("csrf class TokenName "+TokenName);
        if(TokenName.equals("formlogincsrfToken"))
        {
        	FormTokenValue=FormTokenValue+TokenName;
        }
        else
        {
        	FormTokenValue=FormTokenValue+TokenName+sessionsxist.getId();
        }
        
        
           if(FormTokenValue.equals(FormTokenValue)) 
           {
               return "true";
           }else 
        	   
           {
               return "false";
           }
    }   

}  