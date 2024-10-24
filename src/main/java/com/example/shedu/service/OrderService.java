package com.example.shedu.service;

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

    public ApiResponse addOrder(ReqOrders reqOrders, User user) {
        return offersRepository.findById(reqOrders.getServiceId())
                .map(service -> {
                    Orders orders = Orders.builder()
                            .offers(service)
                            .user(user)
                            .barbershop(barberShopRepository.findById(reqOrders.getBarbershopId()).orElse(null))
                            .duration(reqOrders.getDuration())
                            .bookingDaytime(reqOrders.getBookingDaytime())
                            .status(BookingStatus.PENDING)
                            .build();
                    orderRepository.save(orders);
                    return new ApiResponse("success");
                }).orElse(new ApiResponse(ResponseError.NOTFOUND("Offers")));
    }

    public ApiResponse getAllOrdersByUser(User user) {
        return new ApiResponse(orderRepository.findAllByUserId(user.getId()));
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
        return orderRepository.findById(orderId)
                .map(orders -> {
                    orders.setOffers(offersRepository.findById(reqOrders.getServiceId()).orElse(null));
                    orders.setUser(user);
                    orders.setBookingDaytime(reqOrders.getBookingDaytime());
                    orders.setDuration(reqOrders.getDuration());
                    orders.setStatus(BookingStatus.PENDING);
                    orderRepository.save(orders);
                    return new ApiResponse("success");
                }).orElse(new ApiResponse(ResponseError.NOTFOUND("Orders")));
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
