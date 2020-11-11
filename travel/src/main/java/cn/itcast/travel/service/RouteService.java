package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

/**
 * route service, based on category to search each page
 */
public interface RouteService {
    public PageBean<Route> pageQuary(int cid, int currentPage, int pageSize, String rname);

    /**
     * based on id, search an route object
     * @param rid
     * @return
     */
    public Route findOne(String rid);
}
