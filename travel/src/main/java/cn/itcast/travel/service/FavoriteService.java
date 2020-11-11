package cn.itcast.travel.service;

public interface FavoriteService {

    /**
     * judge whether is favorite
     * @param rid
     * @param uid
     * @return
     */
    public boolean isFavorite(String rid, int uid);

    /**
     * add favorite
     * @param rid
     * @param uid
     */
    public void add(String rid, int uid);
}
