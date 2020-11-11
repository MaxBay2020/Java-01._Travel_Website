package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    private RouteService service = new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();

    /**
     * quary page
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuary(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.receive parameters
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String cidStr = request.getParameter("cid");

        String rname = request.getParameter("rname");
        rname = new String(rname.getBytes("iso-8859-1"), "utf-8");


        //2.deal with parameters
        //category id
        int cid = 0;
        if (cidStr != null && cidStr.length() > 0 && !"null".equals(cidStr)) {
            cid = Integer.parseInt(cidStr);
        }

        int currentPage = 0;
        if (currentPageStr != null && currentPageStr.length() > 0) {
            currentPage = Integer.parseInt(currentPageStr);
        } else {
            //if no such parameter, default current page is 1
            currentPage = 1;
        }

        int pageSize = 0;
        if (pageSizeStr != null && pageSizeStr.length() > 0) {
            pageSize = Integer.parseInt(pageSizeStr);
        } else {
            //if no such parameter, default is 5 record per page;
            pageSize = 5;
        }

        //3.call service to look for pageBean object
        PageBean<Route> routePageBean = service.pageQuary(cid, currentPage, pageSize, rname);

        //4.serialize this pageBean object to JSON and return
        super.writeValue(routePageBean, response);
    }

    /**
     * based on id, search one route details
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.receive param id
        String rid = request.getParameter("rid");
        //2.call service to search a route object
        Route route = service.findOne(rid);
        //3.serialize to JSON and return to browser
        super.writeValue(route, response);
    }

    /**
     * judge whether the logged user likes this route
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.get rid
        String rid = request.getParameter("rid");
        //2.get the current logged in user
        User user = (User) request.getSession().getAttribute("user");
        int uid = 0; //uid of user
        if (user == null) {
            //not login
            uid = 0;
        } else {
            //already login
            uid = user.getUid();
        }

        //3.call favoriteService, to check whether is favorite
        boolean flag = favoriteService.isFavorite(rid, uid);
        //4.return flag to browser
        super.writeValue(flag, response);
    }

    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.get rid
        String rid = request.getParameter("rid");
        //2.get uid
        //2.get the current logged in user
        User user = (User) request.getSession().getAttribute("user");
        int uid = 0; //uid of user
        if (user == null) {
            //not login
            return;
        } else {
            //already login
            uid = user.getUid();
        }
        //3.call service's add
        favoriteService.add(rid,uid);
    }

}
