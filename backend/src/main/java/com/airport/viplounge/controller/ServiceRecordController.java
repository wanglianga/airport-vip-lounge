package com.airport.viplounge.controller;

import com.airport.viplounge.common.PageResult;
import com.airport.viplounge.common.Result;
import com.airport.viplounge.entity.ServiceRecord;
import com.airport.viplounge.service.ServiceRecordService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/service-records")
public class ServiceRecordController {

    @Autowired
    private ServiceRecordService serviceRecordService;

    @GetMapping
    public Result<PageResult<ServiceRecord>> list(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) Long passengerId,
            @RequestParam(required = false) String serviceType,
            @RequestParam(required = false) String status) {

        QueryWrapper<ServiceRecord> wrapper = new QueryWrapper<>();
        if (passengerId != null) {
            wrapper.eq("passenger_id", passengerId);
        }
        if (serviceType != null && !serviceType.isEmpty()) {
            wrapper.eq("service_type", serviceType);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq("status", status);
        }
        wrapper.orderByDesc("create_time");

        Page<ServiceRecord> page = serviceRecordService.page(new Page<>(current, size), wrapper);
        PageResult<ServiceRecord> pageResult = new PageResult<>(
                page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
        return Result.success(pageResult);
    }

    @PostMapping
    public Result<ServiceRecord> create(@RequestBody ServiceRecord record) {
        serviceRecordService.save(record);
        return Result.success(record);
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String status = body.get("status");
        serviceRecordService.updateStatus(id, status);
        return Result.success();
    }

    @PutMapping("/{id}/complete")
    public Result<Void> complete(@PathVariable Long id) {
        serviceRecordService.completeService(id);
        return Result.success();
    }
}
