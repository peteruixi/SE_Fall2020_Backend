package com.healthMonitor.fall2020.service;

import com.healthMonitor.fall2020.domain.UserInfo;
import com.healthMonitor.fall2020.orm.IDataDao;
import com.healthMonitor.fall2020.orm.Page;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Date;

@Service
public class UserInfoService extends BaseService{
    @Autowired
    @Qualifier("iDao")
    protected IDataDao iDao;


    public int addUserInfo(UserInfo userInfo) {
        return iDao.add(userInfo);
    }
    public int modifyUserInfo(String userId, UserInfo userInfo){
        if(userInfo.getHeight()!=null && userInfo.getWeight() != null && userInfo.getAge()!=null) {
            double BMI = Double.parseDouble(userInfo.getWeight()) / Math.pow((Double.parseDouble(userInfo.getWeight())/100), 2);
            DecimalFormat decimalFormat = new DecimalFormat("###.##");
            userInfo.setBMI(String.valueOf(decimalFormat.format(BMI)));
        }
        Date now = LocalDateTodate();
        userInfo.setModifyDate(now);
        return iDao.update(userInfo);
    }

    public UserInfo getUserInfoObj(String userID){
        UserInfo userInfo = iDao.getBySql("SELECT * FROM userInfoTable WHERE userId = ?",UserInfo.class,userID);
        return userInfo;
    }
    public Page getUserInfoPage(Page page, String userID){
        iDao.queryMap(page ,"SELECT * FROM userInfoTable WHERE userId = ?",userID);
        return page;
    }
}
