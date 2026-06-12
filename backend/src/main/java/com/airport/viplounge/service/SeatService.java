package com.airport.viplounge.service;

import com.airport.viplounge.entity.Seat;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SeatService extends IService<Seat> {

    List<Seat> listAvailable();

    boolean assignSeat(Long id, Long passengerId);

    boolean releaseSeat(Long id);

    boolean updateStatus(Long id, String status);
}
