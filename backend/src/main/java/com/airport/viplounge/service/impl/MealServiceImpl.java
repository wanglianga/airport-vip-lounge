package com.airport.viplounge.service.impl;

import com.airport.viplounge.entity.Meal;
import com.airport.viplounge.mapper.MealMapper;
import com.airport.viplounge.service.MealService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MealServiceImpl extends ServiceImpl<MealMapper, Meal> implements MealService {

    @Override
    public List<Meal> listPending() {
        QueryWrapper<Meal> wrapper = new QueryWrapper<>();
        wrapper.in("status", "PENDING", "PREPARING");
        wrapper.orderByAsc("create_time");
        return list(wrapper);
    }

    @Override
    public boolean updateStatus(Long id, String status) {
        Meal meal = new Meal();
        meal.setId(id);
        meal.setStatus(status);
        if ("PREPARING".equals(status)) {
            meal.setPrepareTime(LocalDateTime.now());
        } else if ("READY".equals(status)) {
            meal.setReadyTime(LocalDateTime.now());
        } else if ("DELIVERED".equals(status)) {
            meal.setDeliverTime(LocalDateTime.now());
        }
        return updateById(meal);
    }

    @Override
    public boolean markReady(Long id) {
        Meal meal = new Meal();
        meal.setId(id);
        meal.setStatus("READY");
        meal.setReadyTime(LocalDateTime.now());
        return updateById(meal);
    }
}
