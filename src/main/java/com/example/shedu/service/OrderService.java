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
import com.example.shedu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OffersRepository serviceRepository;
    private final UserRepository userRepository;
    private final BarberShopRepository barberShopRepository;

    public ApiResponse addOrder(ReqOrders reqOrders) {
        Offers service = serviceRepository.findById(reqOrders.getServiceId()).orElse(null);

        Orders orders = Orders.builder()
                .offers(service)
                .user(userRepository.findById(reqOrders.getUserId()).orElse(null))
                .createdAt(LocalDateTime.now())
                .duration(reqOrders.getDuration())
                .status(BookingStatus.PENDING)
                .barbershop(service.getBarbershop())
                .special(reqOrders.getSpecial())
                .bookingDaytime(reqOrders.getBookingDaytime())
                .build();

        orderRepository.save(orders);
        return new ApiResponse("Succes");
    }

    public ApiResponse getAllOrdersByUser(User user) {
        List<Orders> allByUserId = orderRepository.findAllByUserId(user.getId());
        return new ApiResponse(allByUserId);
    }

    public ApiResponse getAllOrders(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Orders> ordersPage = orderRepository.findAll(pageable);

        List<ResOrders> resOrdersList = ordersPage.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return new ApiResponse(resOrdersList);
    }

    public ApiResponse changeStatus(Long orderId, BookingStatus status){
        Orders orders = orderRepository.findById(orderId).orElse(null);
        if (orders == null){
            return new ApiResponse(ResponseError.NOTFOUND("Order"));
        }
        orders.setStatus(status);
        orderRepository.save(orders);
        return new ApiResponse("Succes");
    }

    public ApiResponse updateOrder(Long orderId, ReqOrders reqOrders) {
        Orders orders = orderRepository.findById(orderId).orElse(null);
        if (orders == null){
            return new ApiResponse(ResponseError.NOTFOUND("Order"));
        }
        orders.setOffers(serviceRepository.findById(reqOrders.getServiceId()).orElse(null));
        orders.setUser(userRepository.findById(reqOrders.getUserId()).orElse(null));
        orders.setBookingDaytime(reqOrders.getBookingDaytime());
        orders.setSpecial(reqOrders.getSpecial());
        orders.setDuration(reqOrders.getDuration());
        orders.setStatus(BookingStatus.PENDING);

        orderRepository.save(orders);
        return new ApiResponse("Succes");
    }

    private ResOrders toResponse(Orders orders){
        ResOrders resOrders = ResOrders.builder()
                .serviceId(orders.getOffers().getId())
                .userId(orders.getUser().getId())
                .createdAt(orders.getCreatedAt())
                .duration(orders.getDuration())
                .status(orders.getStatus())
                .special(orders.getSpecial())
                .bookingDaytime(orders.getBookingDaytime())
                .build();

        return resOrders;
    }
}
