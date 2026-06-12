package com.airport.viplounge.service;

import com.airport.viplounge.entity.Staff;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface StaffService extends IService<Staff> {

    List<Staff> listByRole(String role);

    boolean updateStatus(Long id, String status);
}
