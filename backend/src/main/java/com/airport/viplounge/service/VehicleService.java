package com.airport.viplounge.service;

import com.airport.viplounge.entity.Vehicle;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface VehicleService extends IService<Vehicle> {

    List<Vehicle> listAvailable();

    boolean assignTask(Long id, Long passengerId, Long taskId);

    boolean updateStatus(Long id, String status);

    boolean completeTask(Long id);
}
