package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {
    private CategoryDao dao = new CategoryDaoImpl();
    @Override
    public List<Category> findAll() {
        //1.search from redis
        //1.1 get Jedis
        Jedis jedis = JedisUtil.getJedis();
        //1.2 user sortedset to sort
//        Set<String> categories = jedis.zrange("category", 0, -1);
        //1.3 searcher the score(cid) and value(cname) of sortedset
        Set<Tuple> categories = jedis.zrangeWithScores("category", 0, -1);

        List<Category> cs = null;
        //2 if result is null, search from database, save data to redis
        if(categories==null||categories.size()==0){
//            System.out.println("search from database...");
            //2.1 search from database
            cs = dao.findAll();
            //2.2 save cs to redis
            for (Category c : cs) {
                jedis.zadd("category",c.getCid(),c.getCname());
            }
        }else {
//            System.out.println("search from redis...");
            //3 if result not null, data in redis, get data from redis
            //3.1 save set to list
            cs = new ArrayList<Category>();
            for (Tuple tuple : categories) {
                Category category = new Category();
                category.setCid((int)tuple.getScore());
                category.setCname(tuple.getElement());
                cs.add(category);
            }
        }
        return cs;
    }
}
