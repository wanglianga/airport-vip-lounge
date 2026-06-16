package com.airport.viplounge.service.impl;

import com.airport.viplounge.entity.*;
import com.airport.viplounge.mapper.*;
import com.airport.viplounge.service.VehicleTaskService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class VehicleTaskServiceImpl extends ServiceImpl<VehicleTaskMapper, VehicleTask> implements VehicleTaskService {

    @Autowired
    private FlightChangeMapper flightChangeMapper;

    @Autowired
    private FlightMapper flightMapper;

    @Autowired
    private PassengerMapper passengerMapper;

    @Autowired
    private VehicleMapper vehicleMapper;

    @Autowired
    private PassengerConfirmationMapper passengerConfirmationMapper;

    @Override
    public List<VehicleTask> listByFlightChangeId(Long flightChangeId) {
        QueryWrapper<VehicleTask> wrapper = new QueryWrapper<>();
        wrapper.eq("flight_change_id", flightChangeId);
        wrapper.orderByDesc("create_time");
        return list(wrapper);
    }

    @Override
    public List<VehicleTask> listByFlightId(Long flightId) {
        QueryWrapper<VehicleTask> wrapper = new QueryWrapper<>();
        wrapper.eq("flight_id", flightId);
        wrapper.orderByDesc("create_time");
        return list(wrapper);
    }

    @Override
    @Transactional
    public VehicleTask updateVehicleTask(Long taskId, Map<String, Object> updateData) {
        VehicleTask task = getById(taskId);
        if (task == null) {
            throw new RuntimeException("车辆任务不存在");
        }

        if (updateData.containsKey("status")) {
            String status = (String) updateData.get("status");
            task.setStatus(status);

            if ("IN_PROGRESS".equals(status) && task.getActualPickupTime() == null) {
                task.setActualPickupTime(LocalDateTime.now());
            }
            if ("COMPLETED".equals(status)) {
                task.setActualArrivalTime(LocalDateTime.now());
            }
        }

        if (updateData.containsKey("scheduledPickupTime")) {
            String timeStr = (String) updateData.get("scheduledPickupTime");
            task.setScheduledPickupTime(LocalDateTime.parse(timeStr));
        }

        if (updateData.containsKey("dropoffLocation")) {
            task.setDropoffLocation((String) updateData.get("dropoffLocation"));
        }

        if (updateData.containsKey("remark")) {
            task.setRemark((String) updateData.get("remark"));
        }

        task.setUpdateTime(LocalDateTime.now());
        updateById(task);

        updateVehicleStatus(task);

        return task;
    }

    private void updateVehicleStatus(VehicleTask task) {
        if (task.getVehicleId() != null) {
            Vehicle vehicle = vehicleMapper.selectById(task.getVehicleId());
            if (vehicle != null) {
                if ("ASSIGNED".equals(task.getStatus())) {
                    vehicle.setStatus("ASSIGNED");
                    vehicle.setCurrentTaskId(task.getId());
                    vehicle.setCurrentPassengerId(task.getPassengerId());
                } else if ("IN_PROGRESS".equals(task.getStatus())) {
                    vehicle.setStatus("ON_ROUTE");
                } else if ("COMPLETED".equals(task.getStatus())) {
                    vehicle.setStatus("RETURNING");
                    vehicle.setCurrentTaskId(null);
                    vehicle.setCurrentPassengerId(null);
                }
                vehicle.setUpdateTime(LocalDateTime.now());
                vehicleMapper.updateById(vehicle);
            }
        }
    }

    @Override
    @Transactional
    public List<VehicleTask> recalculateVehicleTasks(Long flightChangeId) {
        FlightChange flightChange = flightChangeMapper.selectById(flightChangeId);
        if (flightChange == null) {
            throw new RuntimeException("航班变更记录不存在");
        }

        Flight flight = flightMapper.selectById(flightChange.getFlightId());
        if (flight == null) {
            throw new RuntimeException("航班不存在");
        }

        QueryWrapper<PassengerConfirmation> confirmWrapper = new QueryWrapper<>();
        confirmWrapper.eq("flight_change_id", flightChangeId);
        confirmWrapper.eq("confirm_status", "CONFIRMED");
        confirmWrapper.eq("passenger_choice", "CONTINUE");
        List<PassengerConfirmation> confirmations = passengerConfirmationMapper.selectList(confirmWrapper);

        List<VehicleTask> updatedTasks = new ArrayList<>();

        QueryWrapper<VehicleTask> taskWrapper = new QueryWrapper<>();
        taskWrapper.eq("flight_id", flightChange.getFlightId());
        taskWrapper.in("status", "PENDING", "ASSIGNED");
        List<VehicleTask> existingTasks = list(taskWrapper);

        for (VehicleTask task : existingTasks) {
            boolean passengerConfirmed = false;
            for (PassengerConfirmation pc : confirmations) {
                if (pc.getPassengerId().equals(task.getPassengerId())) {
                    passengerConfirmed = true;
                    break;
                }
            }

            if (passengerConfirmed) {
                if (flightChange.getNewScheduledDeparture() != null) {
                    LocalDateTime newPickupTime = flightChange.getNewScheduledDeparture().minusMinutes(60);
                    task.setScheduledPickupTime(newPickupTime);
                    task.setRemark("航班延误，重新安排送机时间");
                    task.setTaskSource("FLIGHT_CHANGE");
                    task.setFlightChangeId(flightChangeId);
                    task.setUpdateTime(LocalDateTime.now());
                    updateById(task);
                    updatedTasks.add(task);
                }

                if (flightChange.getNewGate() != null) {
                    task.setDropoffLocation(flight.getGate() + "登机口");
                    task.setUpdateTime(LocalDateTime.now());
                    updateById(task);
                    if (!updatedTasks.contains(task)) {
                        updatedTasks.add(task);
                    }
                }
            }
        }

        return updatedTasks;
    }
}
