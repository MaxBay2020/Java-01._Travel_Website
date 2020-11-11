package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

import java.util.UUID;

public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDaoImpl();
    /**
     * user registeration
     * @param user
     * @return
     */
    @Override
    public boolean register(User user) {
        //1.look for user based on username provided
        User u = userDao.findByUsername(user.getUsername());

        //judge whether u is null
        if(u!=null){
            //user exists, register unsuccessfully;
            return false;
        }

        //2.save user info
        //2.1 set activating code (unique)
        user.setCode(UuidUtil.getUuid());
        //2.3 set status of activation
        user.setStatus("N");
        userDao.save(user);

        //3.send activating email, contend of email
        String content = "<a href='http://localhost:8080/travel/user/activateUser?code="+user.getCode()+"'>Click to activate your account.[Toronto Tourism Website]</a>";
        MailUtils.sendMail(user.getEmail(),content,"Activating email");
        return true;
    }

    /**
     * activate user
     * @param code
     * @return
     */
    @Override
    public boolean activate(String code) {
        //1.based on activate code, look for user
        User user = userDao.findUserByCode(code);
        if(user!=null){
            //2.if user exists, update status to Y, return true;
            userDao.updateStatus(user);
            return true;
        }else{
            //3.if user not exists, return false
            return false;
        }
    }

    /**
     * login
     * @param user
     * @return
     */
    @Override
    public User login(User user) {
        return userDao.findUserByUsernameAndPassword(user.getUsername(),user.getPassword());
    }
}
