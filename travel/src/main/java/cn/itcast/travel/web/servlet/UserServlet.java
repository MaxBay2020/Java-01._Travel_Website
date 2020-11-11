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

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    //delare a UserService object
    private UserService service = new UserServiceImpl();
    /**
     * register function
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //0.code check
        String code = request.getParameter("check");
        //get code from session
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        //code only for once
        session.removeAttribute("CHECKCODE_SERVER");
        //compare
        if (checkcode_server == null || !checkcode_server.equalsIgnoreCase(code)) {
            //code incorrect;
            ResultInfo info = new ResultInfo();
            info.setFlag(false);
            info.setErrorMsg("Code incorrect");
//            ObjectMapper mapper = new ObjectMapper();
//            String json = mapper.writeValueAsString(info);
//            response.setContentType("application/json;charset=utf-8");
            String json = super.writeValueToString(info);
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
        //UserService service = new UserServiceImpl();
        boolean flag = service.register(user);
        //4.response result
        ResultInfo info = new ResultInfo();
        if (flag) {
            //register successfully;
            info.setFlag(true);
            System.out.println(info.isFlag());
        } else {
            //register unsuccessfully;
            info.setFlag(false);
            System.out.println(info.isFlag());
//            info.setErrorMsg("Register unsuccessfully");
            info.setErrorMsg("Username already exists");
        }
        //4.serialize info to json and send to browser;
//        ObjectMapper mapper = new ObjectMapper();
//        String json = mapper.writeValueAsString(info);
//        response.setContentType("application/json;charset=utf-8");
        String json = super.writeValueToString(info);
        response.getWriter().write(json);
    }

    /**
     * login function
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        if (checkcode_server == null || !checkcode_server.equalsIgnoreCase(code)) {
            //code incorrect;
            info.setFlag(false);
            info.setErrorMsg("Code incorrect");
//            ObjectMapper mapper = new ObjectMapper();
//            response.setContentType("application/json;charset=utf-8");
//            mapper.writeValue(response.getOutputStream(), info);
            super.writeValue(info,response);
            return;
        }
        //4.call service to quary
        //UserService service = new UserServiceImpl();
        User u = service.login(user);
        //5.judge, u is null?

        if (u == null) {
            //username or password incorrect
            info.setFlag(false);
            info.setErrorMsg("username or password incorrect");
        }
        //6.judge user activated?
        if (u != null && !"y".equalsIgnoreCase(u.getStatus())) {
            //not activated
            info.setFlag(false);
            info.setErrorMsg("Please activate your account first");
        }
        //7.login successfully
        if (u != null && "y".equalsIgnoreCase(u.getStatus())) {
            //login successfully
            info.setFlag(true);
            //save user to session
            request.getSession().setAttribute("user", u);
        }

        //8.respond data
//        ObjectMapper mapper = new ObjectMapper();
//        response.setContentType("application/json;charset=utf-8");
//        mapper.writeValue(response.getOutputStream(), info);
        super.writeValue(info,response);
    }

    /**
     * find single user
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.get user from session
        User user = (User) request.getSession().getAttribute("user");
        //2.send user to browser
//        ObjectMapper mapper = new ObjectMapper();
//        response.setContentType("application/json;charset=utf-8");
//        mapper.writeValue(response.getOutputStream(), user);
        super.writeValue(user,response);
    }

    /**
     * exit function
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.delete session;
        request.getSession().invalidate();
        //2.go to login.html
        response.sendRedirect(request.getContextPath() + "/login.html");
    }

    /**
     * activate user function
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void activateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.get activate code
        String code = request.getParameter("code");
        //2.judge
        if(code!=null){
            //2.1 call service to activate
            //UserService service = new UserServiceImpl();
            boolean flag = service.activate(code);

            //3. judge flag
            String msg = null;
            if(flag){
                //Activate successfully
                msg = "Activate successfully, please <a href='login.html'>login</a>";

            }else{
                //Activate unsuccessfully
                msg = "Activate unsuccessfully, please contact administrator";
            }
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);
        }
    }
}
