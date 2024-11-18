package com.example.shedu.service;

import com.example.shedu.entity.*;
import com.example.shedu.entity.enums.BookingStatus;
import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.ResponseError;
import com.example.shedu.payload.req.ReqOrders;
import com.example.shedu.payload.res.ResOrders;
import com.example.shedu.repository.BarberShopRepository;
import com.example.shedu.repository.OfferRepository;
import com.example.shedu.repository.OrderRepository;
import com.example.shedu.repository.WorkDaysRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OfferRepository offersRepository;
    private final BarberShopRepository barberShopRepository;
    private final NotificationService notificationService;
    private final WorkDaysRepository workDaysRepository;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private LocalTime parseTime(String time) {
        return LocalTime.parse(time, TIME_FORMATTER);
    }

    public ApiResponse addOrder(ReqOrders reqOrders, User user) {
        LocalTime startTime = parseTime(reqOrders.getStartBooking());
        LocalTime endTime = parseTime(reqOrders.getEndBooking());

        Barbershop barbershop = barberShopRepository.findById(reqOrders.getBarbershopId()).orElse(null);
        if (barbershop == null)
            return new ApiResponse(ResponseError.NOTFOUND("Barbershop"));

        boolean timeConflict = orderRepository.existsByBarbershopIdAndBookingDayAndStartBookingLessThanEqualAndEndBookingGreaterThanEqual(
                reqOrders.getBarbershopId(), reqOrders.getBookingDay(), startTime, endTime);

        if (timeConflict)
            return new ApiResponse(ResponseError.DEFAULT_ERROR("Bu vaqt oralig‘ida boshqa foydalanuvchi buyurtma qilingan."));

        Offer offer = offersRepository.findById(reqOrders.getOfferId()).orElse(null);
        if (offer == null)
            return new ApiResponse(ResponseError.NOTFOUND("Offer"));

        WorkDays workday = workDaysRepository.findByBarbershopId_Id(barbershop.getId()).orElse(null);
        if (workday == null)
            return new ApiResponse(ResponseError.NOTFOUND("Ish kuni"));

        if (!checkOrderTime(workday.getOpen(), workday.getClose(), startTime, endTime))
            return new ApiResponse(ResponseError.DEFAULT_ERROR("Buyurtma vaqti ish vaqtiga mos kelmaydi."));

        Orders orders = Orders.builder()
                .offers(offer)
                .user(user)
                .bookingDay(reqOrders.getBookingDay())
                .startBooking(startTime)
                .endBooking(endTime)
                .status(BookingStatus.PENDING)
                .barbershop(barbershop)
                .build();

        orderRepository.save(orders);
        notificationService.saveNotification(
                user, "Successfully ordered!",
                user.getFullName() + " siz muvaffaqiyatli buyurtma qildingiz!",
                0L, false
        );

        return new ApiResponse("Ordered!");
    }


    public ApiResponse getAllOrdersByUser(User user) {
        List<ResOrders> resOrders = orderRepository.findAllByUserId(user.getId()).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return new ApiResponse(resOrders);
    }

    public ApiResponse getAllOrders(int page, int size) {
        List<ResOrders> resOrdersList = orderRepository.findAll(PageRequest.of(page, size)).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return new ApiResponse(resOrdersList);
    }

    public ApiResponse changeStatus(Long orderId, BookingStatus status) {
        return orderRepository.findById(orderId)
                .map(orders -> {
                    orders.setStatus(status);
                    orderRepository.save(orders);
                    return new ApiResponse("success");
                }).orElse(new ApiResponse(ResponseError.NOTFOUND("Orders")));
    }

    public ApiResponse updateOrder(Long orderId, ReqOrders reqOrders, User user) {
        LocalTime startTime = parseTime(reqOrders.getStartBooking());
        LocalTime endTime = parseTime(reqOrders.getEndBooking());

        return orderRepository.findById(orderId)
                .map(orders -> {
                    orders.setOffers(offersRepository.findById(reqOrders.getOfferId()).orElse(null));
                    orders.setUser(user);
                    orders.setStartBooking(startTime);
                    orders.setEndBooking(endTime);
                    orders.setStatus(BookingStatus.PENDING);
                    orderRepository.save(orders);
                    return new ApiResponse("success");
                }).orElse(new ApiResponse(ResponseError.NOTFOUND("Orders")));
    }

    public ApiResponse getOrdersByBarbershop(User user) {
        List<ResOrders> resOrders = barberShopRepository.findByOwnerOrderByDesc(user.getId()).stream()
                .flatMap(barbershop -> orderRepository.findByBarbershopId(barbershop.getId()).stream())
                .map(this::toResponse)
                .toList();
        return resOrders.isEmpty() ? new ApiResponse(ResponseError.NOTFOUND("Orders")) : new ApiResponse(resOrders);
    }

    private ResOrders toResponse(Orders orders) {
        return ResOrders.builder()
                .id(orders.getId())
                .offerId(orders.getOffers().getId())
                .userId(orders.getUser().getId())
                .barbershopId(orders.getBarbershop().getId())
                .createdAt(orders.getCreatedAt())
                .start(orders.getStartBooking())
                .end(orders.getEndBooking())
                .status(orders.getStatus())
                .build();
    }

    public static boolean checkOrderTime(LocalTime openTime, LocalTime closeTime,
                                         LocalTime orderStartTime, LocalTime orderEndTime) {
        return !orderStartTime.isBefore(openTime) && !orderEndTime.isAfter(closeTime);
    }
}
