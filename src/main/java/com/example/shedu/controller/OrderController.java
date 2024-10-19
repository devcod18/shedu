package com.example.shedu.controller;

import com.example.shedu.entity.User;
import com.example.shedu.entity.enums.BookingStatus;
import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.ResponseError;
import com.example.shedu.payload.req.ReqOrders;
import com.example.shedu.security.CurrentUser;
import com.example.shedu.service.OrderService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/addOrder")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse> addOrder(@RequestBody ReqOrders reqOrders,@CurrentUser User user) {
        ApiResponse apiResponse = orderService.addOrder(reqOrders,user);
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/changeStatus/[{orderId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse> changeStatus(@PathVariable Long orderId, @RequestParam BookingStatus status) {
        ApiResponse apiResponse = orderService.changeStatus(orderId, status);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/getMyOrders")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse> getUserOrders(@CurrentUser User user) {
        ApiResponse allOrdersByUser = orderService.getAllOrdersByUser(user);
        return ResponseEntity.ok(allOrdersByUser);
    }

    @GetMapping("/getAllOrders")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    public ResponseEntity<ApiResponse> getAllOrders(@RequestParam int size, @RequestParam int page) {
        ApiResponse allOrders = orderService.getAllOrders(size, page);
        return ResponseEntity.ok(allOrders);
    }

    @PutMapping("/updateOrder/{orderId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse> updateOrder(@PathVariable Long orderId, @RequestBody ReqOrders reqOrders,@CurrentUser User user) {
        ApiResponse apiResponse = orderService.updateOrder(orderId, reqOrders,user);
        return ResponseEntity.ok(apiResponse);
    }
}
