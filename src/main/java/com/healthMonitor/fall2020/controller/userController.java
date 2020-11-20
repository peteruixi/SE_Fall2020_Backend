package com.healthMonitor.fall2020.controller;

import com.healthMonitor.fall2020.domain.User;
import com.healthMonitor.fall2020.dto.CommResponse;
import com.healthMonitor.fall2020.service.UserService;
import com.healthMonitor.fall2020.utils.IDTool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags="user management")
@RestController
@RequestMapping("user")
public class userController {
    @Autowired
    UserService userService;

    @ApiOperation("User Registeration")
    @PostMapping("/registerUser")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "user email",
                    required = true, paramType = "query"),
            @ApiImplicitParam(name = "password", value = "user password",
                    required = true, paramType = "query"),
            @ApiImplicitParam(name = "userType", value = "0:admin 1:regular user",
                    required = true, paramType = "query")
    })
    public CommResponse registerUser(User user){
        CommResponse commResponse = new CommResponse();
        user.setUserId(IDTool.NewID());
        int ret = userService.createUser(user);
        if (ret==0){
            commResponse.setCode(0);
            commResponse.setMsg("Create User Failed");
        }
        else if (ret==1){
            commResponse.setCode(1);
            commResponse.setMsg("Create User Success");
        }
        return commResponse;

    }


}
