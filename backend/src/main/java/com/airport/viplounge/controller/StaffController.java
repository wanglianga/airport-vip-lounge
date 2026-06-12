package com.airport.viplounge.controller;

import com.airport.viplounge.common.PageResult;
import com.airport.viplounge.common.Result;
import com.airport.viplounge.entity.Staff;
import com.airport.viplounge.service.StaffService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/staffs")
public class StaffController {

    @Autowired
    private StaffService staffService;

    @GetMapping
    public Result<PageResult<Staff>> list(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status) {

        QueryWrapper<Staff> wrapper = new QueryWrapper<>();
        if (role != null && !role.isEmpty()) {
            wrapper.eq("role", role);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq("status", status);
        }
        wrapper.orderByAsc("name");

        Page<Staff> page = staffService.page(new Page<>(current, size), wrapper);
        PageResult<Staff> pageResult = new PageResult<>(
                page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
        return Result.success(pageResult);
    }

    @PostMapping
    public Result<Staff> create(@RequestBody Staff staff) {
        staffService.save(staff);
        return Result.success(staff);
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String status = body.get("status");
        staffService.updateStatus(id, status);
        return Result.success();
    }
}
