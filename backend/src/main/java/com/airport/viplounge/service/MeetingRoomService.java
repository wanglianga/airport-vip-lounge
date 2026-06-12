package com.airport.viplounge.service;

import com.airport.viplounge.entity.MeetingRoom;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface MeetingRoomService extends IService<MeetingRoom> {

    List<MeetingRoom> listAvailable();

    boolean bookRoom(Long id, Long passengerId, String purpose);

    boolean releaseRoom(Long id);
}
