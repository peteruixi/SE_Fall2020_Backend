package com.healthMonitor.fall2020.controller;

import com.healthMonitor.fall2020.domain.UserInfo;
import com.healthMonitor.fall2020.dto.CommResponse;
import com.healthMonitor.fall2020.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;

@Api(tags = "userinfo management")
@RestController
@RequestMapping("userinfo")
public class userInfoController {
    @Autowired
    UserInfoService userInfoService;

    @ApiOperation("modify userinfo")
    @PostMapping("/updateUserInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "coordinates", value = "user physical location",
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
    public CommResponse updateUserInfo(HttpServletRequest request, String coordinates, String age, String weight, String gender, String height){
        //UserInfo userInfo = new UserInfo();
        CommResponse commResponse = new CommResponse();
//        String userId = (String) request.getSession().getAttribute("userID");
        String userId = "19bff1a99bd84e2b963f37f65cf4d77b";
        UserInfo oldUserInfo = userInfoService.getUserInfo(userId);
        if(coordinates !=null) oldUserInfo.setCoordinates(coordinates);
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

}
