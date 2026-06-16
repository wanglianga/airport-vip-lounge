package com.airport.viplounge.controller;

import com.airport.viplounge.common.Result;
import com.airport.viplounge.entity.PassengerConfirmation;
import com.airport.viplounge.service.PassengerConfirmationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/passenger-confirmations")
public class PassengerConfirmationController {

    @Autowired
    private PassengerConfirmationService passengerConfirmationService;

    @GetMapping("/flight-change/{flightChangeId}")
    public Result<List<PassengerConfirmation>> listByFlightChangeId(@PathVariable Long flightChangeId) {
        List<PassengerConfirmation> confirmations = passengerConfirmationService.listByFlightChangeId(flightChangeId);
        return Result.success(confirmations);
    }

    @GetMapping("/{id}")
    public Result<PassengerConfirmation> getById(@PathVariable Long id) {
        PassengerConfirmation confirmation = passengerConfirmationService.getById(id);
        return Result.success(confirmation);
    }

    @PutMapping("/{id}/confirm")
    public Result<PassengerConfirmation> confirm(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        String confirmedBy = body.get("confirmedBy") != null ? (String) body.get("confirmedBy") : "RECEPTION";
        String confirmedByName = body.get("confirmedByName") != null ? (String) body.get("confirmedByName") : "前台接待";

        PassengerConfirmation confirmation = passengerConfirmationService.confirmPassenger(id, body, confirmedBy, confirmedByName);
        return Result.success(confirmation);
    }

    @GetMapping("/summary/{flightChangeId}")
    public Result<Map<String, Object>> getSummary(@PathVariable Long flightChangeId) {
        Map<String, Object> summary = passengerConfirmationService.getConfirmationSummary(flightChangeId);
        return Result.success(summary);
    }
}
