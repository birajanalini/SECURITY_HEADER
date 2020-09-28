package com.javatpoint.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import com.javatpoint.controllers.*;




public class HTTPHeaderFilter implements Filter{
	
	/** The logger */
	private Logger logger = Logger.getLogger(HTTPHeaderFilter.class);
    
    private static String X_CROSS_DOMAIN_POLICY = "X-Permitted-Cross-Domain-Policies";
    private static String X_CONTENT_TYPE = "X-Content-Type-Options";
    private static String X_XSS_PROTECTION = "X-XSS-Protection";   
    private static String X_CONTENT_POLICY = "Content-Security-Policy"; 
    //For checking if Sub-domains are to be included and age for HSTS is defined by the user or not.
  	private static String HSTSParameter = null;
  	private static int MaxAgeHSTS = 0;
  	private static String crossDomainPolicyValue;
  	private static String contentSecurityPolicyValue;
  	private static String XFRAMEVALUE;
    
    public void destroy() {
    
    }
    /** For adding header parameters to response 
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
    	logger.info("Entering the HTTPHeaderFilter");
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        // We need to allow verbs GET and POST, filter everything else.
       	
        httpResponse.setHeader(X_CONTENT_POLICY,contentSecurityPolicyValue);    
        

    	logger.info("Exiting the HTTPHeaderFilter");
    	filterChain.doFilter(request, response);
    	return;
    }
   
    public void init(FilterConfig arg0) throws ServletException {
        
    		logger.info("filter initiation started");
            contentSecurityPolicyValue = arg0.getInitParameter("ContentSecurityPolicyValue");		
          
        

	}
    */
    
    
    
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain filterChain) throws IOException, ServletException 
            {
    	logger.debug("Entering the HTTPHeaderFilter");
    	HttpServletRequest httpReq = (HttpServletRequest) request;
        // We need to allow verbs GET and POST, filter everything else.
        if (!(httpReq.getMethod().equals("POST") || httpReq.getMethod().equals("GET"))) {
            HttpServletResponse httpResp = (HttpServletResponse) response;
            httpResp.setHeader("Allow", "GET,POST");
            httpReq.getRequestDispatcher("/jsp/error.jsp").forward(request,response);
            return;
        }
    	checkServletInstance(response);
    	HttpServletResponse httpResponse = (HttpServletResponse) response;
    	logger.debug("Adding the Security configuration in Response");
    	// Adding HSTS header for secure requests 
    	if (request.isSecure()) {
    		if (MaxAgeHSTS > 0) {
    			if (HSTSParameter.equals("none")) {
    				httpResponse.setHeader("Strict-Transport-Security","max-age=" +MaxAgeHSTS+"");
    			} else if (HSTSParameter.equals("includeSubDomains")) {
    				httpResponse.setHeader("Strict-Transport-Security","max-age=" + MaxAgeHSTS + "; includeSubDomains");
    			} else if (HSTSParameter.equals("preload")) {
    				httpResponse.setHeader("Strict-Transport-Security","max-age=" + MaxAgeHSTS + "; includeSubDomains; preload");
    			}
    		}
    	}
    	//httpResponse.setHeader(X_CONTENT_TYPE, "nosniff");
		httpResponse.setHeader(X_XSS_PROTECTION," 1; mode=block");
		httpResponse.setHeader(X_CROSS_DOMAIN_POLICY,crossDomainPolicyValue);
		if(contentSecurityPolicyValue != null && (!contentSecurityPolicyValue.equals(""))){
			httpResponse.setHeader(X_CONTENT_POLICY,contentSecurityPolicyValue);        	
		}
        //Adding header option to avoid click jacking
        httpResponse.setHeader("X-FRAME-OPTIONS", XFRAMEVALUE);
		logger.debug("Added the Security configuration in Response");
		
    	filterChain.doFilter(request, response);
    	checkServletInstance(response);
    	if (!(httpResponse.isCommitted())){
    		
    		
    			//This following code is enable cookie to 'httponly'
    			HttpServletRequest httpRequest = (HttpServletRequest) request;
    			if (httpResponse.containsHeader("Set-Cookie")){
    				String sessionid = httpRequest.getSession().getId();
    				logger.debug("Set-Cookie found in servlet, =" + sessionid);
    				httpResponse.setHeader("Set-Cookie", "JSESSIONID=" + sessionid + "; Path=/; HttpOnly"+((request.isSecure())?"; Secure":""));	            
    			}
    		
    	}
    	logger.debug("Exiting the HTTPHeaderFilter");
    }
   
    public void init(FilterConfig arg0) throws ServletException {
		/*
		 * Reading parameters from web.xml
		 */
		String HSTSMaxAgeInSeconds = arg0.getInitParameter("HSTSMaxAgeInSeconds");
		HSTSParameter = arg0.getInitParameter("HSTSParameterValue");
		try {			
			MaxAgeHSTS = Integer.parseInt(HSTSMaxAgeInSeconds);			
		} catch (NullPointerException e) {
			handleInitError("MaxAgeInSeconds not found, hence not setting HSTS Age. Check web.xml");
		} catch (NumberFormatException e) {
			handleInitError("MaxAgeInSeconds not an Integer, hence not setting HSTS Age. Check web.xml");
		}
		if (MaxAgeHSTS < 0) {
			handleInitError("MaxAgeInSeconds less than 0, hence not setting HSTS Age. Check web.xml");
		}
		
		crossDomainPolicyValue = arg0.getInitParameter("CrossDomainPolicyValue");
		//check the value of domain policy and set the default value, if master only not present
		crossDomainPolicyValue = (crossDomainPolicyValue != null && ("master-only").equalsIgnoreCase(crossDomainPolicyValue))? "master-only":"none";
		contentSecurityPolicyValue = arg0.getInitParameter("ContentSecurityPolicyValue");		
		XFRAMEVALUE = arg0.getInitParameter("X-FRAME-OPTIONS");	
	}
	
	private void handleInitError(String message) {
		logger.error(message);
		MaxAgeHSTS = 0;		
	}
	
	private void checkServletInstance(ServletResponse res) throws ServletException {
		if (!(res instanceof HttpServletResponse)) {
			logger.error("Must be an HttpServletResponse");
			throw new ServletException("Must be an HttpServletResponse");
		}
	}

	

	

}
