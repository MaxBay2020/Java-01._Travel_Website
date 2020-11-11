package cn.itcast.travel.dao.impl;

import cn.itcast.travel.domain.Favorite;

public interface FavoriteDao {
    /**
     * based on uid and uid, judge whether is favorite
     * @param rid
     * @param uid
     * @return
     */
    public Favorite findByRidAndUid(int rid, int uid);

    /**
     * based on rid, search the number of favorite
     * @param rid
     * @return
     */
    public int findCountByRid(int rid);

    /**
     * add favorite
     * @param rid
     * @param uid
     */
    public void add(int rid, int uid);
}
