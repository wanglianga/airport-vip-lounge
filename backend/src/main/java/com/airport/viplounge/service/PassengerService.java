package com.airport.viplounge.service;

import com.airport.viplounge.entity.Passenger;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface PassengerService extends IService<Passenger> {

    List<Passenger> listInLounge();

    boolean checkIn(Long id);

    boolean checkOut(Long id);

    boolean updateStatus(Long id, String status);

    boolean batchImport(List<Passenger> passengers);
}
