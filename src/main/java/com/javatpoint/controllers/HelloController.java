package com.javatpoint.controllers;  
  
import org.springframework.stereotype.Controller;  
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RequestMethod;  
  
@Controller  
public class HelloController {  
      
    @RequestMapping(value="/", method=RequestMethod.GET)  
    public String home() {  
    	//biraja
        return "home";  
    }  
      
    @RequestMapping(value="/admin", method=RequestMethod.GET)  
    public String privateHome() {  
        return "privatePage";  
        
    }  
}  