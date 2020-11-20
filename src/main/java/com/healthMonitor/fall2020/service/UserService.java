package com.healthMonitor.fall2020.service;

import com.healthMonitor.fall2020.domain.User;
import com.healthMonitor.fall2020.orm.IDataDao;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;


@Service
public class UserService {
    @Autowired
    @Qualifier("iDao")
    protected IDataDao iDao;

    public int createUser(User user){
        user.setStatus(1);
        user.setCrateDate(LocalDateTodate());
        return iDao.add(user);
    }
    public Date LocalDateTodate() {
        LocalDate localDate = LocalDate.now();
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        java.util.Date date = Date.from(instant);
        return date;
    }
}
