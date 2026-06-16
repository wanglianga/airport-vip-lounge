package com.airport.viplounge.service.impl;

import com.airport.viplounge.entity.*;
import com.airport.viplounge.mapper.*;
import com.airport.viplounge.service.ServiceAdjustmentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceAdjustmentServiceImpl extends ServiceImpl<ServiceAdjustmentMapper, ServiceAdjustment>
        implements ServiceAdjustmentService {

    @Autowired
    private FlightChangeMapper flightChangeMapper;

    @Autowired
    private FlightMapper flightMapper;

    @Autowired
    private PassengerMapper passengerMapper;

    @Autowired
    private SeatMapper seatMapper;

    @Autowired
    private MealMapper mealMapper;

    @Autowired
    private ShowerRoomMapper showerRoomMapper;

    @Autowired
    private MeetingRoomMapper meetingRoomMapper;

    @Autowired
    private ServiceRecordMapper serviceRecordMapper;

    @Override
    public List<ServiceAdjustment> listByFlightChangeId(Long flightChangeId) {
        QueryWrapper<ServiceAdjustment> wrapper = new QueryWrapper<>();
        wrapper.eq("flight_change_id", flightChangeId);
        wrapper.orderByAsc("create_time");
        return list(wrapper);
    }

    @Override
    @Transactional
    public List<ServiceAdjustment> recalculateServices(Long flightChangeId) {
        FlightChange flightChange = flightChangeMapper.selectById(flightChangeId);
        if (flightChange == null) {
            throw new RuntimeException("航班变更记录不存在");
        }

        Flight flight = flightMapper.selectById(flightChange.getFlightId());
        if (flight == null) {
            throw new RuntimeException("航班不存在");
        }

        QueryWrapper<Passenger> passengerWrapper = new QueryWrapper<>();
        passengerWrapper.eq("flight_no", flightChange.getFlightNo());
        passengerWrapper.in("status", "ARRIVED", "IN_LOUNGE", "SERVING");
        List<Passenger> passengers = passengerMapper.selectList(passengerWrapper);

        List<ServiceAdjustment> adjustments = new ArrayList<>();

        for (Passenger passenger : passengers) {
            adjustments.addAll(recalculateSeatService(flightChange, flight, passenger));
            adjustments.addAll(recalculateMealService(flightChange, flight, passenger));
            adjustments.addAll(recalculateShowerService(flightChange, flight, passenger));
            adjustments.addAll(recalculateMeetingService(flightChange, flight, passenger));
        }

        return adjustments;
    }

    private List<ServiceAdjustment> recalculateSeatService(FlightChange flightChange, Flight flight, Passenger passenger) {
        List<ServiceAdjustment> adjustments = new ArrayList<>();

        QueryWrapper<Seat> wrapper = new QueryWrapper<>();
        wrapper.eq("passenger_id", passenger.getId());
        Seat seat = seatMapper.selectOne(wrapper);

        if (seat != null && "OCCUPIED".equals(seat.getStatus())) {
            if ("DELAY".equals(flightChange.getChangeType()) && flightChange.getNewScheduledDeparture() != null) {
                Duration delayDuration = Duration.between(
                        flightChange.getOldScheduledDeparture(),
                        flightChange.getNewScheduledDeparture()
                );

                if (delayDuration.toMinutes() > 0) {
                    ServiceAdjustment adjustment = new ServiceAdjustment();
                    adjustment.setFlightChangeId(flightChange.getId());
                    adjustment.setFlightId(flightChange.getFlightId());
                    adjustment.setPassengerId(passenger.getId());
                    adjustment.setPassengerName(passenger.getName());
                    adjustment.setServiceType("SEAT");
                    adjustment.setAdjustmentType("EXTEND");
                    adjustment.setOldStatus("OCCUPIED");
                    adjustment.setNewStatus("OCCUPIED");
                    adjustment.setRemark("航班延误" + delayDuration.toMinutes() + "分钟，座位占用延长");
                    adjustment.setCreateTime(LocalDateTime.now());
                    adjustment.setUpdateTime(LocalDateTime.now());
                    save(adjustment);
                    adjustments.add(adjustment);
                }
            }

            if ("CANCELLED".equals(flightChange.getNewStatus())) {
                ServiceAdjustment adjustment = new ServiceAdjustment();
                adjustment.setFlightChangeId(flightChange.getId());
                adjustment.setFlightId(flightChange.getFlightId());
                adjustment.setPassengerId(passenger.getId());
                adjustment.setPassengerName(passenger.getName());
                adjustment.setServiceType("SEAT");
                adjustment.setAdjustmentType("PENDING_RELEASE");
                adjustment.setOldStatus("OCCUPIED");
                adjustment.setNewStatus("OCCUPIED");
                adjustment.setRemark("航班取消，等待旅客确认后释放座位");
                adjustment.setCreateTime(LocalDateTime.now());
                adjustment.setUpdateTime(LocalDateTime.now());
                save(adjustment);
                adjustments.add(adjustment);
            }
        }

        return adjustments;
    }

    private List<ServiceAdjustment> recalculateMealService(FlightChange flightChange, Flight flight, Passenger passenger) {
        List<ServiceAdjustment> adjustments = new ArrayList<>();

        QueryWrapper<Meal> wrapper = new QueryWrapper<>();
        wrapper.eq("passenger_id", passenger.getId());
        wrapper.eq("flight_no", flightChange.getFlightNo());
        wrapper.in("status", "PENDING", "PREPARING", "READY");
        List<Meal> meals = mealMapper.selectList(wrapper);

        for (Meal meal : meals) {
            if ("DELAY".equals(flightChange.getChangeType()) && flightChange.getNewScheduledDeparture() != null) {
                if ("READY".equals(meal.getStatus()) || "PREPARING".equals(meal.getStatus())) {
                    ServiceAdjustment adjustment = new ServiceAdjustment();
                    adjustment.setFlightChangeId(flightChange.getId());
                    adjustment.setFlightId(flightChange.getFlightId());
                    adjustment.setPassengerId(passenger.getId());
                    adjustment.setPassengerName(passenger.getName());
                    adjustment.setServiceType("MEAL");
                    adjustment.setAdjustmentType("KEEP_WARM");
                    adjustment.setOldStatus(meal.getStatus());
                    adjustment.setNewStatus(meal.getStatus());
                    adjustment.setRemark("航班延误，餐食需要延长保温时间");
                    adjustment.setCreateTime(LocalDateTime.now());
                    adjustment.setUpdateTime(LocalDateTime.now());
                    save(adjustment);
                    adjustments.add(adjustment);
                }
            }

            if ("CANCELLED".equals(flightChange.getNewStatus())) {
                ServiceAdjustment adjustment = new ServiceAdjustment();
                adjustment.setFlightChangeId(flightChange.getId());
                adjustment.setFlightId(flightChange.getFlightId());
                adjustment.setPassengerId(passenger.getId());
                adjustment.setPassengerName(passenger.getName());
                adjustment.setServiceType("MEAL");
                adjustment.setAdjustmentType("PENDING_CANCEL");
                adjustment.setOldStatus(meal.getStatus());
                adjustment.setNewStatus(meal.getStatus());
                adjustment.setRemark("航班取消，等待旅客确认后处理餐食");
                adjustment.setCreateTime(LocalDateTime.now());
                adjustment.setUpdateTime(LocalDateTime.now());
                save(adjustment);
                adjustments.add(adjustment);
            }
        }

        return adjustments;
    }

    private List<ServiceAdjustment> recalculateShowerService(FlightChange flightChange, Flight flight, Passenger passenger) {
        List<ServiceAdjustment> adjustments = new ArrayList<>();

        QueryWrapper<ShowerRoom> wrapper = new QueryWrapper<>();
        wrapper.eq("passenger_id", passenger.getId());
        ShowerRoom showerRoom = showerRoomMapper.selectOne(wrapper);

        if (showerRoom != null && "OCCUPIED".equals(showerRoom.getStatus())) {
            if ("DELAY".equals(flightChange.getChangeType()) && flightChange.getNewScheduledDeparture() != null) {
                ServiceAdjustment adjustment = new ServiceAdjustment();
                adjustment.setFlightChangeId(flightChange.getId());
                adjustment.setFlightId(flightChange.getFlightId());
                adjustment.setPassengerId(passenger.getId());
                adjustment.setPassengerName(passenger.getName());
                adjustment.setServiceType("SHOWER");
                adjustment.setAdjustmentType("EXTEND");
                adjustment.setOldStartTime(showerRoom.getStartTime());
                adjustment.setOldEndTime(showerRoom.getEndTime());
                adjustment.setRemark("航班延误，淋浴预约时间延长");
                adjustment.setCreateTime(LocalDateTime.now());
                adjustment.setUpdateTime(LocalDateTime.now());
                save(adjustment);
                adjustments.add(adjustment);
            }
        }

        return adjustments;
    }

    private List<ServiceAdjustment> recalculateMeetingService(FlightChange flightChange, Flight flight, Passenger passenger) {
        List<ServiceAdjustment> adjustments = new ArrayList<>();

        QueryWrapper<MeetingRoom> wrapper = new QueryWrapper<>();
        wrapper.eq("passenger_id", passenger.getId());
        MeetingRoom meetingRoom = meetingRoomMapper.selectOne(wrapper);

        if (meetingRoom != null && "OCCUPIED".equals(meetingRoom.getStatus())) {
            if ("DELAY".equals(flightChange.getChangeType()) && flightChange.getNewScheduledDeparture() != null) {
                ServiceAdjustment adjustment = new ServiceAdjustment();
                adjustment.setFlightChangeId(flightChange.getId());
                adjustment.setFlightId(flightChange.getFlightId());
                adjustment.setPassengerId(passenger.getId());
                adjustment.setPassengerName(passenger.getName());
                adjustment.setServiceType("MEETING");
                adjustment.setAdjustmentType("EXTEND");
                adjustment.setOldStartTime(meetingRoom.getStartTime());
                adjustment.setOldEndTime(meetingRoom.getEndTime());
                adjustment.setRemark("航班延误，会议室使用时间延长");
                adjustment.setCreateTime(LocalDateTime.now());
                adjustment.setUpdateTime(LocalDateTime.now());
                save(adjustment);
                adjustments.add(adjustment);
            }
        }

        return adjustments;
    }
}
