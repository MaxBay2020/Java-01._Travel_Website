package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Route;

import java.util.List;

public interface RouteDao {
    /**
     * based on cid, find total records
     */
    public int findTotalCount(int cid, String rname);
    /**
     * based on cid, start, pagesize, find the data list of current page
     */
    public List<Route> findRouteByPage(int cid, int start, int pageSize, String rname);

    /**
     * based on id, search one route object
     * @param rid
     * @return
     */
    public Route findOne(int rid);
}
