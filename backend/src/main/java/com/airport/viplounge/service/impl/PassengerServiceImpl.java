package com.airport.viplounge.service.impl;

import com.airport.viplounge.entity.Passenger;
import com.airport.viplounge.mapper.PassengerMapper;
import com.airport.viplounge.service.PassengerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PassengerServiceImpl extends ServiceImpl<PassengerMapper, Passenger> implements PassengerService {

    @Override
    public List<Passenger> listInLounge() {
        QueryWrapper<Passenger> wrapper = new QueryWrapper<>();
        wrapper.in("status", "ARRIVED", "IN_LOUNGE", "SERVING", "BOARDING");
        wrapper.orderByDesc("check_in_time");
        return list(wrapper);
    }

    @Override
    public boolean checkIn(Long id) {
        Passenger passenger = new Passenger();
        passenger.setId(id);
        passenger.setStatus("IN_LOUNGE");
        passenger.setCheckInTime(LocalDateTime.now());
        return updateById(passenger);
    }

    @Override
    public boolean checkOut(Long id) {
        Passenger passenger = new Passenger();
        passenger.setId(id);
        passenger.setStatus("LEFT");
        return updateById(passenger);
    }

    @Override
    public boolean updateStatus(Long id, String status) {
        Passenger passenger = new Passenger();
        passenger.setId(id);
        passenger.setStatus(status);
        return updateById(passenger);
    }

    @Override
    public boolean batchImport(List<Passenger> passengers) {
        return saveBatch(passengers);
    }
}
