package com.airport.viplounge.controller;

import com.airport.viplounge.common.Result;
import com.airport.viplounge.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/summary")
    public Result<Map<String, Object>> getSummary() {
        Map<String, Object> summary = dashboardService.getSummary();
        return Result.success(summary);
    }

    @GetMapping("/passengers")
    public Result<List<Map<String, Object>>> getPassengers() {
        List<Map<String, Object>> passengers = dashboardService.getInLoungePassengers();
        return Result.success(passengers);
    }

    @GetMapping("/flights")
    public Result<List<Map<String, Object>>> getFlights() {
        List<Map<String, Object>> flights = dashboardService.getTodayFlights();
        return Result.success(flights);
    }

    @GetMapping("/activities")
    public Result<List<Map<String, Object>>> getActivities() {
        List<Map<String, Object>> activities = dashboardService.getRecentActivities();
        return Result.success(activities);
    }
}
