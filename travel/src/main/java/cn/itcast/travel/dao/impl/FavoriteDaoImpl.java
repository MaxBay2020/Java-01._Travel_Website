package cn.itcast.travel.dao.impl;

import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;

public class FavoriteDaoImpl implements FavoriteDao{
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    /**
     * based on uid and uid, judge whether is favorite
     * @param rid
     * @param uid
     * @return
     */
    @Override
    public Favorite findByRidAndUid(int rid, int uid) {
        Favorite favorite = null;
        try {
            //1.define sql
            String sql = "SELECT * FROM tab_favorite WHERE rid=? AND uid=?";
            //2.execute sql and return
            favorite = template.queryForObject(sql, new BeanPropertyRowMapper<Favorite>(Favorite.class), rid, uid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return favorite;
    }

    @Override
    public int findCountByRid(int rid) {
        //1.define sql
        String sql = "SELECT COUNT(*) FROM tab_favorite WHERE rid=?";
        //2.execute sql and return
        return template.queryForObject(sql, Integer.class,rid);
    }

    /**
     * add favorite
     * @param rid
     * @param uid
     */
    @Override
    public void add(int rid, int uid) {
        //1.define sql
        String sql = "INSERT INTO tab_favorite VALUES(?,?,?)";
        //2.execute sql
        template.update(sql, rid, new Date(),uid);
    }
}
