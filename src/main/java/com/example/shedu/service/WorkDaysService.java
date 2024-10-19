package com.example.shedu.service;

import com.example.shedu.entity.Barbershop;
import com.example.shedu.entity.WorkDays;
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
    public WorkDays save(ReqWorkDays reqWorkDays) {
        Barbershop barbershop = barbershopRepository.findById(reqWorkDays.getBarbershopId()).orElse(null);
        WorkDays workDays = WorkDays.builder()
                .barbershopId(barbershop)
                .close(reqWorkDays.getClose())
                .open(reqWorkDays.getOpen())
                .daysList(reqWorkDays.getDaysList())
                .build();
        return workDaysRepository.save(workDays);
    }
    public WorkDays update(Long id, ReqWorkDays reqWorkDays) {
        WorkDays workDays = workDaysRepository.findById(id).orElse(null);
        workDays.setDaysList(reqWorkDays.getDaysList());
        workDays.setClose(reqWorkDays.getClose());
        workDays.setOpen(reqWorkDays.getOpen());
        return workDaysRepository.save(workDays);
    }

    public void delete(Long id) {
        workDaysRepository.deleteById(id);
    }
    public WorkDays getById(Long id) {
        return workDaysRepository.findById(id).orElse(null);
    }
}
