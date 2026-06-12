package com.airport.viplounge.service.impl;

import com.airport.viplounge.entity.Vehicle;
import com.airport.viplounge.mapper.VehicleMapper;
import com.airport.viplounge.service.VehicleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleServiceImpl extends ServiceImpl<VehicleMapper, Vehicle> implements VehicleService {

    @Override
    public List<Vehicle> listAvailable() {
        QueryWrapper<Vehicle> wrapper = new QueryWrapper<>();
        wrapper.eq("status", "AVAILABLE");
        wrapper.orderByAsc("plate_no");
        return list(wrapper);
    }

    @Override
    public boolean assignTask(Long id, Long passengerId, Long taskId) {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(id);
        vehicle.setStatus("ASSIGNED");
        vehicle.setCurrentPassengerId(passengerId);
        vehicle.setCurrentTaskId(taskId);
        return updateById(vehicle);
    }

    @Override
    public boolean updateStatus(Long id, String status) {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(id);
        vehicle.setStatus(status);
        return updateById(vehicle);
    }

    @Override
    public boolean completeTask(Long id) {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(id);
        vehicle.setStatus("RETURNING");
        vehicle.setCurrentTaskId(null);
        vehicle.setCurrentPassengerId(null);
        return updateById(vehicle);
    }
}
