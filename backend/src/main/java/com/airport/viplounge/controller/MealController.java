package com.airport.viplounge.controller;

import com.airport.viplounge.common.PageResult;
import com.airport.viplounge.common.Result;
import com.airport.viplounge.entity.Meal;
import com.airport.viplounge.service.MealService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/meals")
public class MealController {

    @Autowired
    private MealService mealService;

    @GetMapping
    public Result<PageResult<Meal>> list(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String mealType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String flightNo) {

        QueryWrapper<Meal> wrapper = new QueryWrapper<>();
        if (mealType != null && !mealType.isEmpty()) {
            wrapper.eq("meal_type", mealType);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq("status", status);
        }
        if (flightNo != null && !flightNo.isEmpty()) {
            wrapper.like("flight_no", flightNo);
        }
        wrapper.orderByDesc("create_time");

        Page<Meal> page = mealService.page(new Page<>(current, size), wrapper);
        PageResult<Meal> pageResult = new PageResult<>(
                page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
        return Result.success(pageResult);
    }

    @GetMapping("/pending")
    public Result<List<Meal>> listPending() {
        List<Meal> meals = mealService.listPending();
        return Result.success(meals);
    }

    @PostMapping
    public Result<Meal> create(@RequestBody Meal meal) {
        mealService.save(meal);
        return Result.success(meal);
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String status = body.get("status");
        mealService.updateStatus(id, status);
        return Result.success();
    }

    @PutMapping("/{id}/ready")
    public Result<Void> markReady(@PathVariable Long id) {
        mealService.markReady(id);
        return Result.success();
    }
}
