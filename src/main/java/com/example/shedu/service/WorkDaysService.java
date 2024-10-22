package com.example.shedu.service;

import com.example.shedu.entity.Barbershop;

import com.example.shedu.entity.Days;
import com.example.shedu.entity.WorkDays;
import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.ResponseError;
import com.example.shedu.payload.req.ReqWorkDays;

import com.example.shedu.payload.res.ResWorkDay;
import com.example.shedu.repository.BarberShopRepository;
import com.example.shedu.repository.DaysRepository;
import com.example.shedu.repository.WorkDaysRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkDaysService {

    private final BarberShopRepository repository;
    private final WorkDaysRepository workDaysRepository;
    private final DaysRepository daysRepository;

    // WorkDays
        public ApiResponse saveWorkDays(ReqWorkDays reqWorkDays) {
          boolean b= workDaysRepository.existsByBarbershopId_Id(reqWorkDays.getBarbershopId());
            if (b) {
                return new ApiResponse(ResponseError.ALREADY_EXIST("WorkDays"));
            }
            Barbershop barbershop= repository.findById(reqWorkDays.getBarbershopId()).orElse(null);
            if (barbershop == null) {
                return new ApiResponse(ResponseError.NOTFOUND("Barbershop"));
            }
            List<Days> daysList = new ArrayList<>();
            for (Integer i : reqWorkDays.getDayOfWeekId()) {
                Days days = daysRepository.findById(i).orElse(null);
                daysList.add(days);
            }

            WorkDays workDays = WorkDays.builder()
                    .barbershopId(barbershop)
                    .daysList(daysList)
                    .close(reqWorkDays.getCloseTime())
                    .open(reqWorkDays.getOpenTime())
                    .build();
            workDaysRepository.save(workDays);
        return new ApiResponse("Success");
        }

        public ApiResponse update (ReqWorkDays reqWorkDays) {
        WorkDays workDays = workDaysRepository.findByBarbershopId_Id(reqWorkDays.getBarbershopId()).orElse(null);
        if (workDays != null) {
            List<Days> daysList = new ArrayList<>();
            for (Integer i : reqWorkDays.getDayOfWeekId()) {
                daysList.add(daysRepository.findById(i).orElse(null));
            }
            workDays.setDaysList(daysList);
            workDays.setOpen(reqWorkDays.getOpenTime());
            workDays.setClose(reqWorkDays.getCloseTime());
            workDaysRepository.save(workDays);
        }
            return new ApiResponse("sucsess");
        }
        public ResWorkDay getWorkDays(Long id) {
            WorkDays workDays = workDaysRepository.findByBarbershopId_Id(id).orElse(null);
            if(workDays != null) {
            ResWorkDay resWorkDay = new ResWorkDay();
            resWorkDay.setDayOfWeek(workDays.getDaysList());
            resWorkDay.setCloseTime(workDays.getClose());
            resWorkDay.setOpenTime(workDays.getOpen());
            resWorkDay.setId(workDays.getId());
            resWorkDay.setBarbershopId(workDays.getBarbershopId().getId());

            return resWorkDay;
        }
            else {
                return null;
            }
        }

}