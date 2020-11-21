package com.healthMonitor.fall2020.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author Ruixi Li
 */
@Data
@Table(name = "userTable")
public class User implements Serializable {

    @Id
    @Column(name = "userId")
    @ApiModelProperty(hidden = true)
    private String userId;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "userType")
    private int userType;

    @Column(name = "status")
    @ApiModelProperty(hidden = true)
    private int status;

    @Column(name = "createDate")
    @ApiModelProperty(hidden = true)
    private Date createDate;

    @Column(name = "modifyDate")
    @ApiModelProperty(hidden = true)
    private Date modifyDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date crateDate) {
        this.createDate = crateDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }
}

