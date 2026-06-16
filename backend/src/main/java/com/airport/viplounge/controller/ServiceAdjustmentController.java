package com.airport.viplounge.controller;

import com.airport.viplounge.common.Result;
import com.airport.viplounge.entity.ServiceAdjustment;
import com.airport.viplounge.service.ServiceAdjustmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-adjustments")
public class ServiceAdjustmentController {

    @Autowired
    private ServiceAdjustmentService serviceAdjustmentService;

    @GetMapping("/flight-change/{flightChangeId}")
    public Result<List<ServiceAdjustment>> listByFlightChangeId(@PathVariable Long flightChangeId) {
        List<ServiceAdjustment> adjustments = serviceAdjustmentService.listByFlightChangeId(flightChangeId);
        return Result.success(adjustments);
    }

    @GetMapping("/{id}")
    public Result<ServiceAdjustment> getById(@PathVariable Long id) {
        ServiceAdjustment adjustment = serviceAdjustmentService.getById(id);
        return Result.success(adjustment);
    }

    @PostMapping("/recalculate/{flightChangeId}")
    public Result<List<ServiceAdjustment>> recalculate(@PathVariable Long flightChangeId) {
        List<ServiceAdjustment> adjustments = serviceAdjustmentService.recalculateServices(flightChangeId);
        return Result.success(adjustments);
    }
}
