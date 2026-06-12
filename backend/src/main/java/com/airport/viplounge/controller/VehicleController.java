package com.airport.viplounge.controller;

import com.airport.viplounge.common.PageResult;
import com.airport.viplounge.common.Result;
import com.airport.viplounge.entity.Vehicle;
import com.airport.viplounge.service.VehicleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping
    public Result<PageResult<Vehicle>> list(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String vehicleType,
            @RequestParam(required = false) String status) {

        QueryWrapper<Vehicle> wrapper = new QueryWrapper<>();
        if (vehicleType != null && !vehicleType.isEmpty()) {
            wrapper.eq("vehicle_type", vehicleType);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq("status", status);
        }
        wrapper.orderByAsc("plate_no");

        Page<Vehicle> page = vehicleService.page(new Page<>(current, size), wrapper);
        PageResult<Vehicle> pageResult = new PageResult<>(
                page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
        return Result.success(pageResult);
    }

    @GetMapping("/available")
    public Result<List<Vehicle>> listAvailable() {
        List<Vehicle> vehicles = vehicleService.listAvailable();
        return Result.success(vehicles);
    }

    @PostMapping
    public Result<Vehicle> create(@RequestBody Vehicle vehicle) {
        vehicleService.save(vehicle);
        return Result.success(vehicle);
    }

    @PutMapping("/{id}/assign")
    public Result<Void> assign(@PathVariable Long id, @RequestBody Map<String, Long> body) {
        Long passengerId = body.get("passengerId");
        Long taskId = body.get("taskId");
        vehicleService.assignTask(id, passengerId, taskId);
        return Result.success();
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String status = body.get("status");
        vehicleService.updateStatus(id, status);
        return Result.success();
    }

    @PutMapping("/{id}/complete")
    public Result<Void> complete(@PathVariable Long id) {
        vehicleService.completeTask(id);
        return Result.success();
    }
}
