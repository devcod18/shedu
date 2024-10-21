package com.example.shedu.service;

import com.example.shedu.entity.Barbershop;
import com.example.shedu.entity.Offers;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OffersRepository offersRepository;
    private final BarberShopRepository barberShopRepository;

    public ApiResponse addOrder(ReqOrders reqOrders, User user) {
        Offers offer = offersRepository.findById(reqOrders.getServiceId())
                .orElse(null);
        if (offer == null) {
            return new ApiResponse(ResponseError.NOTFOUND("Offer not found"));
        }

        Barbershop barbershop = barberShopRepository.findById(reqOrders.getBarbershopId()).orElse(null);
        if (barbershop == null) {
            return new ApiResponse(ResponseError.NOTFOUND("Barbershop not found"));
        }

        Orders order = Orders.builder()
                .offers(offer)
                .user(user)
                .barbershop(barbershop)
                .createdAt(LocalDateTime.now())
                .duration(reqOrders.getDuration())
                .status(BookingStatus.PENDING)
                .bookingDay(reqOrders.getBookingDay())
                .bookingTime(reqOrders.getBookingTime())
                .build();

        orderRepository.save(order);
        return new ApiResponse("Success");
    }

    public ApiResponse getAllOrdersByUser(User user) {
        return new ApiResponse(orderRepository.findAllByUserId(user.getId()));
    }

    public ApiResponse getAllOrders(int page, int size) {
        var pageable = PageRequest.of(page, size);
        var ordersPage = orderRepository.findAll(pageable);

        var resOrdersList = ordersPage.map(this::toResponse).toList();
        return new ApiResponse(resOrdersList);
    }

    public ApiResponse changeStatus(Long orderId, BookingStatus status) {
        Orders order = orderRepository.findById(orderId)
                .orElse(null);
        if (order == null) {
            return new ApiResponse(ResponseError.NOTFOUND("Order not found"));
        }
        order.setStatus(status);
        orderRepository.save(order);
        return new ApiResponse("Success");
    }

    public ApiResponse updateOrder(Long orderId, ReqOrders reqOrders, User user) {
        Orders order = orderRepository.findById(orderId)
                .orElse(null);
        if (order == null) {
            return new ApiResponse(ResponseError.NOTFOUND("Order not found"));
        }

        Offers offer = offersRepository.findById(reqOrders.getServiceId()).orElse(null);
        if (offer == null) {
            return new ApiResponse(ResponseError.NOTFOUND("Offer not found"));
        }

        order.setOffers(offer);
        order.setUser(user);
        order.setBookingDay(reqOrders.getBookingDay());
        order.setBookingTime(reqOrders.getBookingTime());
        order.setDuration(reqOrders.getDuration());
        order.setStatus(BookingStatus.PENDING);

        orderRepository.save(order);
        return new ApiResponse("Success");
    }

    private ResOrders toResponse(Orders order) {
        return ResOrders.builder()
                .serviceId(order.getOffers().getId())
                .userId(order.getUser().getId())
                .createdAt(order.getCreatedAt())
                .duration(order.getDuration())
                .status(order.getStatus())
                .bookingDay(LocalDate.parse(order.getBookingDay().toString()))
                .bookingTime(LocalTime.parse(order.getBookingTime().toString()))
                .build();
    }
}
