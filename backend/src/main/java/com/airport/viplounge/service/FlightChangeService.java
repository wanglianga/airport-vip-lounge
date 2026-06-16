package com.airport.viplounge.service;

import com.airport.viplounge.entity.FlightChange;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface FlightChangeService extends IService<FlightChange> {

    FlightChange createFlightChange(Long flightId, String changeType, Map<String, Object> changeData, String notifiedBy, String notifiedByName);

    List<FlightChange> listByFlightId(Long flightId);

    Map<String, Object> getFlightChangeDetail(Long id);
}
