package com.healthMonitor.fall2020.controller;

import com.healthMonitor.fall2020.domain.User;
import com.healthMonitor.fall2020.dto.CommResponse;
import com.healthMonitor.fall2020.service.UserService;
import com.healthMonitor.fall2020.utils.IDTool;
import com.healthMonitor.fall2020.utils.JwtTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Scanner;

@Api(tags="user management")
@RestController
@RequestMapping("user")
public class userController {
    @Autowired
    UserService userService;

    @ApiOperation("User Registration")
    @PostMapping("/registerUser")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "user email",
                    required = true, paramType = "query"),
            @ApiImplicitParam(name = "password", value = "user password",
                    required = true, paramType = "query"),
            @ApiImplicitParam(name = "userType", value = "0:admin 1:regular user",
                    required = false, paramType = "query")
    })
    public CommResponse registerUser(User user,String userType){
        CommResponse commResponse = new CommResponse();
        user.setUserId(IDTool.NewID());
        if(userService.checkEmail(user.getEmail())==false){
            commResponse.setCode(0);
            commResponse.setMsg("Please Input Valid Email Address");
        }
        if(userType==null){ //userType.equals("")||
            user.setUserType(1);

        }
        else{
            user.setUserType(Integer.parseInt(userType));
        }
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

    @ApiOperation("User Login")
    @RequestMapping(value = "/login", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "user email",
                    required = true, paramType = "query"),
            @ApiImplicitParam(name = "password", value = "user password",
                    required = true, paramType = "query")
    })
    public CommResponse Login( String email, String password){
        CommResponse commResponse = new CommResponse();
        JwtTokenUtil util = new JwtTokenUtil();
        try{
            System.out.println(email+" "+password);
            String userId = userService.getUserId(email,password);
            System.out.println("userID:"+userId);
            if(userId==null||userId.equals("")){
                commResponse.setCode(0);
                commResponse.setMsg("Incorrect email or incorrect password");
                return commResponse;
            }
            else{
                String token = util.generateToken(userId);
                //System.out.println("userId from token"+util.getUserIdFromToken(token));
                commResponse.setMsg("login successful");
                commResponse.data.put("token",token);
            }
        }catch (Exception e){
            commResponse.setCode(0);
            commResponse.setMsg("Service Unavailable");
            return commResponse;
        }
        return commResponse;
    }



}
