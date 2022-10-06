package com.Search_Thesis.Search_Thesis.Security;

import com.Search_Thesis.Search_Thesis.Filter.JwtRequestFilter;
import com.Search_Thesis.Search_Thesis.Services.customerDetailsServices;
import com.Search_Thesis.Search_Thesis.resposity.SignIn_Respository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@EnableWebSecurity
@Configuration
public class Sercurity extends WebSecurityConfigurerAdapter {


    public SignIn_Respository signIn_respository;
//    private customerDetailsServices customerDetailsServices = new customerDetailsServices() ;
    @Autowired
    private customerDetailsServices customerDetailsServices ;



    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        return super.userDetailsService();
    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests()
//                .antMatchers("/home ").hasRole("USER")
//                .antMatchers(HttpMethod.POST,"/api/ckt/**").
//
//                hasRole("ADMIN")
//                .antMatchers(HttpMethod.GET , "/api/ckt/**").hasAnyRole("ADMIN","USER")
//
//                .anyRequest().authenticated().and().
//                formLogin().loginPage("/login").defaultSuccessUrl("/home").failureUrl("/login").permitAll().and().
//                httpBasic() ;
//
//    }
    private JwtRequestFilter jwtRequestFilter =  new JwtRequestFilter(signIn_respository);
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeRequests().antMatchers("/document/**","/edit_document/**" ,"/load_user_token_Edit_page" , "/api/ckt/**" ,"/profile/**","/home/**" ,"/login" , "/blog" ,"/css/**" , "/fonts/**" , "/img/**" ,"/js/**","/reset_pass" ,"/contact").hasRole("ADMIN").
                antMatchers("/load_user_token_Edit_page" , "/login" ,"/blog", "/sign_up","/reset_pass","/reset_pass/**",
                          "/profile/**","/forgot_password/**" ,"/home/**" ,"/api/ckt/**", "/css/**" , "/fonts/**" , "/img/**" ,"/js/**"
                        ,"/document/**" ,
                        "/document_upload" ,"/load_user_token" ,"/edit_document/**"  ,"/contact")
                .permitAll().
                anyRequest().authenticated().and().

                formLogin().loginPage("/login").defaultSuccessUrl("/home").and().

                exceptionHandling().and().sessionManagement().maximumSessions(8).and()

                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS).and()

                .rememberMe();
        httpSecurity
                .sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true)
                .sessionRegistry(sessionRegistry());

        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    }
    @Bean(name="myPasswordEncoder")

    public PasswordEncoder getPasswordEncoder() {
        DelegatingPasswordEncoder delPasswordEncoder=  (DelegatingPasswordEncoder) PasswordEncoderFactories.createDelegatingPasswordEncoder();
        BCryptPasswordEncoder bcryptPasswordEncoder =new BCryptPasswordEncoder();
        delPasswordEncoder.setDefaultPasswordEncoderForMatches(bcryptPasswordEncoder);
        return delPasswordEncoder;
    };

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
    @Bean
    public SessionRegistry sessionRegistry() {
        SessionRegistry sessionRegistry = new SessionRegistryImpl();
        return sessionRegistry;
    }

    // Register HttpSessionEventPublisher
    @Bean
    public static ServletListenerRegistrationBean _httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
    }



    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        try {
            System.out.println("tsdasdasd" + customerDetailsServices.get_Role()) ;
        }
        catch (Exception e ) {
            System.out.println(e.getMessage());
        }
        auth.userDetailsService(customerDetailsServices);


    }
    @Bean
    public UserDetailsService requestContextListener() {
        return new customerDetailsServices();
    }

}
