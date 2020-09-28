package com.javatpoint.beans; 
import com.javatpoint.aspect.Loggable;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
@Component
@EnableAspectJAutoProxy(proxyTargetClass = true)

 


public class user {  
private int id;  
private String UserName;  
private String Email;  
private String Password;  
private String designaion; 
private String phone; 

//user bean

  
public int getId() {  
    return id;  
}  
public void setId(int id) {  
    this.id = id;  
}  
@Loggable
public String getUserName() {
	
    return UserName;  
}  

public void setUserName(String username) {  
	
    this.UserName = username;  
}  

public String getEmail() {  
    return Email;  
}  
public void setEmail(String email) {  
    this.Email = email;  
}  

public String getPassword() {  
    return Password;  
}  
public void setPassword(String password) {  
    this.Password = password;  
}  

public String getDesignation() {  
    return designaion;  
}  
public void setDesignation(String designaion) {  
    this.designaion = designaion;  
}  

public String getPhone() {  
    return phone;  
}  
public void setPhone(String phone) {  
    this.phone = phone;  
}  

public String dGenToken(String TokenName)
{
    
    String TokenValue="1q2w3e4r";  
  
     
           
  
    return TokenValue;

}

}  