package com.airport.viplounge.service;

import com.airport.viplounge.entity.ServiceAdjustment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ServiceAdjustmentService extends IService<ServiceAdjustment> {

    List<ServiceAdjustment> listByFlightChangeId(Long flightChangeId);

    List<ServiceAdjustment> recalculateServices(Long flightChangeId);
}
