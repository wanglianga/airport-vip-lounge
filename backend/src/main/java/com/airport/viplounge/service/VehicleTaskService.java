package com.airport.viplounge.service;

import com.airport.viplounge.entity.VehicleTask;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface VehicleTaskService extends IService<VehicleTask> {

    List<VehicleTask> listByFlightChangeId(Long flightChangeId);

    List<VehicleTask> listByFlightId(Long flightId);

    VehicleTask updateVehicleTask(Long taskId, Map<String, Object> updateData);

    List<VehicleTask> recalculateVehicleTasks(Long flightChangeId);
}
