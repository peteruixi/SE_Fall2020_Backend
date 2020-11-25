package com.healthMonitor.fall2020.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "adviceTable" )
public class Advice {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "deviceId")
    private String deviceId;

    @Column(name = "heartRate")
    private String hearRate;

    @Column(name = "bodyTemp")
    private String bodyTemp;

    @Column(name = "stepCount")
    private String stepCount;

    @Column(name = "createTime")
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

    public String getHearRate() {
        return hearRate;
    }

    public void setHearRate(String hearRate) {
        this.hearRate = hearRate;
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
