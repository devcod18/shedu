package com.example.shedu.service;

import com.example.shedu.entity.Barbershop;
import com.example.shedu.entity.WorkDays;
import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.req.ReqWorkDays;
import com.example.shedu.repository.BarberShopRepository;
import com.example.shedu.repository.WorkDaysRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkDaysService {
    private final WorkDaysRepository workDaysRepository;
    private final BarberShopRepository barbershopRepository;
 }
