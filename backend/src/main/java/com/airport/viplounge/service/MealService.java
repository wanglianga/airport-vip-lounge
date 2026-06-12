package com.airport.viplounge.service;

import com.airport.viplounge.entity.Meal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface MealService extends IService<Meal> {

    List<Meal> listPending();

    boolean updateStatus(Long id, String status);

    boolean markReady(Long id);
}
