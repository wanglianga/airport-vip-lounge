package com.airport.viplounge.service.impl;

import com.airport.viplounge.entity.FlightChange;
import com.airport.viplounge.entity.Passenger;
import com.airport.viplounge.entity.PassengerConfirmation;
import com.airport.viplounge.mapper.FlightChangeMapper;
import com.airport.viplounge.mapper.PassengerConfirmationMapper;
import com.airport.viplounge.mapper.PassengerMapper;
import com.airport.viplounge.service.PassengerConfirmationService;
import com.airport.viplounge.service.ServiceAdjustmentService;
import com.airport.viplounge.service.VehicleTaskService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PassengerConfirmationServiceImpl extends ServiceImpl<PassengerConfirmationMapper, PassengerConfirmation>
        implements PassengerConfirmationService {

    @Autowired
    private PassengerMapper passengerMapper;

    @Autowired
    private FlightChangeMapper flightChangeMapper;

    @Autowired
    private ServiceAdjustmentService serviceAdjustmentService;

    @Autowired
    private VehicleTaskService vehicleTaskService;

    @Override
    public List<PassengerConfirmation> listByFlightChangeId(Long flightChangeId) {
        QueryWrapper<PassengerConfirmation> wrapper = new QueryWrapper<>();
        wrapper.eq("flight_change_id", flightChangeId);
        wrapper.orderByAsc("create_time");
        return list(wrapper);
    }

    @Override
    @Transactional
    public PassengerConfirmation confirmPassenger(Long id, Map<String, Object> confirmData,
                                                  String confirmedBy, String confirmedByName) {
        PassengerConfirmation confirmation = getById(id);
        if (confirmation == null) {
            throw new RuntimeException("确认记录不存在");
        }

        String passengerChoice = (String) confirmData.get("passengerChoice");
        String newFlightNo = (String) confirmData.get("newFlightNo");
        Boolean needPackedMeal = (Boolean) confirmData.get("needPackedMeal");
        String remark = (String) confirmData.get("remark");

        confirmation.setPassengerChoice(passengerChoice);
        confirmation.setNewFlightNo(newFlightNo);
        confirmation.setNeedPackedMeal(needPackedMeal != null ? needPackedMeal : false);
        confirmation.setConfirmStatus("CONFIRMED");
        confirmation.setConfirmedBy(confirmedBy);
        confirmation.setConfirmedByName(confirmedByName);
        confirmation.setConfirmTime(LocalDateTime.now());
        if (remark != null) {
            confirmation.setRemark(remark);
        }
        confirmation.setUpdateTime(LocalDateTime.now());

        updateById(confirmation);

        updatePassengerStatus(confirmation, passengerChoice, newFlightNo);

        if ("CONTINUE".equals(passengerChoice)) {
            vehicleTaskService.recalculateVehicleTasks(confirmation.getFlightChangeId());
        }

        return confirmation;
    }

    private void updatePassengerStatus(PassengerConfirmation confirmation, String choice, String newFlightNo) {
        Passenger passenger = passengerMapper.selectById(confirmation.getPassengerId());
        if (passenger != null) {
            if ("REBOOK".equals(choice) && newFlightNo != null) {
                passenger.setFlightNo(newFlightNo);
            }
            if (Boolean.TRUE.equals(confirmation.getNeedPackedMeal())) {
                passenger.setRemark(passenger.getRemark() == null ? "需要打包餐食" : passenger.getRemark() + "; 需要打包餐食");
            }
            passenger.setUpdateTime(LocalDateTime.now());
            passengerMapper.updateById(passenger);
        }
    }

    @Override
    public Map<String, Object> getConfirmationSummary(Long flightChangeId) {
        List<PassengerConfirmation> confirmations = listByFlightChangeId(flightChangeId);

        FlightChange flightChange = flightChangeMapper.selectById(flightChangeId);

        int total = confirmations.size();
        int confirmed = 0;
        int pending = 0;
        int continueStay = 0;
        int rebook = 0;
        int packedMeal = 0;

        for (PassengerConfirmation pc : confirmations) {
            if ("CONFIRMED".equals(pc.getConfirmStatus())) {
                confirmed++;
                if ("CONTINUE".equals(pc.getPassengerChoice())) {
                    continueStay++;
                } else if ("REBOOK".equals(pc.getPassengerChoice())) {
                    rebook++;
                }
                if (Boolean.TRUE.equals(pc.getNeedPackedMeal())) {
                    packedMeal++;
                }
            } else {
                pending++;
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("flightChange", flightChange);
        result.put("total", total);
        result.put("confirmed", confirmed);
        result.put("pending", pending);
        result.put("continueStay", continueStay);
        result.put("rebook", rebook);
        result.put("packedMeal", packedMeal);
        result.put("confirmations", confirmations);

        return result;
    }
}
