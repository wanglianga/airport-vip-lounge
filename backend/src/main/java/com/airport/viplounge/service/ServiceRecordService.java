package com.airport.viplounge.service;

import com.airport.viplounge.entity.ServiceRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ServiceRecordService extends IService<ServiceRecord> {

    List<ServiceRecord> listByPassengerId(Long passengerId);

    boolean updateStatus(Long id, String status);

    boolean completeService(Long id);
}
