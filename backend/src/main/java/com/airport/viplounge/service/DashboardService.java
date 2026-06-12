package com.airport.viplounge.service;

import java.util.Map;
import java.util.List;

public interface DashboardService {

    Map<String, Object> getSummary();

    List<Map<String, Object>> getInLoungePassengers();

    List<Map<String, Object>> getTodayFlights();

    List<Map<String, Object>> getRecentActivities();
}
