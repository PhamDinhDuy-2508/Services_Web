package com.Search_Thesis.Search_Thesis.Config;

import com.Search_Thesis.Search_Thesis.Filter.Document_Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration

    public class WebConfig implements WebMvcConfigurer {

        @Override
        public void addViewControllers(ViewControllerRegistry registry) {
        }

        @Bean
        public FilterRegistrationBean<Document_Filter> loggingFilter() {

            FilterRegistrationBean<Document_Filter> registrationBean
                    = new FilterRegistrationBean<>();

            registrationBean.setFilter(new Document_Filter());
            registrationBean.addUrlPatterns("/document/*");

            return  registrationBean ;
        }

}
