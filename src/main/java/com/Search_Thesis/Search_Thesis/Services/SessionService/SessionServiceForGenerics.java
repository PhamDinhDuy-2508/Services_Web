package com.Search_Thesis.Search_Thesis.Services.SessionService;

import javax.servlet.http.HttpServletRequest;

public interface SessionServiceForGenerics<P> {
    void Create_Session(HttpServletRequest request, String name, P list_json);
}
