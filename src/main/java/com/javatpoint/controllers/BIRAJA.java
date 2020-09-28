package com.javatpoint.controllers;   
import java.util.List;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;  
import org.springframework.web.bind.annotation.PathVariable;  
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RequestMethod;   
import com.javatpoint.beans.user;  
import com.javatpoint.dao.UserDao;  
import org.apache.log4j.Logger;  
import com.javatpoint.controllers.CsrfToken;
@Controller  
@CrossOrigin(origins ="http://172.168.15.173:8080")

@WebServlet(
        name = "BIRAJA",
        description = "This is optional but helpful",
        urlPatterns = "/BIRAJA"
)
public class BIRAJA extends HttpServlet{  
    @Autowired  
    UserDao dao1;;//will inject dao from xml file
    CsrfToken CsrfToken;
    
    private Logger logger = Logger.getLogger(BIRAJA.class);  
      
    /*It displays a form to input data, here "command" is a reserved request attribute 
     *which is used to display object data into form 
     */  
      


    private boolean biraja(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException
    {
    	 HttpSession session = request.getSession(false);
    	 if (session!=null && (String)session.getAttribute("sessionuser") != null)
    	 {
    		 return true;
    	 }
    	 else
    	 {
    		 request.setAttribute("Errormsg","Please login first");
			 request.setAttribute("csrfToken", dao1.GenToken(request, "formlogincsrfToken"));
			
             request.getRequestDispatcher("/jsp/login.jsp").include(request, response);  
    		 return false;
    	 }
    	
    	
    }
    
    @RequestMapping("/")
    protected void doGet(HttpServletRequest request, HttpServletResponse response)  
                           throws ServletException, IOException {  
        response.setContentType("text/html"); 
       PrintWriter out=response.getWriter();  
        HttpSession session = request.getSession(false);
        String name="";
         
       

        if (session != null) {
         
          name = (String)session.getAttribute("sessionuser");
          if (name != null)
          {
            //out.print("Hello, " + name + " Welcome to Profile");
            logger.info("SESSION ID BOLETO" +session.getId());
            request.getRequestDispatcher("/jsp/Home.jsp").include(request, response); 
          }
          else
          {
               // out.print("Please login first");
               //request.setAttribute("csrfToken", "1q2w3e4r");
               String TokenName="formlogincsrfToken";
               String Token= dao1.GenToken(request, TokenName.toString());
               request.setAttribute("csrfToken", Token );
               //response.addHeader("Access-Control-Allow-Origin", "*");
          request.getRequestDispatcher("/jsp/login.jsp").include(request, response); 
          //return "login";
          }
        
        } else {
          
         // out.print("Please login first");
          //request.getRequestDispatcher("/login.html").include((ServletRequest)request, (ServletResponse)response);
          //request.setAttribute("csrfToken", "1q2w3e4r");
          String TokenName="formlogincsrfToken";
          String Token= dao1.GenToken(request, TokenName.toString());
          request.setAttribute("csrfToken", Token );
          //response.addHeader("Access-Control-Allow-Origin", "*");
          request.getRequestDispatcher("/jsp/login.jsp").include(request, response);  
          
          
        } 
        //out.close();
    }  
    @RequestMapping("/EditProfile")  
     
    public String EditProfile(HttpServletRequest request, HttpServletResponse response, Model m) throws ServletException, IOException
    {
    	biraja(request, response);
    	
    	//int UserId=session.getAttribute("sessionuserId")
    
          // user usr=dao1.getUserById(Integer.valueOf((String) session.getAttribute("sessionuserId").toString()));  
    	 user usr=dao1.getUserById(Integer.valueOf((String) request.getSession().getAttribute("sessionuserId").toString()));
          
            m.addAttribute("command",usr);
            String TokenName="EditProfileForm";
            String Token= dao1.GenToken(request, TokenName.toString());
            request.setAttribute("csrfToken", Token );
           
            //response.addHeader("Access-Control-Allow-Origin", "*");
           return "EditProfile";  
        
    }
    @RequestMapping(value="/UpdateProfile", method=RequestMethod.POST)  
    
    public void UpdateProfile(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("usr") user usr) throws IOException, ServletException
    {
    	    HttpSession session = request.getSession(false);
    	
          response.setContentType("text/html"); 
          logger.info("Access-Control-Allow-Origin from request" +request.getHeader("Access-Control-Allow-Origin"));
          
          PrintWriter out=response.getWriter();  
          String ValidationFlag= dao1.csrfTokenValidate(request, "formlogincsrfToken", request.getParameter("FormToken").toString());
          if(ValidationFlag=="false")
          {
            session.invalidate();
            request.setAttribute("csrfToken", dao1.GenToken(request, "formlogincsrfToken"));
            //response.addHeader("Access-Control-Allow-Origin", "*");			
    				request.getRequestDispatcher("/jsp/login.jsp").include(request, response);
          }
          else
          {

          
    	
              // out.print("Hello, "  + request.getParameter("name")+" Welcome to Profile");
              // return "EditProfile";
                if (usr.getEmail().equals(null))
                {
                  out.print("Invalid Data");
                }
                else
                {
                  int sessionuserId = Integer.valueOf((String) session.getAttribute("sessionuserId").toString());
                  dao1.UpdateProfile(usr, sessionuserId);
                  out.print("OK");  //Sending ok because the validation.js expect it to return OK on success.
                }
          }
    }

    @RequestMapping(value="/hashdata", method=RequestMethod.GET)  
    public String hashthedata(HttpServletRequest request, HttpServletResponse response)
    {
    	String hashkey="keyhash";
    	String hasval="1234445";
    	
        HashMap<String, String> hashdata = new HashMap<String, String>();
    	
    	hashdata.put(hashkey, hasval);
    	return "hashoutput";
    }
    
    @RequestMapping(value="/hashdatafetch", method=RequestMethod.GET)  
    public String hashthedatafetch(HttpServletRequest request, HttpServletResponse response)
    {
    	
    	
        HashMap<String, String> hashdata = new HashMap<String, String>();
    	
    	String Fetchdatahash= hashdata.get("keyhash");
    	logger.info("Fetchdatahash "+Fetchdatahash);
    	return "0";
    }
    

}  