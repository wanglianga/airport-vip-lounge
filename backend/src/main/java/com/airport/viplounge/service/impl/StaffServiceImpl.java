package com.airport.viplounge.service.impl;

import com.airport.viplounge.entity.Staff;
import com.airport.viplounge.mapper.StaffMapper;
import com.airport.viplounge.service.StaffService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffServiceImpl extends ServiceImpl<StaffMapper, Staff> implements StaffService {

    @Override
    public List<Staff> listByRole(String role) {
        QueryWrapper<Staff> wrapper = new QueryWrapper<>();
        wrapper.eq("role", role);
        wrapper.orderByAsc("name");
        return list(wrapper);
    }

    @Override
    public boolean updateStatus(Long id, String status) {
        Staff staff = new Staff();
        staff.setId(id);
        staff.setStatus(status);
        return updateById(staff);
    }
}
