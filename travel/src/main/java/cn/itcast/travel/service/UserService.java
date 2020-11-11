package cn.itcast.travel.service;

import cn.itcast.travel.domain.User;

public interface UserService {
    /**
     * user registeration
     * @param user
     * @return
     */
    public boolean register(User user);

    public boolean activate(String code);

    public User login(User user);
}
