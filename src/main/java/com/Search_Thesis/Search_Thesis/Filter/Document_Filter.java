package com.Search_Thesis.Search_Thesis.Filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "Document_Filter")
public class Document_Filter implements Filter {

    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest res, ServletResponse resp,
                         FilterChain chain) throws ServletException, IOException {




        HttpServletRequest request = (HttpServletRequest) res  ;

        HttpServletResponse response =  (HttpServletResponse) resp ;
        HttpSession session = request.getSession(true);


        String token = (String) session.getAttribute("jwt_code");

        System.out.println("test" + token);


        if(token == null) {

            response.sendRedirect("/login");
            chain.doFilter(res ,  response);
        }
        chain.doFilter(res, response);

    }
}
