package com.Search_Thesis.Search_Thesis.Security;

import com.Search_Thesis.Search_Thesis.JWT.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableAutoConfiguration
public class Security_Config  extends WebSecurityConfigurerAdapter {
    @Autowired
    UserServices_Sercurity userService;

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
    @Autowired
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(userService) // Cung cáp userservice cho spring security
                .passwordEncoder(bCryptPasswordEncoder()); // cung cấp password encoder
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        // Get AuthenticationManager bean
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Password encoder, để Spring Security sử dụng mã hóa mật khẩu người dùng
        return new BCryptPasswordEncoder();
    }

        @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeRequests().antMatchers("/document/**","/edit_document/**" ,"/load_user_token_Edit_page" , "/api/ckt/**" ,"/profile/**","/home/**" ,"/login" , "/blog" ,"/css/**" , "/fonts/**" , "/img/**" ,"/js/**","/reset_pass" ,"/contact").hasRole("ADMIN").
                antMatchers("/load_user_token_Edit_page" , "/login" ,"/blog", "/sign_up","/reset_pass","/reset_pass/**",
                          "/profile/**","/forgot_password/**" ,"/home/**" ,"/api/ckt/**", "/css/**" , "/fonts/**" , "/img/**" ,"/js/**"
                        ,"/document/**" ,
                        "/document_upload" ,"/question_info/**","/load_user_token" , "/Create_question/**", "/question/**","/edit_document/**"  ,"/contact")
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

        httpSecurity.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    }


        @Bean
    public SessionRegistry sessionRegistry() {
        SessionRegistry sessionRegistry = new SessionRegistryImpl();
        return sessionRegistry;
    }
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .cors() // Ngăn chặn request từ một domain khác
//                .and()
//                .authorizeRequests()
//                .antMatchers("/api/login").permitAll() // Cho phép tất cả mọi người truy cập vào địa chỉ này
//                .anyRequest().authenticated(); // Tất cả các request khác đều cần phải xác thực mới được truy cập
//
//        // Thêm một lớp Filter kiểm tra jwt
//        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//    }
}
