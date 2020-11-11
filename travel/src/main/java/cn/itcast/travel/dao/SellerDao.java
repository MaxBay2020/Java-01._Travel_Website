package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Seller;

public interface SellerDao {
    /**
     * based on sid in tab_route, search seller info from tab_seller
     * @param sid
     * @return
     */
    public Seller findBySid(int sid);
}
