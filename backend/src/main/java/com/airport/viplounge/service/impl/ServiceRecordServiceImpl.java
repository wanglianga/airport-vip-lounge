package com.airport.viplounge.service.impl;

import com.airport.viplounge.entity.ServiceRecord;
import com.airport.viplounge.mapper.ServiceRecordMapper;
import com.airport.viplounge.service.ServiceRecordService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ServiceRecordServiceImpl extends ServiceImpl<ServiceRecordMapper, ServiceRecord> implements ServiceRecordService {

    @Override
    public List<ServiceRecord> listByPassengerId(Long passengerId) {
        QueryWrapper<ServiceRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("passenger_id", passengerId);
        wrapper.orderByDesc("create_time");
        return list(wrapper);
    }

    @Override
    public boolean updateStatus(Long id, String status) {
        ServiceRecord record = new ServiceRecord();
        record.setId(id);
        record.setStatus(status);
        if ("IN_PROGRESS".equals(status)) {
            record.setStartTime(LocalDateTime.now());
        } else if ("COMPLETED".equals(status) || "CANCELLED".equals(status)) {
            record.setEndTime(LocalDateTime.now());
        }
        return updateById(record);
    }

    @Override
    public boolean completeService(Long id) {
        ServiceRecord record = new ServiceRecord();
        record.setId(id);
        record.setStatus("COMPLETED");
        record.setEndTime(LocalDateTime.now());
        return updateById(record);
    }
}
