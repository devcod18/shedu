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

    public ApiResponse addOrder(ReqOrders reqOrders, User user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String currentDate = LocalDate.now().toString(); // Get today's date
        LocalTime startTime = LocalTime.parse(currentDate + " " + reqOrders.getStartBooking(),
                formatter);
        LocalTime endTime = LocalTime.parse(currentDate + " " + reqOrders.getEndBooking(),
                formatter);


        Barbershop barbershop = barberShopRepository.findById(reqOrders.getBarbershopId()).orElse(null);
        if (barbershop == null) {
            return new ApiResponse(ResponseError.NOTFOUND("Barbershop"));
        }

        boolean timeConflict = orderRepository.existsByBookingDayAndStartBookingLessThanEqualAndEndBookingGreaterThanEqual(
                reqOrders.getBookingDay(), startTime, endTime);
        if (timeConflict) {
            return new ApiResponse(ResponseError.DEFAULT_ERROR("Bu vaqt oralig‘ida boshqa foydalanuvchi buyurtma qilingan."));
        }

        Offer offer = offersRepository.findById(reqOrders.getOfferId()).orElse(null);
        if (offer == null) {
            return new ApiResponse(ResponseError.NOTFOUND("Offer"));
        }

        Long barbershopId = barbershop.getId();
        WorkDays workday = workDaysRepository.findByBarbershopId_Id(barbershopId)
                .orElse(null);

        if (workday == null) {
            return new ApiResponse(ResponseError.NOTFOUND("Ish kuni"));
        }


        LocalTime openTime = workday.getOpen();
        LocalTime closeTime = workday.getClose();

        boolean checkBarberShopAndOrderTime = checkOrderTime(openTime, closeTime, startTime, endTime);


//        if (reqOrders.getStartBooking().isBefore(openTime) || reqOrders.getEndBooking().isAfter(closeTime)) {
//        }
        if (checkBarberShopAndOrderTime) {

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
                    user,
                    "Successfully ordered!",
                    user.getFullName() + " siz muvaffaqiyatli buyurtma qildingiz!",
                    0L,
                    false
            );
            return new ApiResponse("Ordered!");
        }
        return new ApiResponse(ResponseError.DEFAULT_ERROR("Buyurtma vaqti ish vaqtiga mos kelmaydi."));

    }


    public ApiResponse getAllOrdersByUser(User user) {
        List<Orders> orders = orderRepository.findAllByUserId(user.getId());
        List<ResOrders> resOrders = orders.stream()
                .map(this::toResponse).collect(Collectors.toList());
        return new ApiResponse(resOrders);
    }

    public ApiResponse getAllOrders(int page, int size) {
        Page<Orders> ordersPage = orderRepository.findAll(PageRequest.of(page, size));
        List<ResOrders> resOrdersList = ordersPage.stream()
                .map(this::toResponse).collect(Collectors.toList());
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
        LocalTime starTime = LocalTime.parse(reqOrders.getStartBooking());
        LocalTime endTime = LocalTime.parse(reqOrders.getEndBooking());
        return orderRepository.findById(orderId)
                .map(orders -> {
                    orders.setOffers(offersRepository.findById(reqOrders.getOfferId()).orElse(null));
                    orders.setUser(user);
                    orders.setStartBooking(starTime);
                    orders.setEndBooking(endTime);
                    orders.setStatus(BookingStatus.PENDING);
                    orderRepository.save(orders);
                    return new ApiResponse("success");
                }).orElse(new ApiResponse(ResponseError.NOTFOUND("Orders")));
    }

    public ApiResponse getOrdersByBarbershop(User user) {
        List<Barbershop> barbershops = barberShopRepository.findByOwnerOrderByDesc(user.getId());
        if (barbershops.isEmpty()) {
            return new ApiResponse(ResponseError.NOTFOUND("Barbershops"));
        }
        List<Orders> orders = barbershops.stream()
                .flatMap(barbershop -> orderRepository.findByBarbershopId(barbershop.getId()).stream())
                .toList();
        if (orders.isEmpty()) {
            return new ApiResponse(ResponseError.NOTFOUND("Orders"));
        }
        List<ResOrders> resOrders = orders.stream()
                .map(this::toResponse)
                .toList();

        return new ApiResponse(resOrders);
    }


    private ResOrders toResponse(Orders orders) {
        return ResOrders.builder()
                .offerId(orders.getOffers().getId())
                .userId(orders.getUser().getId())
                .barbershopId(orders.getBarbershop().getId())
                .createdAt(orders.getCreatedAt())
                .start(orders.getStartBooking())
                .end(orders.getEndBooking())
                .status(orders.getStatus()).build();
    }


    public static boolean checkOrderTime(LocalTime openTime, LocalTime closeTime,
                                         LocalTime orderStartTime, LocalTime orderEndTime) {
        // Tekshiruv shartlari
        return !orderStartTime.isBefore(openTime) &&
                !orderEndTime.isAfter(closeTime);
    }

}
