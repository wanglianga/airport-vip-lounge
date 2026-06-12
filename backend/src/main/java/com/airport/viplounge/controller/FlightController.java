package com.airport.viplounge.controller;

import com.airport.viplounge.common.PageResult;
import com.airport.viplounge.common.Result;
import com.airport.viplounge.entity.Flight;
import com.airport.viplounge.service.FlightService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    @Autowired
    private FlightService flightService;

    @GetMapping
    public Result<PageResult<Flight>> list(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String flightNo,
            @RequestParam(required = false) String status) {

        QueryWrapper<Flight> wrapper = new QueryWrapper<>();
        if (flightNo != null && !flightNo.isEmpty()) {
            wrapper.like("flight_no", flightNo);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq("status", status);
        }
        wrapper.orderByAsc("scheduled_departure");

        Page<Flight> page = flightService.page(new Page<>(current, size), wrapper);
        PageResult<Flight> pageResult = new PageResult<>(
                page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    public Result<Flight> getById(@PathVariable Long id) {
        Flight flight = flightService.getById(id);
        return Result.success(flight);
    }

    @PostMapping
    public Result<Flight> create(@RequestBody Flight flight) {
        flightService.save(flight);
        return Result.success(flight);
    }

    @PutMapping("/{id}")
    public Result<Flight> update(@PathVariable Long id, @RequestBody Flight flight) {
        flight.setId(id);
        flightService.updateById(flight);
        return Result.success(flight);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        flightService.removeById(id);
        return Result.success();
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String status = body.get("status");
        flightService.updateStatus(id, status);
        return Result.success();
    }
}
