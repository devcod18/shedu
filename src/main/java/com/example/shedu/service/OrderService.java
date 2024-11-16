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
import com.example.shedu.repository.OfferRepository;
import com.example.shedu.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OfferRepository offersRepository;
    private final BarberShopRepository barberShopRepository;
    private final NotificationService notificationService;

    public ApiResponse addOrder(ReqOrders reqOrders, User user) {
        if (reqOrders.getBookingDay().isBefore(LocalDate.now())) {
            return new ApiResponse(ResponseError.DEFAULT_ERROR("Siz bron qilayotgan sana bugungi kundan orqada bo'lmasligi kerak."));
        }

        boolean timeConflict = orderRepository.existsByBookingDayAndStartBookingLessThanEqualAndEndBookingGreaterThanEqual(
                reqOrders.getBookingDay(), reqOrders.getStartBooking(), reqOrders.getEndBooking());
        if (timeConflict) {
            return new ApiResponse(ResponseError.DEFAULT_ERROR("Bu vaqt oralig‘ida boshqa foydalanuvchi buyurtma qilingan."));
        }

        Offer offer = offersRepository.findById(reqOrders.getOfferId()).orElse(null);
        if (offer == null) {
            return new ApiResponse(ResponseError.NOTFOUND("Offer"));
        }

        Orders orders = Orders.builder()
                .offers(offer)
                .user(user)
                .bookingDay(reqOrders.getBookingDay())
                .startBooking(reqOrders.getStartBooking())
                .endBooking(reqOrders.getEndBooking())
                .status(BookingStatus.PENDING)
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
        return orderRepository.findById(orderId)
                .map(orders -> {
                    orders.setOffers(offersRepository.findById(reqOrders.getOfferId()).orElse(null));
                    orders.setUser(user);
                    orders.setStartBooking(reqOrders.getStartBooking());
                    orders.setEndBooking(reqOrders.getEndBooking());
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

}
