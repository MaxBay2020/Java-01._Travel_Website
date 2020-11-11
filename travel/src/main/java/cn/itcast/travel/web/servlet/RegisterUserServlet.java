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

@WebServlet("/registerUserServlet")
public class RegisterUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //0.code check
        String code = request.getParameter("check");
        //get code from session
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        //code only for once
        session.removeAttribute("CHECKCODE_SERVER");
        //compare
        if (checkcode_server==null || !checkcode_server.equalsIgnoreCase(code)) {
            //code incorrect;
            ResultInfo info = new ResultInfo();
            info.setFlag(false);
            info.setErrorMsg("Code incorrect");
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(info);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return;
        }

        //1.acquire data
        Map<String, String[]> map = request.getParameterMap();
        //2.seal data
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //3.call service
        UserService service = new UserServiceImpl();
        boolean flag = service.register(user);
        //4.response result
        ResultInfo info = new ResultInfo();
        if (flag) {
            //register successfully;

            info.setFlag(true);
        } else {
            //register unsuccessfully;
            info.setFlag(false);

//            info.setErrorMsg("Register unsuccessfully");
            info.setErrorMsg("Username already exists");
        }
        //4.serialize info to json and send to browser;
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
