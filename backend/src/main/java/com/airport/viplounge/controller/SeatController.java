package com.airport.viplounge.controller;

import com.airport.viplounge.common.PageResult;
import com.airport.viplounge.common.Result;
import com.airport.viplounge.entity.Seat;
import com.airport.viplounge.service.SeatService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/seats")
public class SeatController {

    @Autowired
    private SeatService seatService;

    @GetMapping
    public Result<PageResult<Seat>> list(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String area,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status) {

        QueryWrapper<Seat> wrapper = new QueryWrapper<>();
        if (area != null && !area.isEmpty()) {
            wrapper.eq("area", area);
        }
        if (type != null && !type.isEmpty()) {
            wrapper.eq("type", type);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq("status", status);
        }
        wrapper.orderByAsc("seat_no");

        Page<Seat> page = seatService.page(new Page<>(current, size), wrapper);
        PageResult<Seat> pageResult = new PageResult<>(
                page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
        return Result.success(pageResult);
    }

    @GetMapping("/available")
    public Result<List<Seat>> listAvailable() {
        List<Seat> seats = seatService.listAvailable();
        return Result.success(seats);
    }

    @PostMapping
    public Result<Seat> create(@RequestBody Seat seat) {
        seatService.save(seat);
        return Result.success(seat);
    }

    @PutMapping("/{id}/assign")
    public Result<Void> assign(@PathVariable Long id, @RequestBody Map<String, Long> body) {
        Long passengerId = body.get("passengerId");
        seatService.assignSeat(id, passengerId);
        return Result.success();
    }

    @PutMapping("/{id}/release")
    public Result<Void> release(@PathVariable Long id) {
        seatService.releaseSeat(id);
        return Result.success();
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String status = body.get("status");
        seatService.updateStatus(id, status);
        return Result.success();
    }
}
