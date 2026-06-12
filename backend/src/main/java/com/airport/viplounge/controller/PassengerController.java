package com.airport.viplounge.controller;

import com.airport.viplounge.common.PageResult;
import com.airport.viplounge.common.Result;
import com.airport.viplounge.entity.Passenger;
import com.airport.viplounge.service.PassengerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/passengers")
public class PassengerController {

    @Autowired
    private PassengerService passengerService;

    @GetMapping
    public Result<PageResult<Passenger>> list(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String tier,
            @RequestParam(required = false) String flightNo) {

        QueryWrapper<Passenger> wrapper = new QueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            wrapper.like("name", name);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq("status", status);
        }
        if (tier != null && !tier.isEmpty()) {
            wrapper.eq("tier", tier);
        }
        if (flightNo != null && !flightNo.isEmpty()) {
            wrapper.like("flight_no", flightNo);
        }
        wrapper.orderByDesc("create_time");

        Page<Passenger> page = passengerService.page(new Page<>(current, size), wrapper);
        PageResult<Passenger> pageResult = new PageResult<>(
                page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    public Result<Passenger> getById(@PathVariable Long id) {
        Passenger passenger = passengerService.getById(id);
        return Result.success(passenger);
    }

    @PostMapping
    public Result<Passenger> create(@RequestBody Passenger passenger) {
        passengerService.save(passenger);
        return Result.success(passenger);
    }

    @PutMapping("/{id}")
    public Result<Passenger> update(@PathVariable Long id, @RequestBody Passenger passenger) {
        passenger.setId(id);
        passengerService.updateById(passenger);
        return Result.success(passenger);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        passengerService.removeById(id);
        return Result.success();
    }

    @PostMapping("/batch-import")
    public Result<Void> batchImport(@RequestBody List<Passenger> passengers) {
        passengerService.batchImport(passengers);
        return Result.success();
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String status = body.get("status");
        passengerService.updateStatus(id, status);
        return Result.success();
    }

    @PutMapping("/{id}/check-in")
    public Result<Void> checkIn(@PathVariable Long id) {
        passengerService.checkIn(id);
        return Result.success();
    }

    @PutMapping("/{id}/check-out")
    public Result<Void> checkOut(@PathVariable Long id) {
        passengerService.checkOut(id);
        return Result.success();
    }
}
