package com.healthMonitor.fall2020.service;

import com.healthMonitor.fall2020.domain.Record;
import com.healthMonitor.fall2020.domain.UserDeviceRelation;
import com.healthMonitor.fall2020.orm.IDataDao;
import com.healthMonitor.fall2020.orm.Page;
import com.healthMonitor.fall2020.utils.IDTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;

/*
Written by: Ruixi Li
Tested by: Ruixi Li
Debugged by: Ruixi Li
 */

@Service
public class DeviceRecordService extends BaseService {

    @Autowired
    @Qualifier("iDao")
    protected IDataDao iDao;

//    public int getRelation(String userId) {
//        iDao.get(UserDeviceRelation.class,"WHERE userId = ?",userId);
//    }

    public int createRecord(Record record){
        record.setId(IDTool.NewID());
        Date now = new Date();
        now = LocalDateTodate();
        record.setCreateTime(now);
        return iDao.add(record);
    }

    public Page getRecordList(Page page,String userId, String deviceId){
        page = iDao.queryMap(page,"SELECT * FROM deviceDataRecordTable WHERE deviceId = ?",deviceId);
        return page;
    }
}
