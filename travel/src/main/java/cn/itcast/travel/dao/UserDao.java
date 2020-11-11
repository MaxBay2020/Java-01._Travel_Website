package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

public interface UserDao {
    /**
     * Based on username, look for user info
     * @return
     */
    public User findByUsername(String username);

    /**
     * Add user
     * @param user
     */
    public void save(User user);

    public User findUserByCode(String code);

    public void updateStatus(User user);

    public User findUserByUsernameAndPassword(String username, String password);
}
