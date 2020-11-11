package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    /**
     * based on cid, find total records
     */
    @Override
    public int findTotalCount(int cid, String rname) {
        //1.define sql
//        String sql = "SELECT COUNT(*) FROM tab_route WHERE cid=?";
        //1.define a template sql;
        String sql = "SELECT COUNT(*) FROM tab_route WHERE 1=1 ";
        //2.whether has value
        StringBuilder sb = new StringBuilder(sql);

        List params = new ArrayList();//conditions

        if(cid!=0){
            sb.append(" AND cid=? ");
            params.add(cid);
        }
        if(rname!=null && rname.length()>0){
            sb.append(" AND rname LIKE ? ");
            params.add("%"+rname+"%");
        }
        sql=sb.toString();
        //3.execute sql
        return template.queryForObject(sql, Integer.class, params.toArray());
    }
    /**
     * based on cid, start, pagesize, find the data list of current page
     */
    @Override
    public List<Route> findRouteByPage(int cid, int start, int pageSize, String rname) {
        //1.define sql
        //String sql = "SELECT * FROM tab_route WHERE cid=? LIMIT ?,?";

        //1.define a template sql;
        String sql = "SELECT * FROM tab_route WHERE 1=1 ";

        //2.whether has value
        StringBuilder sb = new StringBuilder(sql);

        List params = new ArrayList();//conditions

        if(cid!=0){
            sb.append(" AND cid=? ");
            params.add(cid);
        }
        if(rname!=null && rname.length()>0){
            sb.append(" AND rname LIKE ? ");
            params.add("%"+rname+"%");
        }
        sb.append("LIMIT ?, ? ");
        
        params.add(start);
        params.add(pageSize);

        sql=sb.toString();

        //3.execute sql
        return template.query(sql, new BeanPropertyRowMapper<Route>(Route.class), params.toArray());
    }

    /**
     * based on id, search one route object
     * @param rid
     * @return
     */
    @Override
    public Route findOne(int rid) {
        //1.define sql
        String sql = "SELECT * FROM tab_route WHERE rid=?";
        //2.execute sql and return
        return template.queryForObject(sql, new BeanPropertyRowMapper<Route>(Route.class),rid);
    }
}
