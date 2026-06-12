package com.airport.viplounge.service.impl;

import com.airport.viplounge.entity.*;
import com.airport.viplounge.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private PassengerService passengerService;

    @Autowired
    private MealService mealService;

    @Autowired
    private ShowerRoomService showerRoomService;

    @Autowired
    private MeetingRoomService meetingRoomService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private FlightService flightService;

    @Autowired
    private ServiceRecordService serviceRecordService;

    @Override
    public Map<String, Object> getSummary() {
        Map<String, Object> summary = new HashMap<>();

        QueryWrapper<Passenger> passengerWrapper = new QueryWrapper<>();
        passengerWrapper.in("status", "ARRIVED", "IN_LOUNGE", "SERVING", "BOARDING");
        long inLoungeCount = passengerService.count(passengerWrapper);
        summary.put("inLoungePassengers", inLoungeCount);

        QueryWrapper<Meal> mealWrapper = new QueryWrapper<>();
        mealWrapper.in("status", "PENDING", "PREPARING");
        long pendingMeals = mealService.count(mealWrapper);
        summary.put("pendingMeals", pendingMeals);

        QueryWrapper<ShowerRoom> showerWrapper = new QueryWrapper<>();
        showerWrapper.eq("status", "AVAILABLE");
        long availableShowers = showerRoomService.count(showerWrapper);
        summary.put("availableShowerRooms", availableShowers);

        QueryWrapper<MeetingRoom> meetingWrapper = new QueryWrapper<>();
        meetingWrapper.eq("status", "AVAILABLE");
        long availableMeetings = meetingRoomService.count(meetingWrapper);
        summary.put("availableMeetingRooms", availableMeetings);

        QueryWrapper<Vehicle> vehicleWrapper = new QueryWrapper<>();
        vehicleWrapper.eq("status", "AVAILABLE");
        long availableVehicles = vehicleService.count(vehicleWrapper);
        summary.put("availableVehicles", availableVehicles);

        QueryWrapper<Flight> flightWrapper = new QueryWrapper<>();
        flightWrapper.in("status", "ON_TIME", "DELAYED", "BOARDING");
        long flightUpdates = flightService.count(flightWrapper);
        summary.put("flightUpdates", flightUpdates);

        return summary;
    }

    @Override
    public List<Map<String, Object>> getInLoungePassengers() {
        List<Passenger> passengers = passengerService.listInLounge();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Passenger p : passengers) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", p.getId());
            map.put("name", p.getName());
            map.put("tier", p.getTier());
            map.put("flightNo", p.getFlightNo());
            map.put("status", p.getStatus());
            map.put("checkInTime", p.getCheckInTime());
            map.put("airline", p.getAirline());
            result.add(map);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getTodayFlights() {
        List<Flight> flights = flightService.listTodayFlights();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Flight f : flights) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", f.getId());
            map.put("flightNo", f.getFlightNo());
            map.put("airline", f.getAirline());
            map.put("destination", f.getDestination());
            map.put("scheduledDeparture", f.getScheduledDeparture());
            map.put("gate", f.getGate());
            map.put("status", f.getStatus());
            map.put("isFarStand", f.getIsFarStand());
            result.add(map);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getRecentActivities() {
        List<ServiceRecord> records = serviceRecordService.list(
                new QueryWrapper<ServiceRecord>()
                        .orderByDesc("create_time")
                        .last("LIMIT 20")
        );

        List<Map<String, Object>> result = new ArrayList<>();
        for (ServiceRecord r : records) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", r.getId());
            map.put("passengerId", r.getPassengerId());
            map.put("serviceType", r.getServiceType());
            map.put("status", r.getStatus());
            map.put("staffName", r.getStaffName());
            map.put("time", r.getCreateTime());
            map.put("remark", r.getRemark());
            result.add(map);
        }
        return result;
    }
}
