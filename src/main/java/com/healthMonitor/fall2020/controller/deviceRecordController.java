package com.healthMonitor.fall2020.controller;

import com.healthMonitor.fall2020.domain.Record;
import com.healthMonitor.fall2020.dto.CommResponse;
import com.healthMonitor.fall2020.orm.Page;
import com.healthMonitor.fall2020.service.DeviceRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "device data management")
@RestController
@RequestMapping("deviceData")
public class deviceRecordController {

    @Autowired
    DeviceRecordService deviceRecordService;



    @ApiOperation("Create Record of Data From Device")
    @PostMapping("/createRecord")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceId", value = "device Id",
                    required = true, paramType = "query"),
            @ApiImplicitParam(name = "heartRate", value = "heart rate of the user",
                    required = true, paramType = "query"),
            @ApiImplicitParam(name = "bodyTemp", value = "body temperature of the user",
                    required = true, paramType = "query"),
            @ApiImplicitParam(name = "stepCount", value = "step count",
                    required = true, paramType = "query")
    })
    public CommResponse createRecord(Record record){
        CommResponse commResponse = new CommResponse();
        int ret = deviceRecordService.createRecord(record);
        if(ret == 1){
            commResponse.setCode(1);
            commResponse.data.put("Heartrate",record.getHeartRate());
            commResponse.data.put("Body Temperature",record.getBodyTemp());
            commResponse.data.put("Pedometer",record.getStepCount());
            commResponse.setMsg("Record add success");
        }
        else{
            commResponse.setCode(0);
            commResponse.setMsg("Record add failed");
        }
        return commResponse;
    }

    @ApiOperation("Get Record List")
    @GetMapping("/getRecordList")
    public CommResponse getRecordList(Page page, HttpServletRequest request){
        CommResponse commResponse = new CommResponse();
        //String userId = (String)request.getSession().getAttribute("userId");
        String userId = "19bff1a99bd84e2b963f37f65cf4d77b";
//        DeviceRecordService.getRelation(userId);
        String deviceId = "ae2d762039264cbdb192aa3b9d57a579";
        page = deviceRecordService.getRecordList(page,userId, deviceId);
        commResponse.data.put("data",page);
        return commResponse;
    }

}
