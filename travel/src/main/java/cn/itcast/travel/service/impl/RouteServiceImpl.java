package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImageDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.*;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {
    private RouteDao routeDao = new RouteDaoImpl();
    private RouteImageDao imgDao = new RouteImageDaoImpl();
    private SellerDao sellerDao = new SellerDaoImpl();
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();

    @Override
    public PageBean<Route> pageQuary(int cid, int currentPage, int pageSize, String rname) {
        //1.seal PageBean;
        PageBean<Route> pb = new PageBean<Route>();

        pb.setCurrentPage(currentPage);
        pb.setPageSize(pageSize);

        int totalCount = routeDao.findTotalCount(cid,rname);
        pb.setTotalCount(totalCount);

        int start = (currentPage - 1) * pageSize;
        pb.setList(routeDao.findRouteByPage(cid, start, pageSize,rname));

        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
        pb.setTotalPage(totalPage);

        return pb;
    }

    @Override
    public Route findOne(String rid) {
        //1.based on rid in tab_route, search route object from tab_route
        Route route = routeDao.findOne(Integer.parseInt(rid));

        //2.based on rid in tab_route, search img list from tab_route_img
        List<RouteImg> routeImgList = imgDao.findByRid(Integer.parseInt(rid));
        route.setRouteImgList(routeImgList);

        //3.based on sid in tab_route, search seller info from tab_seller
        Seller seller = sellerDao.findBySid(route.getSid());
        route.setSeller(seller);

        //4.search number of favorite
        int count = favoriteDao.findCountByRid(route.getRid());
        route.setCount(count);
        return route;
    }
}
