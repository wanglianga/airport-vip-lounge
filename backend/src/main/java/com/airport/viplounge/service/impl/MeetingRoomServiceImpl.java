package com.airport.viplounge.service.impl;

import com.airport.viplounge.entity.MeetingRoom;
import com.airport.viplounge.mapper.MeetingRoomMapper;
import com.airport.viplounge.service.MeetingRoomService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MeetingRoomServiceImpl extends ServiceImpl<MeetingRoomMapper, MeetingRoom> implements MeetingRoomService {

    @Override
    public List<MeetingRoom> listAvailable() {
        QueryWrapper<MeetingRoom> wrapper = new QueryWrapper<>();
        wrapper.eq("status", "AVAILABLE");
        wrapper.orderByAsc("room_name");
        return list(wrapper);
    }

    @Override
    public boolean bookRoom(Long id, Long passengerId, String purpose) {
        MeetingRoom room = new MeetingRoom();
        room.setId(id);
        room.setStatus("OCCUPIED");
        room.setPassengerId(passengerId);
        room.setStartTime(LocalDateTime.now());
        room.setPurpose(purpose);
        return updateById(room);
    }

    @Override
    public boolean releaseRoom(Long id) {
        MeetingRoom room = new MeetingRoom();
        room.setId(id);
        room.setStatus("AVAILABLE");
        room.setPassengerId(null);
        room.setStartTime(null);
        room.setEndTime(null);
        room.setPurpose(null);
        return updateById(room);
    }
}
