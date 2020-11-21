package com.healthMonitor.fall2020.service;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.healthMonitor.fall2020.domain.User;
import com.healthMonitor.fall2020.domain.UserInfo;
import com.healthMonitor.fall2020.orm.IDataDao;
import com.healthMonitor.fall2020.utils.IDTool;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;


@Service
public class UserService extends BaseService {
    @Autowired
    @Qualifier("iDao")
    protected IDataDao iDao;

    @Autowired
    UserInfoService userInfoService;

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

}
