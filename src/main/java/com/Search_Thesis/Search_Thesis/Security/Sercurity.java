package com.Search_Thesis.Search_Thesis.Security;

import com.Search_Thesis.Search_Thesis.Filter.JwtRequestFilter;
import com.Search_Thesis.Search_Thesis.Services.customerDetailsServices;
import com.Search_Thesis.Search_Thesis.resposity.SignIn_Respository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class Sercurity extends WebSecurityConfigurerAdapter {


    public SignIn_Respository signIn_respository;
    private customerDetailsServices customerDetailsServices = new customerDetailsServices() ;
    private  final  UserDetailsService userDetailsService ;

    public Sercurity(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

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
                .authorizeRequests().antMatchers("/document/**","/edit_document/**" ,"/load_user_token_Edit_page" , "/api/ckt/**" ,"/profile/**","/home/**" ,"/login" , "/blog" ,"/css/**" , "/fonts/**" , "/img/**" ,"/js/**","/reset_pass" ,"/contact").hasRole("USER").
                antMatchers(HttpMethod.GET ,"/load_user_token_Edit_page" , "/edit_document" ,"/profile/**"  ,"/home/**","/reset_pass" , "/document/**").hasAnyRole("USER").
                antMatchers("/load_user_token_Edit_page" , "/login" ,"/blog", "/sign_up","/reset_pass","/reset_pass/**",
                          "/profile/**","/forgot_password/**" ,"/home/**" ,"/api/ckt/**", "/css/**" , "/fonts/**" , "/img/**" ,"/js/**"
                        ,"/contact","/document/**" ,
                        "/document_upload" ,"/load_user_token" ,"/edit_document/**" )
                .permitAll().
                anyRequest().authenticated().and().

                formLogin().loginPage("/login").defaultSuccessUrl("/home").and().

                exceptionHandling().and().sessionManagement().maximumSessions(8).and()

                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS).and()

                .rememberMe();

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


    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.customerDetailsServices);
        try {
            System.out.println(this.customerDetailsServices.get_Role());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
