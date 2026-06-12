package com.airport.viplounge.service.impl;

import com.airport.viplounge.entity.Flight;
import com.airport.viplounge.mapper.FlightMapper;
import com.airport.viplounge.service.FlightService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FlightServiceImpl extends ServiceImpl<FlightMapper, Flight> implements FlightService {

    @Override
    public List<Flight> listTodayFlights() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59);
        QueryWrapper<Flight> wrapper = new QueryWrapper<>();
        wrapper.between("scheduled_departure", startOfDay, endOfDay);
        wrapper.orderByAsc("scheduled_departure");
        return list(wrapper);
    }

    @Override
    public boolean updateStatus(Long id, String status) {
        Flight flight = new Flight();
        flight.setId(id);
        flight.setStatus(status);
        if ("DEPARTED".equals(status)) {
            flight.setActualDeparture(LocalDateTime.now());
        }
        return updateById(flight);
    }
}
