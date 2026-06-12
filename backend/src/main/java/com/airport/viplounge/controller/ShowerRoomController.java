package com.airport.viplounge.controller;

import com.airport.viplounge.common.PageResult;
import com.airport.viplounge.common.Result;
import com.airport.viplounge.entity.ShowerRoom;
import com.airport.viplounge.service.ShowerRoomService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shower-rooms")
public class ShowerRoomController {

    @Autowired
    private ShowerRoomService showerRoomService;

    @GetMapping
    public Result<PageResult<ShowerRoom>> list(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String status) {

        QueryWrapper<ShowerRoom> wrapper = new QueryWrapper<>();
        if (status != null && !status.isEmpty()) {
            wrapper.eq("status", status);
        }
        wrapper.orderByAsc("room_no");

        Page<ShowerRoom> page = showerRoomService.page(new Page<>(current, size), wrapper);
        PageResult<ShowerRoom> pageResult = new PageResult<>(
                page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
        return Result.success(pageResult);
    }

    @GetMapping("/available")
    public Result<List<ShowerRoom>> listAvailable() {
        List<ShowerRoom> rooms = showerRoomService.listAvailable();
        return Result.success(rooms);
    }

    @PostMapping
    public Result<ShowerRoom> create(@RequestBody ShowerRoom room) {
        showerRoomService.save(room);
        return Result.success(room);
    }

    @PutMapping("/{id}/assign")
    public Result<Void> assign(@PathVariable Long id, @RequestBody Map<String, Long> body) {
        Long passengerId = body.get("passengerId");
        showerRoomService.assignRoom(id, passengerId);
        return Result.success();
    }

    @PutMapping("/{id}/release")
    public Result<Void> release(@PathVariable Long id) {
        showerRoomService.releaseRoom(id);
        return Result.success();
    }
}
