package com.airport.viplounge.controller;

import com.airport.viplounge.common.PageResult;
import com.airport.viplounge.common.Result;
import com.airport.viplounge.entity.MeetingRoom;
import com.airport.viplounge.service.MeetingRoomService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/meeting-rooms")
public class MeetingRoomController {

    @Autowired
    private MeetingRoomService meetingRoomService;

    @GetMapping
    public Result<PageResult<MeetingRoom>> list(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String status) {

        QueryWrapper<MeetingRoom> wrapper = new QueryWrapper<>();
        if (status != null && !status.isEmpty()) {
            wrapper.eq("status", status);
        }
        wrapper.orderByAsc("room_name");

        Page<MeetingRoom> page = meetingRoomService.page(new Page<>(current, size), wrapper);
        PageResult<MeetingRoom> pageResult = new PageResult<>(
                page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
        return Result.success(pageResult);
    }

    @GetMapping("/available")
    public Result<List<MeetingRoom>> listAvailable() {
        List<MeetingRoom> rooms = meetingRoomService.listAvailable();
        return Result.success(rooms);
    }

    @PostMapping
    public Result<MeetingRoom> create(@RequestBody MeetingRoom room) {
        meetingRoomService.save(room);
        return Result.success(room);
    }

    @PutMapping("/{id}/book")
    public Result<Void> book(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Long passengerId = body.get("passengerId") != null ? ((Number) body.get("passengerId")).longValue() : null;
        String purpose = body.get("purpose") != null ? body.get("purpose").toString() : null;
        meetingRoomService.bookRoom(id, passengerId, purpose);
        return Result.success();
    }

    @PutMapping("/{id}/release")
    public Result<Void> release(@PathVariable Long id) {
        meetingRoomService.releaseRoom(id);
        return Result.success();
    }
}
