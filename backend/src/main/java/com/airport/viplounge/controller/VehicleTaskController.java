package com.airport.viplounge.controller;

import com.airport.viplounge.common.PageResult;
import com.airport.viplounge.common.Result;
import com.airport.viplounge.entity.VehicleTask;
import com.airport.viplounge.service.VehicleTaskService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vehicle-tasks")
public class VehicleTaskController {

    @Autowired
    private VehicleTaskService vehicleTaskService;

    @GetMapping
    public Result<PageResult<VehicleTask>> list(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) Long flightId,
            @RequestParam(required = false) Long flightChangeId,
            @RequestParam(required = false) String status) {

        QueryWrapper<VehicleTask> wrapper = new QueryWrapper<>();
        if (flightId != null) {
            wrapper.eq("flight_id", flightId);
        }
        if (flightChangeId != null) {
            wrapper.eq("flight_change_id", flightChangeId);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq("status", status);
        }
        wrapper.orderByDesc("create_time");

        Page<VehicleTask> page = vehicleTaskService.page(new Page<>(current, size), wrapper);
        PageResult<VehicleTask> pageResult = new PageResult<>(
                page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    public Result<VehicleTask> getById(@PathVariable Long id) {
        VehicleTask task = vehicleTaskService.getById(id);
        return Result.success(task);
    }

    @GetMapping("/flight-change/{flightChangeId}")
    public Result<List<VehicleTask>> listByFlightChangeId(@PathVariable Long flightChangeId) {
        List<VehicleTask> tasks = vehicleTaskService.listByFlightChangeId(flightChangeId);
        return Result.success(tasks);
    }

    @GetMapping("/flight/{flightId}")
    public Result<List<VehicleTask>> listByFlightId(@PathVariable Long flightId) {
        List<VehicleTask> tasks = vehicleTaskService.listByFlightId(flightId);
        return Result.success(tasks);
    }

    @PutMapping("/{id}")
    public Result<VehicleTask> update(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        VehicleTask task = vehicleTaskService.updateVehicleTask(id, body);
        return Result.success(task);
    }

    @PostMapping("/recalculate/{flightChangeId}")
    public Result<List<VehicleTask>> recalculate(@PathVariable Long flightChangeId) {
        List<VehicleTask> tasks = vehicleTaskService.recalculateVehicleTasks(flightChangeId);
        return Result.success(tasks);
    }
}
