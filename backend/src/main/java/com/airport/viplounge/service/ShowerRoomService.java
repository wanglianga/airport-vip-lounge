package com.airport.viplounge.service;

import com.airport.viplounge.entity.ShowerRoom;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ShowerRoomService extends IService<ShowerRoom> {

    List<ShowerRoom> listAvailable();

    boolean assignRoom(Long id, Long passengerId);

    boolean releaseRoom(Long id);
}
