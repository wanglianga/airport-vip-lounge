package com.airport.viplounge.service.impl;

import com.airport.viplounge.entity.ShowerRoom;
import com.airport.viplounge.mapper.ShowerRoomMapper;
import com.airport.viplounge.service.ShowerRoomService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShowerRoomServiceImpl extends ServiceImpl<ShowerRoomMapper, ShowerRoom> implements ShowerRoomService {

    @Override
    public List<ShowerRoom> listAvailable() {
        QueryWrapper<ShowerRoom> wrapper = new QueryWrapper<>();
        wrapper.eq("status", "AVAILABLE");
        wrapper.orderByAsc("room_no");
        return list(wrapper);
    }

    @Override
    public boolean assignRoom(Long id, Long passengerId) {
        ShowerRoom room = new ShowerRoom();
        room.setId(id);
        room.setStatus("OCCUPIED");
        room.setPassengerId(passengerId);
        room.setStartTime(LocalDateTime.now());
        return updateById(room);
    }

    @Override
    public boolean releaseRoom(Long id) {
        ShowerRoom room = new ShowerRoom();
        room.setId(id);
        room.setStatus("AVAILABLE");
        room.setPassengerId(null);
        room.setStartTime(null);
        room.setEndTime(null);
        return updateById(room);
    }
}
