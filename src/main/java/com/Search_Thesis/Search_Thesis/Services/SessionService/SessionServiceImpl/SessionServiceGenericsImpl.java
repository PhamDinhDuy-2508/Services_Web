package com.Search_Thesis.Search_Thesis.Services.SessionService.SessionServiceImpl;

import com.Search_Thesis.Search_Thesis.Model.Folder;
import com.Search_Thesis.Search_Thesis.Services.SessionService.SessionServiceForGenerics;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Service("SessionServiceGenerics")
public class SessionServiceGenericsImpl  implements SessionServiceForGenerics<List<Folder>> {

    @Override
    public void Create_Session(HttpServletRequest request, String name, List<Folder> list_json) {
        HttpSession session = request.getSession(true);
        session.setAttribute((String) name, list_json);
        session.setMaxInactiveInterval(60 * 3600);
    }
}
