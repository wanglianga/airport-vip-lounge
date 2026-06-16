package com.airport.viplounge.service;

import com.airport.viplounge.entity.PassengerConfirmation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface PassengerConfirmationService extends IService<PassengerConfirmation> {

    List<PassengerConfirmation> listByFlightChangeId(Long flightChangeId);

    PassengerConfirmation confirmPassenger(Long id, Map<String, Object> confirmData, String confirmedBy, String confirmedByName);

    Map<String, Object> getConfirmationSummary(Long flightChangeId);
}
