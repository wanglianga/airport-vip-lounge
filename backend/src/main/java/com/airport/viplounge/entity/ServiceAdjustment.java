package com.airport.viplounge.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("service_adjustment")
public class ServiceAdjustment {

    private Long id;
    private Long flightChangeId;
    private Long flightId;
    private Long passengerId;
    private String passengerName;
    private String serviceType;
    private Long serviceRecordId;
    private String adjustmentType;
    private LocalDateTime oldStartTime;
    private LocalDateTime oldEndTime;
    private LocalDateTime newStartTime;
    private LocalDateTime newEndTime;
    private String oldStatus;
    private String newStatus;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getFlightChangeId() { return flightChangeId; }
    public void setFlightChangeId(Long flightChangeId) { this.flightChangeId = flightChangeId; }
    public Long getFlightId() { return flightId; }
    public void setFlightId(Long flightId) { this.flightId = flightId; }
    public Long getPassengerId() { return passengerId; }
    public void setPassengerId(Long passengerId) { this.passengerId = passengerId; }
    public String getPassengerName() { return passengerName; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }
    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }
    public Long getServiceRecordId() { return serviceRecordId; }
    public void setServiceRecordId(Long serviceRecordId) { this.serviceRecordId = serviceRecordId; }
    public String getAdjustmentType() { return adjustmentType; }
    public void setAdjustmentType(String adjustmentType) { this.adjustmentType = adjustmentType; }
    public LocalDateTime getOldStartTime() { return oldStartTime; }
    public void setOldStartTime(LocalDateTime oldStartTime) { this.oldStartTime = oldStartTime; }
    public LocalDateTime getOldEndTime() { return oldEndTime; }
    public void setOldEndTime(LocalDateTime oldEndTime) { this.oldEndTime = oldEndTime; }
    public LocalDateTime getNewStartTime() { return newStartTime; }
    public void setNewStartTime(LocalDateTime newStartTime) { this.newStartTime = newStartTime; }
    public LocalDateTime getNewEndTime() { return newEndTime; }
    public void setNewEndTime(LocalDateTime newEndTime) { this.newEndTime = newEndTime; }
    public String getOldStatus() { return oldStatus; }
    public void setOldStatus(String oldStatus) { this.oldStatus = oldStatus; }
    public String getNewStatus() { return newStatus; }
    public void setNewStatus(String newStatus) { this.newStatus = newStatus; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
