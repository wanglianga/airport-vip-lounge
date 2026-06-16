package com.airport.viplounge.controller;

import com.airport.viplounge.common.PageResult;
import com.airport.viplounge.common.Result;
import com.airport.viplounge.entity.FlightChange;
import com.airport.viplounge.service.FlightChangeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/flight-changes")
public class FlightChangeController {

    @Autowired
    private FlightChangeService flightChangeService;

    @GetMapping
    public Result<PageResult<FlightChange>> list(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) Long flightId,
            @RequestParam(required = false) String changeType) {

        QueryWrapper<FlightChange> wrapper = new QueryWrapper<>();
        if (flightId != null) {
            wrapper.eq("flight_id", flightId);
        }
        if (changeType != null && !changeType.isEmpty()) {
            wrapper.eq("change_type", changeType);
        }
        wrapper.orderByDesc("create_time");

        Page<FlightChange> page = flightChangeService.page(new Page<>(current, size), wrapper);
        PageResult<FlightChange> pageResult = new PageResult<>(
                page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    public Result<Map<String, Object>> getById(@PathVariable Long id) {
        Map<String, Object> detail = flightChangeService.getFlightChangeDetail(id);
        return Result.success(detail);
    }

    @PostMapping
    public Result<FlightChange> create(@RequestBody Map<String, Object> body) {
        Long flightId = Long.valueOf(body.get("flightId").toString());
        String changeType = (String) body.get("changeType");
        String notifiedBy = body.get("notifiedBy") != null ? (String) body.get("notifiedBy") : "AIRLINE";
        String notifiedByName = body.get("notifiedByName") != null ? (String) body.get("notifiedByName") : "航司系统";

        FlightChange flightChange = flightChangeService.createFlightChange(flightId, changeType, body, notifiedBy, notifiedByName);
        return Result.success(flightChange);
    }

    @GetMapping("/flight/{flightId}")
    public Result<List<FlightChange>> listByFlightId(@PathVariable Long flightId) {
        List<FlightChange> changes = flightChangeService.listByFlightId(flightId);
        return Result.success(changes);
    }
}
