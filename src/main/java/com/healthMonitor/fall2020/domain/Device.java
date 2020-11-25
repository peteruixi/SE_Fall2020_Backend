package com.healthMonitor.fall2020.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "deviceTable")
public class Device {

    @Id
    @Column(name = "deviceId")
    @ApiModelProperty(hidden = true)
    private String deviceId;

    @Column(name = "deviceName")
    private String deviceName;

    @Column(name = "deviceType")
    private String deviceType;

    @ApiModelProperty(hidden = true)
    @Column(name = "createDate")
    private Date createDate;

    @ApiModelProperty(hidden = true)
    @Column(name = "status")
    private int status;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
