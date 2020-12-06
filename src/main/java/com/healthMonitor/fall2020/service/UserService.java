package com.healthMonitor.fall2020.service;

import com.healthMonitor.fall2020.domain.User;
import com.healthMonitor.fall2020.domain.UserInfo;
import com.healthMonitor.fall2020.orm.IDataDao;
import com.healthMonitor.fall2020.utils.IDTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class UserService extends BaseService {
    @Autowired
    @Qualifier("iDao")
    protected IDataDao iDao;

    @Autowired
    UserInfoService userInfoService;

    public static boolean checkEmail(String email) {
        try {
            String check = "([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            return matcher.matches();
        } catch (Exception e) {
            return false;
        }
    }


    public int createUser(User user){
        user.setStatus(1);
        Date now = LocalDateTodate();
        user.setCreateDate(now);
        UserInfo userInfo = new UserInfo();
        userInfo.setId(IDTool.NewID());
        userInfo.setUserID(user.getUserId());
        userInfo.setCreateDate(now);
        int ret = userInfoService.addUserInfo(userInfo);
        if(ret==1){
            return iDao.add(user);
        }
        else{
            return 0;
        }


    }

    public String getUserId(String email, String password) {

        return iDao.uniqueResult("SELECT userId FROM userTable WHERE email = ? and password =?",email,password);
    }
    public boolean getUser(User user) {
        User ret;
        try {
            ret = iDao.get(User.class, "WHERE email = ?", user.getEmail());
        }
        catch (Exception e){
            return true;
        }
       if(ret==null){
           return true;
       }
       else{
           return false;
       }
    }
}
