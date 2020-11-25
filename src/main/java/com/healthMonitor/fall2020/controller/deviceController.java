package com.healthMonitor.fall2020.controller;

import com.healthMonitor.fall2020.dto.CommResponse;
import com.healthMonitor.fall2020.orm.Page;
import com.healthMonitor.fall2020.service.DeviceService;
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

@Api(tags = "device management")
@RestController
@RequestMapping("device")
public class deviceController {
    @Autowired
    DeviceService deviceService;

    @ApiOperation("Add Device")
    @PostMapping(name = "addDevice")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceName", value = "user physical location",
                    required = true, paramType = "query"),
            @ApiImplicitParam(name = "deviceType", value = "user physical location",
                    required = true, paramType = "query")
    })
    public CommResponse addDevice(HttpServletRequest request,String deviceName, String deviceType){
        //String userId = (String)request.getSession().getAttribute("userID");
        String userId = "19bff1a99bd84e2b963f37f65cf4d77b";
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

    @ApiOperation("Get List of Device Associated with UserID")
    @GetMapping("/getDeviceList")
    public CommResponse getDeviceList(Page page, HttpServletRequest request){
        //String userId = (String)request.getSession().getAttribute("userID");
        String userId = "19bff1a99bd84e2b963f37f65cf4d77b";
        CommResponse commResponse = new CommResponse();
        page = deviceService.getDeviceList(page,userId);
        commResponse.data.put("data",page);
        return commResponse;
    }

    @ApiOperation("delete device")
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
