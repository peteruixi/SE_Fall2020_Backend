package com.healthMonitor.fall2020.controller;

import com.healthMonitor.fall2020.dto.CommResponse;
import com.healthMonitor.fall2020.filter.NeedToken;
import com.healthMonitor.fall2020.orm.Page;
import com.healthMonitor.fall2020.service.DeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "device management")
@RestController
@RequestMapping("/device")
public class deviceController {
    @Autowired
    DeviceService deviceService;

    @NeedToken
    @ApiOperation("Add Device")
    @RequestMapping (value = "/addDevice",method = {RequestMethod.POST})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceName", value = "device name",
                    required = true, paramType = "query"),
            @ApiImplicitParam(name = "deviceType", value = "type of device 1:smartphone 2:wristband 3:smart watch 4:others",
                    required = true, paramType = "query")
    })
    public CommResponse addDevice(HttpServletRequest request,String deviceName, String deviceType){
        String userId = (String)request.getSession().getAttribute("userId");
        //String userId = "19bff1a99bd84e2b963f37f65cf4d77b";
        CommResponse commResponse = new CommResponse();
        int ret = deviceService.addDevice(userId, deviceName,deviceType);
        if(ret == 1){
            commResponse.setCode(1);
            commResponse.setMsg("Device add success");
        }
        else{
            commResponse.setCode(0);
            commResponse.setMsg("Device add failed");
        }
        return commResponse;
    }

    @NeedToken
    @ApiOperation("Get List of Device Associated with UserID")
    @GetMapping("/getDeviceList")
    public CommResponse getDeviceList(Page page, HttpServletRequest request){
        String userId = (String)request.getSession().getAttribute("userId");
        //String userId = "19bff1a99bd84e2b963f37f65cf4d77b";
        CommResponse commResponse = new CommResponse();
        page = deviceService.getDeviceList(page,userId);
        commResponse.data.put("data",page);
        return commResponse;
    }

    @NeedToken
    @ApiOperation("Delete Device")
    @PostMapping("/deleteDevice")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceId", value = "user email",
                    required = true, paramType = "query")
    })
    public CommResponse deleteDevice(String deviceId){
        CommResponse commResponse = new CommResponse();
        int ret = deviceService.deleteDevice(deviceId);
        if(ret ==1){
            commResponse.setCode(1);
            commResponse.setMsg("Device delete success");
        }
        else{
            commResponse.setCode(0);
            commResponse.setMsg("Device delete failed");
        }
        return commResponse;
    }
}
