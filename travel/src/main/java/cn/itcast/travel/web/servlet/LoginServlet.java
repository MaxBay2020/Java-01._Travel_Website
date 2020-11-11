package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.acquire username and password;
        Map<String, String[]> map = request.getParameterMap();
        //2.seal a User object;
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        ResultInfo info = new ResultInfo();
        //3.code check
        String code = request.getParameter("check");
        //3.1get code from session
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        //3.2code only for once
        session.removeAttribute("CHECKCODE_SERVER");
        //3.3compare
        if (checkcode_server==null || !checkcode_server.equalsIgnoreCase(code)) {
            //code incorrect;
            info.setFlag(false);
            info.setErrorMsg("Code incorrect");
            ObjectMapper mapper = new ObjectMapper();
            response.setContentType("application/json;charset=utf-8");
            mapper.writeValue(response.getOutputStream(),info);
            return;
        }
        //4.call service to quary
        UserService service = new UserServiceImpl();
        User u = service.login(user);
        //5.judge, u is null?

        if(u==null){
            //username or password incorrect
            info.setFlag(false);
            info.setErrorMsg("username or password incorrect");
        }
        //6.judge user activated?
        if(u!=null && !"y".equalsIgnoreCase(u.getStatus())){
            //not activated
            info.setFlag(false);
            info.setErrorMsg("Please activate your account first");
        }
        //7.login successfully
        if(u!=null&&"y".equalsIgnoreCase(u.getStatus())){
            //login successfully
            info.setFlag(true);
            //save user to session
            request.getSession().setAttribute("user", u);
        }

        //8.respond data
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),info);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
