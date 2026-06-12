package com.airport.viplounge.service;

import com.airport.viplounge.entity.Flight;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface FlightService extends IService<Flight> {

    List<Flight> listTodayFlights();

    boolean updateStatus(Long id, String status);
}
