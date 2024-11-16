package com.example.shedu.service;

import com.example.shedu.entity.Barbershop;
import com.example.shedu.entity.Offer;
import com.example.shedu.entity.Orders;
import com.example.shedu.entity.User;
import com.example.shedu.entity.enums.BookingStatus;
import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.ResponseError;
import com.example.shedu.payload.req.ReqOrders;
import com.example.shedu.payload.res.ResOrders;
import com.example.shedu.repository.BarberShopRepository;
import com.example.shedu.repository.OffersRepository;
import com.example.shedu.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OffersRepository offersRepository;
    private final BarberShopRepository barberShopRepository;
    private final NotificationService notificationService;

    public ApiResponse addOrder(ReqOrders reqOrders, User user) {
        Offer offer = offersRepository.findById(reqOrders.getServiceId()).orElse(null);
        if (orderRepository.existsByBookingDaytime(reqOrders.getBookingDaytime())) {
            return new ApiResponse(ResponseError.ALREADY_EXIST("Order"));
        }
        return offersRepository.findById(reqOrders.getServiceId())
                .map(service -> {
                    assert offer != null;
                    Orders orders = Orders.builder()
                            .offers(service)
                            .user(user)
                            .barbershop(barberShopRepository.findById(offer.getBarberShop().getId()).orElse(null))
                            .duration(reqOrders.getDuration())
                            .bookingDaytime(reqOrders.getBookingDaytime())
                            .status(BookingStatus.PENDING)
                            .build();
                    orderRepository.save(orders);
                    notificationService.saveNotification(
                            user,
                            "Hurmatli " + user.getFullName() + "!",
                            "Siz muvaffaqiyatli buyurtma qildingiz",
                            0L,
                            false
                    );
                    return new ApiResponse("success");
                }).orElse(new ApiResponse(ResponseError.NOTFOUND("Offers")));
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
                .serviceId(orders.getOffers().getId())
                .userId(orders.getUser().getId())
                .createdAt(orders.getCreatedAt())
                .duration(orders.getDuration())
                .status(orders.getStatus())
                .bookingDaytime(orders.getBookingDaytime())
                .build();
    }

}
