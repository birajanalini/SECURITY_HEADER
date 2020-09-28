package com.javatpoint;  
import org.springframework.context.annotation.*;      
import org.springframework.security.config.annotation.web.builders.HttpSecurity;    
import org.springframework.security.config.annotation.web.configuration.*;    
import org.springframework.security.core.userdetails.*;    
import org.springframework.security.provisioning.InMemoryUserDetailsManager;  
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;    
@EnableWebSecurity    
@ComponentScan("com.javatpoint")    
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {    
  @Bean    
  public UserDetailsService userDetailsService() {    
      InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();    
      manager.createUser(User.withDefaultPasswordEncoder()  
      .username("admin").password("admin123").roles("ADMIN").build());    
      return manager;    
  }    
  @Override    
  protected void configure(HttpSecurity http) throws Exception {    
      http.authorizeRequests().  
      antMatchers("/index", "/user","/").permitAll()  
      .antMatchers("/admin").authenticated()  
      .and()  
      .formLogin() // It renders a login form   
      .and()  
      .logout()  
      .logoutRequestMatcher(new AntPathRequestMatcher("/logout"));      
  }    
}    