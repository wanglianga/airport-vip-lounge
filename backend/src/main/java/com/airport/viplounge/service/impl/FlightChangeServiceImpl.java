package com.airport.viplounge.service.impl;

import com.airport.viplounge.entity.Flight;
import com.airport.viplounge.entity.FlightChange;
import com.airport.viplounge.entity.Passenger;
import com.airport.viplounge.entity.PassengerConfirmation;
import com.airport.viplounge.entity.ServiceAdjustment;
import com.airport.viplounge.mapper.FlightChangeMapper;
import com.airport.viplounge.mapper.FlightMapper;
import com.airport.viplounge.mapper.PassengerConfirmationMapper;
import com.airport.viplounge.mapper.PassengerMapper;
import com.airport.viplounge.service.FlightChangeService;
import com.airport.viplounge.service.ServiceAdjustmentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FlightChangeServiceImpl extends ServiceImpl<FlightChangeMapper, FlightChange> implements FlightChangeService {

    @Autowired
    private FlightMapper flightMapper;

    @Autowired
    private PassengerMapper passengerMapper;

    @Autowired
    private PassengerConfirmationMapper passengerConfirmationMapper;

    @Autowired
    private ServiceAdjustmentService serviceAdjustmentService;

    @Override
    @Transactional
    public FlightChange createFlightChange(Long flightId, String changeType, Map<String, Object> changeData,
                                           String notifiedBy, String notifiedByName) {
        Flight oldFlight = flightMapper.selectById(flightId);
        if (oldFlight == null) {
            throw new RuntimeException("航班不存在");
        }

        FlightChange flightChange = new FlightChange();
        flightChange.setFlightId(flightId);
        flightChange.setFlightNo(oldFlight.getFlightNo());
        flightChange.setChangeType(changeType);
        flightChange.setOldStatus(oldFlight.getStatus());
        flightChange.setOldGate(oldFlight.getGate());
        flightChange.setOldFarStand(oldFlight.getIsFarStand());
        flightChange.setOldScheduledDeparture(oldFlight.getScheduledDeparture());
        flightChange.setNotifiedBy(notifiedBy);
        flightChange.setNotifiedByName(notifiedByName);
        flightChange.setCreateTime(LocalDateTime.now());
        flightChange.setUpdateTime(LocalDateTime.now());

        Flight updatedFlight = new Flight();
        updatedFlight.setId(flightId);

        if ("STATUS".equals(changeType)) {
            String newStatus = (String) changeData.get("newStatus");
            flightChange.setNewStatus(newStatus);
            updatedFlight.setStatus(newStatus);
        } else if ("GATE".equals(changeType)) {
            String newGate = (String) changeData.get("newGate");
            flightChange.setNewGate(newGate);
            updatedFlight.setGate(newGate);
        } else if ("FAR_STAND".equals(changeType)) {
            Boolean newFarStand = (Boolean) changeData.get("newFarStand");
            flightChange.setNewFarStand(newFarStand);
            updatedFlight.setIsFarStand(newFarStand);
        } else if ("DELAY".equals(changeType)) {
            String newDepartureStr = (String) changeData.get("newScheduledDeparture");
            LocalDateTime newDeparture = LocalDateTime.parse(newDepartureStr);
            flightChange.setNewScheduledDeparture(newDeparture);
            flightChange.setNewStatus("DELAYED");
            updatedFlight.setScheduledDeparture(newDeparture);
            updatedFlight.setStatus("DELAYED");
        }

        if (changeData.get("remark") != null) {
            flightChange.setRemark((String) changeData.get("remark"));
        }

        save(flightChange);
        flightMapper.updateById(updatedFlight);

        createPassengerConfirmations(flightChange);
        serviceAdjustmentService.recalculateServices(flightChange.getId());

        return flightChange;
    }

    private void createPassengerConfirmations(FlightChange flightChange) {
        QueryWrapper<Passenger> wrapper = new QueryWrapper<>();
        wrapper.eq("flight_no", flightChange.getFlightNo());
        wrapper.in("status", "ARRIVED", "IN_LOUNGE", "SERVING");
        List<Passenger> passengers = passengerMapper.selectList(wrapper);

        for (Passenger passenger : passengers) {
            PassengerConfirmation confirmation = new PassengerConfirmation();
            confirmation.setFlightChangeId(flightChange.getId());
            confirmation.setFlightId(flightChange.getFlightId());
            confirmation.setPassengerId(passenger.getId());
            confirmation.setPassengerName(passenger.getName());
            confirmation.setPassengerPhone(passenger.getPhone());
            confirmation.setConfirmStatus("PENDING");
            confirmation.setNeedPackedMeal(false);
            confirmation.setCreateTime(LocalDateTime.now());
            confirmation.setUpdateTime(LocalDateTime.now());
            passengerConfirmationMapper.insert(confirmation);
        }
    }

    @Override
    public List<FlightChange> listByFlightId(Long flightId) {
        QueryWrapper<FlightChange> wrapper = new QueryWrapper<>();
        wrapper.eq("flight_id", flightId);
        wrapper.orderByDesc("create_time");
        return list(wrapper);
    }

    @Override
    public Map<String, Object> getFlightChangeDetail(Long id) {
        FlightChange flightChange = getById(id);
        if (flightChange == null) {
            throw new RuntimeException("航班变更记录不存在");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("flightChange", flightChange);

        QueryWrapper<PassengerConfirmation> confirmWrapper = new QueryWrapper<>();
        confirmWrapper.eq("flight_change_id", id);
        List<PassengerConfirmation> confirmations = passengerConfirmationMapper.selectList(confirmWrapper);
        result.put("confirmations", confirmations);

        List<ServiceAdjustment> adjustments = serviceAdjustmentService.listByFlightChangeId(id);
        result.put("adjustments", adjustments);

        int confirmedCount = 0;
        int pendingCount = 0;
        int continueCount = 0;
        int rebookCount = 0;
        int packedMealCount = 0;

        for (PassengerConfirmation pc : confirmations) {
            if ("CONFIRMED".equals(pc.getConfirmStatus())) {
                confirmedCount++;
                if ("CONTINUE".equals(pc.getPassengerChoice())) {
                    continueCount++;
                } else if ("REBOOK".equals(pc.getPassengerChoice())) {
                    rebookCount++;
                }
                if (Boolean.TRUE.equals(pc.getNeedPackedMeal())) {
                    packedMealCount++;
                }
            } else {
                pendingCount++;
            }
        }

        Map<String, Integer> summary = new HashMap<>();
        summary.put("total", confirmations.size());
        summary.put("confirmed", confirmedCount);
        summary.put("pending", pendingCount);
        summary.put("continue", continueCount);
        summary.put("rebook", rebookCount);
        summary.put("packedMeal", packedMealCount);
        result.put("summary", summary);

        return result;
    }
}
