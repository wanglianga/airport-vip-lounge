package com.airport.viplounge.service.impl;

import com.airport.viplounge.entity.Seat;
import com.airport.viplounge.mapper.SeatMapper;
import com.airport.viplounge.service.SeatService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SeatServiceImpl extends ServiceImpl<SeatMapper, Seat> implements SeatService {

    @Override
    public List<Seat> listAvailable() {
        QueryWrapper<Seat> wrapper = new QueryWrapper<>();
        wrapper.eq("status", "AVAILABLE");
        wrapper.orderByAsc("seat_no");
        return list(wrapper);
    }

    @Override
    public boolean assignSeat(Long id, Long passengerId) {
        Seat seat = new Seat();
        seat.setId(id);
        seat.setStatus("OCCUPIED");
        seat.setPassengerId(passengerId);
        seat.setCheckInTime(LocalDateTime.now());
        return updateById(seat);
    }

    @Override
    public boolean releaseSeat(Long id) {
        Seat seat = new Seat();
        seat.setId(id);
        seat.setStatus("AVAILABLE");
        seat.setPassengerId(null);
        seat.setCheckInTime(null);
        return updateById(seat);
    }

    @Override
    public boolean updateStatus(Long id, String status) {
        Seat seat = new Seat();
        seat.setId(id);
        seat.setStatus(status);
        return updateById(seat);
    }
}
