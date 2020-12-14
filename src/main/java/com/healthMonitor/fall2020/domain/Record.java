package com.healthMonitor.fall2020.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/*
Written by: Ruixi Li
Tested by: Ruixi Li
Debugged by: Ruixi Li
 */

@Data
@Table(name = "deviceDataRecordTable")
public class Record {

    @Id
    @Column(name = "id")
    @ApiModelProperty(hidden = true)
    private String id;

    @Column(name = "deviceId")
    private String deviceId;

    @Column(name = "heartRate")
    private String heartRate;

    @Column(name = "bodyTemp")
    private String bodyTemp;

    @Column(name = "stepCount")
    private String stepCount;

    @Column(name = "createTime")
    @ApiModelProperty(hidden = true)
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(String heartRate) {
        this.heartRate = heartRate;
    }

    public String getBodyTemp() {
        return bodyTemp;
    }

    public void setBodyTemp(String bodyTemp) {
        this.bodyTemp = bodyTemp;
    }

    public String getStepCount() {
        return stepCount;
    }

    public void setStepCount(String stepCount) {
        this.stepCount = stepCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
