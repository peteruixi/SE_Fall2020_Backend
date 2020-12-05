package com.healthMonitor.fall2020.service;

import com.healthMonitor.fall2020.domain.Device;
import com.healthMonitor.fall2020.domain.UserDeviceRelation;
import com.healthMonitor.fall2020.orm.IDataDao;
import com.healthMonitor.fall2020.orm.Page;
import com.healthMonitor.fall2020.utils.IDTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DeviceService extends BaseService {
    @Autowired
    @Qualifier("iDao")
    protected IDataDao iDao;

    public int addDevice(String userId, String deviceName, String deviceType){
        Device device = new Device();
        UserDeviceRelation deviceRelation = new UserDeviceRelation();
        String deviceId = IDTool.NewID();
        deviceRelation.setStatus(1);


        device.setDeviceId(deviceId);
        device.setDeviceName(deviceName);
        device.setDeviceType(deviceType);
        device.setStatus(1);
        Date now = LocalDateTodate();
        device.setCreateDate(now);
        int flag = iDao.add(device);
        if(flag ==1){
            deviceRelation.setId(IDTool.NewID());
            deviceRelation.setDeviceId(deviceId);
            deviceRelation.setUserId(userId);
            return iDao.add(deviceRelation);

        }
        else{
            return 0;
        }
    }
    public Page getDeviceList(Page page, String userId){
        page = iDao.queryMap(page,"SELECT * FROM userDeviceRelationTable WHERE userId =? AND status =?",userId,1);
        List<Map> list = page.getmList();
        if(list != null){
            List templist = new ArrayList();
            for(int i=0;i<list.size();i++)
            {
                String a_id=(String)list.get(i).get("deviceId");
                System.out.println(a_id);
                Map<String, Object> deviceList = iDao.queryBySql("SELECT * FROM deviceTable where deviceId=? ",a_id);

//                mlist.put("covers",iDao.queryMap("select cover from ad_article_cover where article_id=?",a_id));
                templist.add(deviceList);
            }
            page.setmList(templist);
        }
        return page;

    }

    public int deleteDevice(String deviceId){
        Device device = iDao.get(deviceId,Device.class);
        device.setStatus(0);
        return iDao.executeSql("UPDATE userDeviceRelationTable SET status = 0 WHERE deviceId = ?",deviceId);

    }
}
