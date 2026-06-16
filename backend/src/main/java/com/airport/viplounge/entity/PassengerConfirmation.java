package com.airport.viplounge.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("passenger_confirmation")
public class PassengerConfirmation {

    private Long id;
    private Long flightChangeId;
    private Long flightId;
    private Long passengerId;
    private String passengerName;
    private String passengerPhone;
    private String confirmStatus;
    private String passengerChoice;
    private String newFlightNo;
    private Boolean needPackedMeal;
    private String confirmedBy;
    private String confirmedByName;
    private LocalDateTime confirmTime;
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
    public String getPassengerPhone() { return passengerPhone; }
    public void setPassengerPhone(String passengerPhone) { this.passengerPhone = passengerPhone; }
    public String getConfirmStatus() { return confirmStatus; }
    public void setConfirmStatus(String confirmStatus) { this.confirmStatus = confirmStatus; }
    public String getPassengerChoice() { return passengerChoice; }
    public void setPassengerChoice(String passengerChoice) { this.passengerChoice = passengerChoice; }
    public String getNewFlightNo() { return newFlightNo; }
    public void setNewFlightNo(String newFlightNo) { this.newFlightNo = newFlightNo; }
    public Boolean getNeedPackedMeal() { return needPackedMeal; }
    public void setNeedPackedMeal(Boolean needPackedMeal) { this.needPackedMeal = needPackedMeal; }
    public String getConfirmedBy() { return confirmedBy; }
    public void setConfirmedBy(String confirmedBy) { this.confirmedBy = confirmedBy; }
    public String getConfirmedByName() { return confirmedByName; }
    public void setConfirmedByName(String confirmedByName) { this.confirmedByName = confirmedByName; }
    public LocalDateTime getConfirmTime() { return confirmTime; }
    public void setConfirmTime(LocalDateTime confirmTime) { this.confirmTime = confirmTime; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
