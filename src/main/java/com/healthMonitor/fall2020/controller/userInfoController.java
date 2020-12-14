package com.healthMonitor.fall2020.controller;

import com.healthMonitor.fall2020.domain.UserInfo;
import com.healthMonitor.fall2020.dto.CommResponse;
import com.healthMonitor.fall2020.filter.NeedToken;
import com.healthMonitor.fall2020.orm.Page;
import com.healthMonitor.fall2020.service.UserInfoService;
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
import java.text.DecimalFormat;
import java.util.HashMap;

/*
Written by: Ruixi Li
Tested by: Ruixi Li
Debugged by: Ruixi Li
 */

@Api(tags = "userinfo management")
@RestController
@RequestMapping("/userinfo")
public class userInfoController {
    @Autowired
    UserInfoService userInfoService;

    @ApiOperation("Modify Userinfo")
    @NeedToken
    @PostMapping("/updateUserInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "state", value = "user physical location",
                    required = true, paramType = "query"),
            @ApiImplicitParam(name = "age", value = "age",
                    required = true, paramType = "query"),
            @ApiImplicitParam(name = "weight", value = "weight",
                    required = true, paramType = "query"),
            @ApiImplicitParam(name = "gender", value = "0:male 1:female",
                    required = true, paramType = "query"),
            @ApiImplicitParam(name = "height", value = "height",
                    required = true, paramType = "query")
    })
    public CommResponse updateUserInfo(HttpServletRequest request, String state, String age, String weight, String gender, String height){
        //UserInfo userInfo = new UserInfo();
        CommResponse commResponse = new CommResponse();

        String userId = (String) request.getSession().getAttribute("userId");
        //String userId = "19bff1a99bd84e2b963f37f65cf4d77b";
        UserInfo oldUserInfo = userInfoService.getUserInfoObj(userId);
        if(state !=null) oldUserInfo.setState(state);
        if(age != null) oldUserInfo.setAge(age);
        if(weight != null) oldUserInfo.setWeight(weight);
        if(height != null) oldUserInfo.setHeight(height);
        if(gender != null) oldUserInfo.setGender(gender);

        int ret = userInfoService.modifyUserInfo(userId,oldUserInfo);
        if (ret==0){
            commResponse.setCode(0);
            commResponse.setMsg("Modify Userinfo Failed");
        }
        else if (ret==1){
            commResponse.setCode(1);
            commResponse.setMsg("Modify Userinfo Success");
        }
        return commResponse;
    }

    @ApiOperation("Get Userinfo")
    @NeedToken
    @GetMapping("/getUserInfo")
    public CommResponse getUserInfo(HttpServletRequest request, Page page){
        CommResponse commResponse = new CommResponse();
        String userId = (String) request.getSession().getAttribute("userId");
        //String userId = "19bff1a99bd84e2b963f37f65cf4d77b";
        page = userInfoService.getUserInfoPage(page,userId);
        commResponse.data.put("data",page);
        return commResponse;

    }

}
