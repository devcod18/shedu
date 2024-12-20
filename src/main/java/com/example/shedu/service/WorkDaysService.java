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

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkDaysService {

    private final BarberShopRepository repository;
    private final WorkDaysRepository workDaysRepository;
    private final DaysRepository daysRepository;




private LocalTime startTimeParse(String reqWorkDays){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm");
String currentDate = LocalDate.now().toString(); // Get today's dat
    return LocalTime.parse(currentDate + " " + reqWorkDays, formatter);
}
private LocalTime endTimeParse(String reqWorkDays){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm");
String currentDate = LocalDate.now().toString(); // Get today's dat
    return LocalTime.parse(currentDate + " " + reqWorkDays, formatter);
}

    public ApiResponse saveWorkDays(ReqWorkDays reqWorkDays,Long id) {

        boolean b = workDaysRepository.existsByBarbershopId_Id(id);
        if (b) {
            return new ApiResponse(ResponseError.ALREADY_EXIST("WorkDays"));
        }
        Barbershop barbershop = repository.findById(id).orElse(null);
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
                .close(endTimeParse(reqWorkDays.getCloseTime()))
                .open(startTimeParse(reqWorkDays.getOpenTime()))
                .build();
        workDaysRepository.save(workDays);
        return new ApiResponse("success");
    }

    public ApiResponse update(ReqWorkDays reqWorkDays,Long id) {
        WorkDays workDays = workDaysRepository.findByBarbershopId_Id(id).orElse(null);
        if (workDays != null) {
            List<Days> daysList = new ArrayList<>();
            for (Integer i : reqWorkDays.getDayOfWeekId()) {
                daysList.add(daysRepository.findById(i).orElse(null));
            }
            workDays.setDaysList(daysList);
            workDays.setOpen(startTimeParse(reqWorkDays.getOpenTime()));
            workDays.setClose(endTimeParse(reqWorkDays.getCloseTime()));
            workDaysRepository.save(workDays);
        }
        return new ApiResponse("success");
    }

    public ResWorkDay getWorkDays(Long id) {
        WorkDays workDays = workDaysRepository.findByBarbershopId_Id(id).orElse(null);
        if (workDays != null) {
            ResWorkDay resWorkDay = new ResWorkDay();
            resWorkDay.setDayOfWeek(workDays.getDaysList());
            resWorkDay.setCloseTime(workDays.getClose().toString());
            resWorkDay.setOpenTime(workDays.getOpen().toString());
            resWorkDay.setId(workDays.getId());
            resWorkDay.setBarbershopId(workDays.getBarbershopId().getId());

            return resWorkDay;
        } else {
            return null;
        }
    }
}