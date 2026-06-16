package com.airport.viplounge.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("flight_change")
public class FlightChange {

    private Long id;
    private Long flightId;
    private String flightNo;
    private String changeType;
    private String oldStatus;
    private String newStatus;
    private String oldGate;
    private String newGate;
    private Boolean oldFarStand;
    private Boolean newFarStand;
    private LocalDateTime oldScheduledDeparture;
    private LocalDateTime newScheduledDeparture;
    private String notifiedBy;
    private String notifiedByName;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getFlightId() { return flightId; }
    public void setFlightId(Long flightId) { this.flightId = flightId; }
    public String getFlightNo() { return flightNo; }
    public void setFlightNo(String flightNo) { this.flightNo = flightNo; }
    public String getChangeType() { return changeType; }
    public void setChangeType(String changeType) { this.changeType = changeType; }
    public String getOldStatus() { return oldStatus; }
    public void setOldStatus(String oldStatus) { this.oldStatus = oldStatus; }
    public String getNewStatus() { return newStatus; }
    public void setNewStatus(String newStatus) { this.newStatus = newStatus; }
    public String getOldGate() { return oldGate; }
    public void setOldGate(String oldGate) { this.oldGate = oldGate; }
    public String getNewGate() { return newGate; }
    public void setNewGate(String newGate) { this.newGate = newGate; }
    public Boolean getOldFarStand() { return oldFarStand; }
    public void setOldFarStand(Boolean oldFarStand) { this.oldFarStand = oldFarStand; }
    public Boolean getNewFarStand() { return newFarStand; }
    public void setNewFarStand(Boolean newFarStand) { this.newFarStand = newFarStand; }
    public LocalDateTime getOldScheduledDeparture() { return oldScheduledDeparture; }
    public void setOldScheduledDeparture(LocalDateTime oldScheduledDeparture) { this.oldScheduledDeparture = oldScheduledDeparture; }
    public LocalDateTime getNewScheduledDeparture() { return newScheduledDeparture; }
    public void setNewScheduledDeparture(LocalDateTime newScheduledDeparture) { this.newScheduledDeparture = newScheduledDeparture; }
    public String getNotifiedBy() { return notifiedBy; }
    public void setNotifiedBy(String notifiedBy) { this.notifiedBy = notifiedBy; }
    public String getNotifiedByName() { return notifiedByName; }
    public void setNotifiedByName(String notifiedByName) { this.notifiedByName = notifiedByName; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
