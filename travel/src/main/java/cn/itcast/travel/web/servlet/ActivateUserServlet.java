package cn.itcast.travel.web.servlet;

import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * activate user
 */
@WebServlet("/activateUserServlet")
public class ActivateUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.get activate code
        String code = request.getParameter("code");
        //2.judge
        if(code!=null){
            //2.1 call service to activate
            UserService service = new UserServiceImpl();
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
